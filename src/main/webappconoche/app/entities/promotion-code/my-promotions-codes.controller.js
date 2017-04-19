(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('MyPromotionCodesController', MyPromotionCodesController);

    MyPromotionCodesController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'PromotionCode', 'Promotion', 'User','Principal'];

    function MyPromotionCodesController ($timeout, $scope, $stateParams, $uibModalInstance, PromotionCode, Promotion, User,Principal) {
        var vm = this;

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
