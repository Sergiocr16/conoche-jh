(function() {
    'use strict';
//agregar lista de imagenes e index a directiva slider.
    angular
        .module('conocheApp')
        .controller('RealTimeEventImageGalleryController', RealTimeEventImageGalleryController);

    RealTimeEventImageGalleryController.$inject = ['$scope', '$state', 'RealTimeEventImage', 'ParseLinks', 'AlertService',
        'paginationConstants', 'page', 'idEvent', 'WSRealTimeEventImages', 'isOwner', '$uibModal'];

    function RealTimeEventImageGalleryController($scope, $state, RealTimeEventImage, ParseLinks,
                                                 AlertService, paginationConstants,
                                                 page, idEvent, WSRealTimeEventImages, isOwner, $uibModal) {
        const SORT              = 'creationTime,desc';
        const THUMBNAIL_PADDING = 10;

        var vm         = this;
        var modal      = null;

        vm.index       = 0;
        vm.image       = { id:  -1 };
        vm.page        = page;
        vm.width       = 340;
        vm.border      = 5;
        vm.isOwner     = isOwner;
        vm.showGallery = false;

        vm.transition        = transition;
        vm.computeDimentions = computeDimentions;
        vm.toggleFullScreen  = toggleFullScreen;
        vm.toSlideshow       = toSlideshow;
        vm.next              = next;
        vm.prev              = prev;
        vm.close             = close;
        vm.callModal         = callModal;
        vm.itemsPerPage      = paginationConstants.itemsPerPage;

        var unsubscribe = $scope.$on('masonry.created', function(element) {
            vm.showGallery = true;
        });
        $scope.$on('$destroy', unsubscribe);


        loadAll();


        WSRealTimeEventImages.receiveNewImages(idEvent)
            .then(null, null, addNewImage);

        WSRealTimeEventImages.receiveDeleteImages(idEvent)
            .then(null, null, onImageDeleted);


        function loadAll () {
            RealTimeEventImage.eventRealTimeImages({
                idEvent: idEvent,
                page: vm.page - 1,
                size: vm.itemsPerPage,
                sort: SORT
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                var link = ParseLinks.parse(headers('link'));
                vm.links               = link;
                vm.totalItems          = headers('X-Total-Count');
                vm.realTimeEventImages = data;
                vm.page                = page;
                refreshIndex();
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function addNewImage(image) {
            if(!vm.realTimeEventImages) {
                return;
            }

            if(vm.page != 1) {
                loadAll()
            }
            else if(!contains(image)){
                if (vm.realTimeEventImages.length % vm.itemsPerPage === 0) {
                    vm.realTimeEventImages.pop();
                }
                vm.links.last = Math.floor(vm.totalItems++ / vm.itemsPerPage);
                vm.realTimeEventImages.unshift(image);
                refreshIndex();
            }

        }

        function onImageDeleted(image) {
            var last = vm.realTimeEventImages[vm.realTimeEventImages.length - 1];
            if(Date.parse(image.creationTime) < Date.parse(last.creationTime)) {
                return;
            }
            loadAll();
        }

        function toggleFullScreen(image) {
            image.fullScreen = !image.fullScreen;
        }

        function computeDimentions(aspectRatio) {
            var borderOffset = 2 * vm.border + THUMBNAIL_PADDING;
            return {
                'width' : vm.width + borderOffset,
                'height' :  (vm.width * aspectRatio) + borderOffset
            }
        }

        function transition() {
            var current = $state.$current;
            var options = {
                inherit: true,
                reload: current.name
            };
            $state.transitionTo(current,
                { page: vm.page }, options);
        }

        function contains(image) {
            var item = _.find( vm.realTimeEventImages, function(img) {
                return img.id === image.id;
            });
            return item !== undefined;
        }

        function toSlideshow() {
            var url = $state.href('real-time-event-image-slideshow', {idEvent: idEvent});
            window.open(url,'_blank');
        }
        function callModal($index) {
            modal = $uibModal.open({
                templateUrl: 'app/entities/real-time-event-image/fullscreen-real-time-event-image.html',
                scope: $scope,
                size: 'lg'
            });
            setImage($index);
        }

        function close() {
            if(!modal) { return; }
            modal.close();
            modal = null;
        }


        function refreshIndex() {
            var images = vm.realTimeEventImages;
            if (images.length === 0) {
                return close();
            }
            var i = _.findIndex(images, function(img) {
                return vm.image.id == img.id;
            });
            setImage(i >= 0 ? i : vm.index);

        }

        function next() {
            setImage(vm.index + 1);
        }

        function prev() {
            setImage(vm.realTimeEventImages.length + (vm.index - 1));
        }

        function setImage(i) {
            var images = vm.realTimeEventImages;
            vm.index = i % images.length;
            vm.image = images[vm.index];
        }
    }
})();
