(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('EventImageAngDetailController', EventImageAngDetailController);

    EventImageAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'EventImage', 'Event'];

    function EventImageAngDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, EventImage, Event) {
        var vm = this;

        vm.eventImage = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('conocheApp:eventImageUpdate', function(event, result) {
            vm.eventImage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
