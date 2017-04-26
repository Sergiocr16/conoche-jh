(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('PromotionAngDetailController', PromotionAngDetailController);

    PromotionAngDetailController.$inject = [ '$uibModalInstance', '$timeout','Principal','$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Promotion', 'PromotionCode', 'Event'];

    function PromotionAngDetailController( $uibModalInstance, $timeout,Principal,$scope, $rootScope, $stateParams,  DataUtils, entity, Promotion, PromotionCode, Event) {
        var vm = this;
  vm.clear = clear;
        vm.promotion = entity;

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

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


        vm.redeemCode = function (){
        PromotionCode.redeemCode({promotionId: vm.promotion.id,userId: vm.currentUserId}).$promise.then(onSaveSuccess, onSaveError);
        }
   function onSaveError () {
            vm.isSaving = false;
        }
       function onError(error) {
//            AlertService.error(error.data.message);
        }
        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:promotionUpdate', result);
             findAvailableCodes();
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
