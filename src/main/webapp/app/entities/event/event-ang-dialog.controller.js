(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('EventAngDialogController', EventAngDialogController);

    EventAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Event', 'Promotion', 'EventImage', 'RealTimeEventImage', 'User', 'Servicio', 'Local', 'Message'];

    function EventAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Event, Promotion, EventImage, RealTimeEventImage, User, Servicio, Local, Message) {
        var vm = this;

        vm.event = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.promotions = Promotion.query();
        vm.eventimages = EventImage.query();
        vm.realtimeeventimages = RealTimeEventImage.query();
        vm.users = User.query();
        vm.servicios = Servicio.query();
        vm.locals = Local.query();
        vm.messages = Message.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.event.id !== null) {
                Event.update(vm.event, onSaveSuccess, onSaveError);
            } else {
                Event.save(vm.event, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:eventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setBanner = function ($file, event) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        event.banner = base64Data;
                        event.bannerContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.initialTime = false;
        vm.datePickerOpenStatus.finalTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
