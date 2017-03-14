(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('MessageAngDialogController', MessageAngDialogController);

    MessageAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Message','Principal','WSRealTimeEventMessages'];

    function MessageAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Message, Principal,WSRealTimeEventMessages) {
        var vm = this;

        vm.message = entity;
        console.log(vm.message);
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
//        vm.users = User.query();
//        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
        Principal.identity().then(function(account){
            vm.isSaving = true;
            vm.message.creationTime = new Date();
            vm.message.userId = account.id;
            vm.message.login = account.login;
           console.log(vm.message)
           WSRealTimeEventMessages.sendMessage(vm.message);
           $scope.$emit('conocheApp:messageUpdate');
           $uibModalInstance.close();
//            if (vm.message.id !== null) {
//                 Message.update(vm.message, onSaveSuccess, onSaveError);
//             } else {
//                 Message.save(vm.message, onSaveSuccess, onSaveError);
//             }
        })

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
