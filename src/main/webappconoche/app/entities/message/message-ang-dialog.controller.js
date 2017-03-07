(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('MessageAngDialogController', MessageAngDialogController);

    MessageAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Message', 'User', 'Event'];

    function MessageAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Message, User, Event) {
        var vm = this;

        vm.message = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.message.id !== null) {
                Message.update(vm.message, onSaveSuccess, onSaveError);
            } else {
                Message.save(vm.message, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:messageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
