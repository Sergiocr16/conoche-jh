(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RealTimeEventImageGalleryController', RealTimeEventImageGalleryController);

    RealTimeEventImageGalleryController.$inject = ['$scope', 'RealTimeEventImage', 'ParseLinks', 'AlertService', 'idEvent', 'WSRealTimeEventImages', '$timeout'];

    function RealTimeEventImageGalleryController($scope, RealTimeEventImage, ParseLinks, AlertService, idEvent, WSRealTimeEventImages, $timeout) {
        const THUMBNAIL_PADDING = 10
        const ITEMS_PER_PAGE    = 10;
        const SORT = 'creationTime,desc';

        var vm = this;
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

        function realTimeEventImages(onSuccess, onError) {
            vm.infiniteScrollDisable = true;
            RealTimeEventImage.eventRealTimeImages({
                idEvent: idEvent,
                page: vm.page,
                size: ITEMS_PER_PAGE,
                sort: SORT
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                substractMetadataFromHeaders(headers);
                onSuccess(data);
                timeoutDisable();
            }
        }


        function substractMetadataFromHeaders(headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
        }

        function loadAll () {
            realTimeEventImages(onSuccess, onError);
            function onSuccess(data) {
                vm.realTimeEventImages = vm.realTimeEventImages.concat(data);
            }
        }

        function onError(error) {
            AlertService.error(error.data.message);
            vm.infiniteScrollDisable = false;
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

        }

        function onImageDeleted(image) {
            var index = findIndex(vm.realTimeEventImages, image.id);
            if(index < 0) { return; }

            vm.infiniteScrollDisable = true;
            vm.realTimeEventImages.splice(index, 1)
            if(vm.page < vm.links.last) {
                addNextImage(vm.realTimeEventImages);
            }
            vm.links.last = Math.floor(vm.totalItems-- / ITEMS_PER_PAGE);
        }

        function findIndex(items, id) {
           return _.findIndex(items, function(current) {
                return current.id === id;
            });
        }

        function addNextImage(images) {
            RealTimeEventImage.eventRealTimeImages({
                idEvent: idEvent,
                page: images.length,
                size: 1,
                sort: SORT
            }, onSuccessNextImage, onError);
            function onSuccessNextImage(data) {
                images.push(data[0]);
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
})();
