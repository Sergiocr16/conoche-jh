(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('PromotionAngDialogController', PromotionAngDialogController);

    PromotionAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Promotion', 'PromotionCode', 'Event'];

    function PromotionAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Promotion, PromotionCode, Event) {
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

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.promotion.id !== null) {
                Promotion.update(vm.promotion, onSaveSuccess, onSaveError);
            } else {
                Promotion.save(vm.promotion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:promotionUpdate', result);
            $uibModalInstance.close(result);
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
