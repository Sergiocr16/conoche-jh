(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('PromotionAngDetailController', PromotionAngDetailController);

    PromotionAngDetailController.$inject = ['$uibModalInstance', '$timeout','Principal','$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Promotion', 'PromotionCode', 'Event','AlertService'];

    function PromotionAngDetailController( $uibModalInstance, $timeout,Principal,$scope, $rootScope, $stateParams,  DataUtils, entity, Promotion, PromotionCode, Event,AlertService) {
        var vm = this;
        vm.promotion = entity;
        vm.redeemig = false;
        vm.clear = clear;


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
            });
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        vm.redeemCode = function (){
            vm.redeemig = true;
            PromotionCode.redeemCode({promotionId: vm.promotion.id,userId: vm.currentUserId}).$promise.then(onSaveSuccess, onSaveError);
        };

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
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        var unsubscribe = $rootScope.$on('conocheApp:promotionUpdate', function(event, result) {
            vm.promotion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
