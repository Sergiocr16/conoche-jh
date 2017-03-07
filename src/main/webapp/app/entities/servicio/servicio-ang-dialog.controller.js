(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ServicioAngDialogController', ServicioAngDialogController);

    ServicioAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Servicio'];

    function ServicioAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Servicio) {
        var vm = this;

        vm.servicio = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.servicio.id !== null) {
                Servicio.update(vm.servicio, onSaveSuccess, onSaveError);
            } else {
                Servicio.save(vm.servicio, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:servicioUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
