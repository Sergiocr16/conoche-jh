/**
 * Created by melvin on 4/12/2017.
 */
(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RealTimeEventImageSlideshow', RealTimeEventImageSlideshow);

    RealTimeEventImageSlideshow.$inject = ['$scope', '$state', 'RealTimeEventImage', 'ParseLinks', 'WSRealTimeEventImages'];

    function RealTimeEventImageSlideshow($scope, $state, RealTimeEventImage, ParseLinks, WSRealTimeEventImages) {
        const SORT = 'creationTime,desc';


        var vm = this;
    }
})()
