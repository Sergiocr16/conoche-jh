(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ActionAngDialogController', ActionAngDialogController);

    ActionAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Action', 'ActionObject', 'User'];

    function ActionAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Action, ActionObject, User) {
        var vm = this;

        vm.action = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.actionobjects = ActionObject.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.action.id !== null) {
                Action.update(vm.action, onSaveSuccess, onSaveError);
            } else {
                Action.save(vm.action, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:actionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creation = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
