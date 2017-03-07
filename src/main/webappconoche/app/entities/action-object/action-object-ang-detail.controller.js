(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ActionObjectAngDetailController', ActionObjectAngDetailController);

    ActionObjectAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ActionObject', 'Action'];

    function ActionObjectAngDetailController($scope, $rootScope, $stateParams, previousState, entity, ActionObject, Action) {
        var vm = this;

        vm.actionObject = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('conocheApp:actionObjectUpdate', function(event, result) {
            vm.actionObject = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
