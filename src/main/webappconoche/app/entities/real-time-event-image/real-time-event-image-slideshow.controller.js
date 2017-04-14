/**
 * Created by melvin on 4/12/2017.
 */

(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RealTimeEventImageSlideshow', RealTimeEventImageSlideshow);

    RealTimeEventImageSlideshow.$inject = ['$scope', '$state', 'RealTimeEventImage', 'ParseLinks', 'WSRealTimeEventImages', '$timeout' ];

    function RealTimeEventImageSlideshow($scope, $state, RealTimeEventImage, ParseLinks, WSRealTimeEventImages, $timeout) {
        const SORT = 'creationTime,desc';
        const images = ['http://res.cloudinary.com/dgh2svgxo/image/upload/Willaerts_Adam_The_Embarkation_of_the_Elector_Palantine_Oil_Canvas-huge_perfvo', 'http://res.cloudinary.com/dgh2svgxo/image/upload/v1492091713/banner_dl48y2.jpg']

        var vm = this;
        var index = 0;
        vm.toggleFullScreen = toggleFullScreen;
        vm.next = next;
        vm.prev = prev;
        vm.image = images[index];
        function toggleFullScreen() {
            vm.fullScreen = !vm.fullScreen;
        }

        function next() {
            index = (index + 1) % images.length
            vm.image = images[index];
        }

        function prev() {
            index = (images.length + (index - 1)) % images.length
            vm.image = images[index];
            console.log(index);
        }
    }
})()
