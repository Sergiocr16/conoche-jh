(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ObjectChangeDialogController', ObjectChangeDialogController);

    ObjectChangeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ObjectChange', 'ActionObject'];

    function ObjectChangeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ObjectChange, ActionObject) {
        var vm = this;

        vm.objectChange = entity;
        vm.clear = clear;
        vm.save = save;
        vm.actionobjects = ActionObject.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.objectChange.id !== null) {
                ObjectChange.update(vm.objectChange, onSaveSuccess, onSaveError);
            } else {
                ObjectChange.save(vm.objectChange, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:objectChangeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
