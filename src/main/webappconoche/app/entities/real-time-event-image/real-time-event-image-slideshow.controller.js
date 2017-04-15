/**
 * Created by melvin on 4/12/2017.
 */

(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RealTimeEventImageSlideshow', RealTimeEventImageSlideshow);

    RealTimeEventImageSlideshow.$inject = ['$scope', '$state', 'RealTimeEventImage', 'ParseLinks', 'WSRealTimeEventImages', '$timeout', '$stateParams'];

    function RealTimeEventImageSlideshow($scope, $state, RealTimeEventImage, ParseLinks, WSRealTimeEventImages, $timeout, $stateParams) {
        const SORT = 'creationTime,desc';

        var vm = this;
        var index = -1;
        var idEvent = $stateParams.idEvent;
        var pq = [];
        vm.timeout = 5000;


        loadAll();
        WSRealTimeEventImages.receiveNewImages(idEvent)
            .then(null, null, addNewImage);

        WSRealTimeEventImages.receiveDeleteImages(idEvent)
            .then(null, null, onImageDeleted);



        function slideShow() {
            $timeout(function () {
                next();
                slideShow();
            }, vm.timeout);
        }

        function loadAll () {
            RealTimeEventImage.eventRealTimeImages({
                idEvent: idEvent,
                sort: SORT,
            }, onSuccess, onError);

            function onSuccess(data) {
                vm.images = data;
                slideShow();
                next();
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function addNewImage(image) {
            if(!vm.images) {
                return;
            }

            vm.images.unshift(image);
            pq.push(image);
            index = (index + 1) % vm.images.length;
        }

        function onImageDeleted(image) {
            if(!vm.images) {
                return;
            }
            var removed = _.remove(vm.images,
                function(img) { return img.id === image.id; });

            if(removed.length === 0) {
                return;
            }
            index = index % vm.images.length;

            //image.id puede ser undefined
            if(vm.image.id === image.id){
                next();
            }
            else if(vm.image !== vm.images[index]) {
                index = (vm.images.length + (index - 1))
                    % vm.images.length;
            }

        }

        function next() {
            if(vm.images.length === 0){
                return;
            }
            if(pq.length !== 0) {
                vm.image = pq.pop();
            }
            else {
                index = (index + 1) % vm.images.length;
                vm.image = vm.images[index];
            }
            console.log(index);
        }
    }
})()
