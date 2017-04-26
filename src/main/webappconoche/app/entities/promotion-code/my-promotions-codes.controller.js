(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('MyPromotionCodesController', MyPromotionCodesController);

    MyPromotionCodesController.$inject = ['$timeout', '$scope', '$stateParams', 'PromotionCode', 'Promotion', 'User','Principal','WSPromotionCodeService','$filter','AlertService'];

    function MyPromotionCodesController ($timeout, $scope, $stateParams, PromotionCode, Promotion, User,Principal,WSPromotionCodeService,$filter,AlertService) {
        var vm = this;

       Principal.identity().then(function(user){
             vm.user = user;
             WSPromotionCodeService.receive(vm.user.id).then(null, null, deletePromotion);
        })


      function deletePromotion(promo){
      AlertService.success('conocheApp.promotionCode.swapedPromo');
      vm.promotionCodes = $filter('filter')(vm.promotionCodes, function (item) {
            return item.id !== promo.id;
          });
      }


        vm.promotionCodes = [];
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
              }
              function onError(error) {
                  AlertService.error(error.data.message);
             }
         })
         }

         loadAll();


    }
})();
