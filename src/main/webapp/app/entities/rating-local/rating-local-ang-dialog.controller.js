(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RatingLocalAngDialogController', RatingLocalAngDialogController);

    RatingLocalAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RatingLocal', 'User', 'Local'];

    function RatingLocalAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RatingLocal, User, Local) {
        var vm = this;

        vm.ratingLocal = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.locals = Local.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ratingLocal.id !== null) {
                RatingLocal.update(vm.ratingLocal, onSaveSuccess, onSaveError);
            } else {
                RatingLocal.save(vm.ratingLocal, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:ratingLocalUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
