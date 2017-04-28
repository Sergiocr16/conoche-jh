(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('PromotionAngDialogController', PromotionAngDialogController);

    PromotionAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Promotion', 'PromotionCode', 'Event','Principal'];

    function PromotionAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Promotion, PromotionCode, Event,Principal) {
        var vm = this;

        vm.promotion = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.promotioncodes = PromotionCode.query();
        vm.events = Event.query();

       function onError(error) {

        }

        Event.get({id:$stateParams.id},onSuccess)

        function onSuccess(data, headers) {
        vm.event = data;
            vm.updatePicker();
        }
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

     vm.updatePicker = function() {
            vm.picker1 = {
                datepickerOptions: {
                    maxDate: vm.event.finalTime,
                    minDate: vm.event.initialTime,
                    enableTime: false,
                    showWeeks: false,
                }
            };
            vm.picker2 = {
                datepickerOptions: {
                    minDate: vm.promotion.initialTime == undefined ? new Date() : vm.promotion.initialTime,
                  maxDate: vm.event.finalTime,
                    enableTime: false,
                    showWeeks: false,
                }
            }
        }


        vm.datePickerOpenStatus.initialTime = false;
        vm.datePickerOpenStatus.finalTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        function save () {
            vm.isSaving = true;
            if (vm.promotion.id !== null) {
                Promotion.update(vm.promotion, onSaveSuccess, onSaveError);
            } else {
            vm.promotion.eventId = vm.event.id;
                Promotion.save(vm.promotion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {

            $scope.$emit('conocheApp:promotionUpdate', result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, promotion) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        promotion.image = base64Data;
                        promotion.imageContentType = $file.type;
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
