(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ActionAngDetailController', ActionAngDetailController);

    ActionAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Action', 'ActionObject'];

    function ActionAngDetailController($scope, $rootScope, $stateParams, previousState, entity, Action, ActionObject) {
        var vm = this;

        vm.action = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('conocheApp:actionUpdate', function(event, result) {
            vm.action = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
