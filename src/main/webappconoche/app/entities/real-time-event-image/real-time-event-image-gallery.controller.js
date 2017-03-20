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
        vm.width = 190;
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
        WSRealTimeEventImages.receive(idEvent)
            .then(null, null, addNewImage);


        function init() {
            realTimeEventImages(onSuccess, onError);
            function onSuccess(data, headers) {
                substractMetadataFromHeaders(headers);
                vm.realTimeEventImages = data;
                timeoutDisable();
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
        }


        function substractMetadataFromHeaders(headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
        }

        function loadAll () {
            realTimeEventImages(onSuccess, onError);
            function onSuccess(data, headers) {
                substractMetadataFromHeaders(headers);
                vm.realTimeEventImages = vm.realTimeEventImages.concat(data);
                timeoutDisable();
            }

        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function contains(image) {
            var item = _.find( vm.realTimeEventImages, function(img) {
                return img.id === image.id;
            });
            return item !== undefined;
        }

        function timeoutDisable() {
            $timeout(function() {
                vm.infiniteScrollDisable = false;
            } , 400);
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
