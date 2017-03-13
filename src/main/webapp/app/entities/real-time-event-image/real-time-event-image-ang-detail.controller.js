(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RealTimeEventImageAngDetailController', RealTimeEventImageAngDetailController);

    RealTimeEventImageAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RealTimeEventImage', 'Event'];

    function RealTimeEventImageAngDetailController($scope, $rootScope, $stateParams, previousState, entity, RealTimeEventImage, Event) {
        var vm = this;

        vm.realTimeEventImage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('conocheApp:realTimeEventImageUpdate', function(event, result) {
            vm.realTimeEventImage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
