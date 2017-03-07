(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ScheduleAngDialogController', ScheduleAngDialogController);

    ScheduleAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Schedule', 'Local'];

    function ScheduleAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Schedule, Local) {
        var vm = this;

        vm.schedule = entity;
        vm.clear = clear;
        vm.save = save;
        vm.locals = Local.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.schedule.id !== null) {
                Schedule.update(vm.schedule, onSaveSuccess, onSaveError);
            } else {
                Schedule.save(vm.schedule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:scheduleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
