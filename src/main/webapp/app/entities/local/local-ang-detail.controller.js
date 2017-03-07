(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('LocalAngDetailController', LocalAngDetailController);

    LocalAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Local', 'ImagenLocal', 'Event', 'Schedule', 'Servicio', 'User', 'RatingLocal'];

    function LocalAngDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Local, ImagenLocal, Event, Schedule, Servicio, User, RatingLocal) {
        var vm = this;

        vm.local = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('conocheApp:localUpdate', function(event, result) {
            vm.local = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
