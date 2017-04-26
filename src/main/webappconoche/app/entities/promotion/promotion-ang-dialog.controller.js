(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('PromotionAngDialogController', PromotionAngDialogController);

    PromotionAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Promotion', 'PromotionCode', 'Event','Principal','AlertService'];

    function PromotionAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Promotion, PromotionCode, Event,Principal,AlertService) {
        var vm = this;

        vm.promotion = entity;
         vm.redeemig = false;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.promotioncodes = PromotionCode.query();
        vm.events = Event.query();

       function onError(error) {
//            AlertService.error(error.data.message);
        }
        findAvailableCodes();
        function findAvailableCodes (){
        PromotionCode.getAvailableByPromotion({promotionId: vm.promotion.id}).$promise.then(onSuccessAvailable, onError);
       }


       function onSuccessAvailablePerUser(data){
        var availableCodesPerUser = (vm.promotion.maximumCodePerUser - data.length);
           if(vm.availableCodes <= availableCodesPerUser){
            availableCodesPerUser = vm.availableCodes;
           }
          vm.availableCodesPerUser = availableCodesPerUser;
        }
        function onSuccessAvailable(data){
        vm.availableCodes = data.length;
          Principal.identity().then(function(data){
          vm.currentUserId = data.id;
            PromotionCode.getByUserIdAndPromotionId({promotionId: vm.promotion.id,userId: data.id}).$promise.then(onSuccessAvailablePerUser, onError);
           })
        }
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        vm.redeemCode = function (){
        vm.redeemig = true;
        PromotionCode.redeemCode({promotionId: vm.promotion.id,userId: vm.currentUserId}).$promise.then(onSaveSuccess, onSaveError);
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
             findAvailableCodes();
             vm.redeemig = false;
             AlertService.success('Has redimido un código');
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
