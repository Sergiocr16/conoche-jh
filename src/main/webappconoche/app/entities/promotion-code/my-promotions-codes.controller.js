(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('MyPromotionCodesController', MyPromotionCodesController);

    MyPromotionCodesController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'PromotionCode', 'Promotion', 'User','Principal','WSPromotionCodeService','$filter'];

    function MyPromotionCodesController ($timeout, $scope, $stateParams, $uibModalInstance, PromotionCode, Promotion, User,Principal,WSPromotionCodeService,$filter) {
        var vm = this;

       Principal.identity().then(function(user){
             vm.user = user;
             WSPromotionCodeService.receive(vm.user.id).then(null, null, deletePromotion);
        })


      function deletePromotion(promo){
      vm.promotionCodes = $filter('filter')(vm.promotionCodes, {id: promo.id})
      }


        vm.promotionCodes = [];
        vm.clear = clear;
        vm.save = save;
         function loadAll () {
         Principal.identity().then(function(user){
              PromotionCode.getByUserId({
                 userId: user.id
              }, onSuccess, onError);
              function sort() {
                  var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                  if (vm.predicate !== 'id') {
                      result.push('id');
                  }
                  return result;
              }
              function onSuccess(data, headers) {
                  vm.totalItems = headers('X-Total-Count');
                  vm.queryCount = vm.totalItems;
                  vm.promotionCodes = data;
                  console.log(data);
              }
              function onError(error) {
                  AlertService.error(error.data.message);
             }
         })
         }

         loadAll();

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.promotionCode.id !== null) {
                PromotionCode.update(vm.promotionCode, onSaveSuccess, onSaveError);
            } else {
                PromotionCode.save(vm.promotionCode, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:promotionCodeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
