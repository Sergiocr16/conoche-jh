(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ServicioAngDeleteController',ServicioAngDeleteController);

    ServicioAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'Servicio'];

    function ServicioAngDeleteController($uibModalInstance, entity, Servicio) {
        var vm = this;

        vm.servicio = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Servicio.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
