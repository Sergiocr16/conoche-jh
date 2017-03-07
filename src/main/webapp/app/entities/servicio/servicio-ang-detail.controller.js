(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ServicioAngDetailController', ServicioAngDetailController);

    ServicioAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Servicio'];

    function ServicioAngDetailController($scope, $rootScope, $stateParams, previousState, entity, Servicio) {
        var vm = this;

        vm.servicio = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('conocheApp:servicioUpdate', function(event, result) {
            vm.servicio = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
