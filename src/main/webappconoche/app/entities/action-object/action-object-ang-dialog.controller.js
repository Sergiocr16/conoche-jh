(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ActionObjectAngDialogController', ActionObjectAngDialogController);

    ActionObjectAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ActionObject', 'Action'];

    function ActionObjectAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ActionObject, Action) {
        var vm = this;

        vm.actionObject = entity;
        vm.clear = clear;
        vm.save = save;
        vm.actions = Action.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.actionObject.id !== null) {
                ActionObject.update(vm.actionObject, onSaveSuccess, onSaveError);
            } else {
                ActionObject.save(vm.actionObject, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:actionObjectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
