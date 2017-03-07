(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ScheduleAngDetailController', ScheduleAngDetailController);

    ScheduleAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Schedule', 'Local'];

    function ScheduleAngDetailController($scope, $rootScope, $stateParams, previousState, entity, Schedule, Local) {
        var vm = this;

        vm.schedule = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('conocheApp:scheduleUpdate', function(event, result) {
            vm.schedule = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
