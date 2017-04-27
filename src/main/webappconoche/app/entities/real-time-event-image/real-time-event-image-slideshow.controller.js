/**
 * Created by melvin on 4/12/2017.
 */

(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RealTimeEventImageSlideshowController', RealTimeEventImageSlideshow);

    RealTimeEventImageSlideshow.$inject = ['$scope', '$state', 'RealTimeEventImage', 'WSRealTimeEventImages', '$timeout', '$stateParams'];

    function RealTimeEventImageSlideshow($scope, $state, RealTimeEventImage, WSRealTimeEventImages, $timeout, $stateParams) {
        var SORT  = 'creationTime,desc';
        var HOURS = 1;

        var vm = this;
        var index = 0;
        var idEvent = $stateParams.idEvent;
        var timeout = null;
        vm.timeout = 5000;
        vm.pq = [];
        vm.close = close;

        loadAll();

        function initWebsockets() {
            WSRealTimeEventImages.receiveNewImages(idEvent)
                .then(null, null, addNewImage);

            WSRealTimeEventImages.receiveDeleteImages(idEvent)
                .then(null, null, onImageDeleted);
        }

        function slideShow() {
            timeout = $timeout(function () {
                setNext(index + 1);
                slideShow();
            }, vm.timeout);
        }

        function loadAll () {
            RealTimeEventImage.eventRealTimeImagesInLastHours({
                idEvent: idEvent,
                hours: HOURS,
                sort: SORT,
            }, onSuccess);

            function onSuccess(data) {
                vm.images = data;
                setNext(0);
                slideShow();
                initWebsockets();
            }
        }

        function addNewImage(image) {
            vm.images.unshift(image);
            vm.pq.push(image);
            index = (index + 1) % vm.images.length;
        }

        function onImageDeleted(image) {
            var removed = _.remove(vm.images,
                function(img) { return img.id === image.id; });

            if(removed.length === 0) {
                return;
            }

            var imagesLen = vm.images.length;
            if(vm.image.id === image.id) {
                resetTimeout(index);
            }
            else if(vm.image !== vm.images[index % imagesLen]) {
                index = (imagesLen + (index - 1)) % imagesLen;
            }
        }

        function close() {
            $state.go('event-ang-detail', { id: idEvent });
        }

        function setNext(i) {
            if(vm.images.length === 0){
                vm.image = null;
            }
            else if(vm.pq.length !== 0) {
                vm.image = vm.pq.pop();
            }
            else {
                setImage(i);
            }
        }

        function resetTimeout(i) {
            $timeout.cancel(timeout);
            setNext(i);
            slideShow();
        }

        function setImage(i) {
            index = i % vm.images.length;
            vm.image = vm.images[index];
        }

        $scope.$on('$destroy', function() {
            $timeout.cancel(timeout);
        });
    }
})();
