(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RealTimeEventImageAngDialogController', RealTimeEventImageAngDialogController);

    RealTimeEventImageAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RealTimeEventImage', 'Event'];

    function RealTimeEventImageAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RealTimeEventImage, Event) {
        var vm = this;

        vm.realTimeEventImage = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.realTimeEventImage.id !== null) {
                RealTimeEventImage.update(vm.realTimeEventImage, onSaveSuccess, onSaveError);
            } else {
                RealTimeEventImage.save(vm.realTimeEventImage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:realTimeEventImageUpdate', result);
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
