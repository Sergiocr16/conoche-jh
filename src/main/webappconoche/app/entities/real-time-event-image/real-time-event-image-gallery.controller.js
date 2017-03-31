(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RealTimeEventImageGalleryController', RealTimeEventImageGalleryController);

    RealTimeEventImageGalleryController.$inject = ['$scope', '$state', 'RealTimeEventImage', 'ParseLinks', 'AlertService',
        'paginationConstants', 'page', 'idEvent', 'WSRealTimeEventImages', 'isOwner'];

    function RealTimeEventImageGalleryController($scope, $state, RealTimeEventImage, ParseLinks,
                                                 AlertService, paginationConstants,
                                                 page, idEvent, WSRealTimeEventImages, isOwner) {
        const SORT              = 'creationTime,desc';
        const THUMBNAIL_PADDING = 10;

        var vm         = this;
        vm.page        = page;
        vm.width       = 330;
        vm.border      = 5;
        vm.isOwner     = isOwner;
        vm.showGallery = false;

        vm.transition        = transition;
        vm.computeDimentions = computeDimentions;
        vm.toggleFullScreen  = toggleFullScreen;
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
    }
/*
    function RealTimeEventImageGalleryController($scope, RealTimeEventImage, ParseLinks, AlertService, idEvent, WSRealTimeEventImages, $timeout) {
        const THUMBNAIL_PADDING = 10
        const ITEMS_PER_PAGE    = 10;
        const SORT = 'creationTime,desc';

        var vm = this;
        var deleteQueue = [];
        var createQueue = [];
        var loadingPage = false;

        vm.width = 330;
        vm.border = 5;
        vm.infiniteScrollDisable = true;
        vm.loadPage = loadPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.reset = reset;
        vm.computeDimentions = computeDimentions;
        vm.toggleFullScreen = toggleFullScreen;
        vm.deleteImage = deleteImage;

        init();
        WSRealTimeEventImages.receiveNewImages(idEvent)
            .then(null, null, addNewImage);

        WSRealTimeEventImages.receiveDeleteImages(idEvent)
            .then(null, null, onImageDeleted);

        function init() {
            realTimeEventImages(onSuccess, onError);
            function onSuccess(data) {
                vm.realTimeEventImages = data;
            }
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

        function realTimeEventImages(callback, onError) {
            loadingPage = true;
            vm.infiniteScrollDisable = true;
            RealTimeEventImage.eventRealTimeImages({
                idEvent: idEvent,
                page: vm.page,
                size: ITEMS_PER_PAGE,
                sort: SORT
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                substractMetadataFromHeaders(headers);
                callback(data);
                clearDeleteQueue();
                loadingPage = false;
                timeoutDisable();
            }
        }

        function clearDeleteQueue() {
            console.log(deleteQueue.length, deleteQueue);
            _.forEach(deleteQueue, function(callback) {
                callback();
            });
            deleteQueue = [];
        }

        function substractMetadataFromHeaders(headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
        }

        function loadAll () {
            realTimeEventImages(onSuccess, onError);
            function onSuccess(data) {
                vm.realTimeEventImages = vm.realTimeEventImages.concat(data);
                $scope.$emit('masonry.reloaded');
            }
        }

        function onError(error) {
            AlertService.error(error.data.message);
            vm.infiniteScrollDisable = false;
            loadingPage = false;
        }

        function contains(image) {
            var item = _.find( vm.realTimeEventImages, function(img) {
                return img.id === image.id;
            });
            return item !== undefined;
        }

        function timeoutDisable() {
            $timeout(function() { vm.infiniteScrollDisable = false; } , 400);
        }

        function addNewImage(image) {
            if(!vm.realTimeEventImages
                || contains(image)) {
                return;
            }
            if(vm.realTimeEventImages.length % ITEMS_PER_PAGE === 0) {
                vm.realTimeEventImages.pop();
            }
            vm.links.last = Math.floor(vm.totalItems++ / ITEMS_PER_PAGE);
            vm.realTimeEventImages.unshift(image);
        }

        function deleteImage(image) {
            WSRealTimeEventImages.deleteImage(image);
        }

        function onImageDeleted(image) {
            if(loadingPage) {
                deleteQueue.push(function () { addDeletedImage(image); });
            }
            else {
                addDeletedImage(image);
            }
        }

        function addDeletedImage(image) {
            var index = findIndex(vm.realTimeEventImages, image.id);
            if(index < 0) { return; }

            vm.infiniteScrollDisable = true;
            vm.realTimeEventImages.splice(index, 1)
            if(vm.page < vm.links.last) {
                addNextImage(vm.realTimeEventImages);
            }
            vm.links.last = Math.floor(vm.totalItems-- / ITEMS_PER_PAGE);
            vm.page = Math.min(vm.page, vm.links.last);
        }

        function findIndex(items, id) {
           return _.findIndex(items, function(current) {
                return current.id === id;
            });
        }

        function addNextImage() {
            RealTimeEventImage.eventRealTimeImages({
                idEvent: idEvent,
                page:  vm.realTimeEventImages.length,
                size: 1,
                sort: SORT
            }, onSuccessNextImage, onError);
            function onSuccessNextImage(data) {
               if(vm.realTimeEventImages.length % ITEMS_PER_PAGE !== 0) {
                   vm.realTimeEventImages.push(data[0]);
               }
               vm.infiniteScrollDisable = false;
            }
        }

        function reset () {
            vm.page = 0;
            init();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }

    */
})();
