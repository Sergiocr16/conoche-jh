(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ObjectChangeDetailController', ObjectChangeDetailController);

    ObjectChangeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ObjectChange', 'ActionObject'];

    function ObjectChangeDetailController($scope, $rootScope, $stateParams, previousState, entity, ObjectChange, ActionObject) {
        var vm = this;

        vm.objectChange = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('conocheApp:objectChangeUpdate', function(event, result) {
            vm.objectChange = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();