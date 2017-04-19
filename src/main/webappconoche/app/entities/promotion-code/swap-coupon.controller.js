(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('SwapCouponController', SwapCouponController);

    SwapCouponController.$inject = ['$timeout', '$scope', '$stateParams', 'PromotionCode', 'Promotion', 'User'];

    function SwapCouponController ($timeout, $scope, $stateParams, PromotionCode, Promotion, User) {
        var vm = this;

        vm.promotionCode = {};
        vm.clear = clear;
        vm.save = save;
        vm.promotions = Promotion.query();
        vm.users = User.query();

        vm.findCoupon = function(){
        if(vm.query.length==5){
          PromotionCode.findCoupon({ code: vm.query},onSuccess,OnError)
        }
        function onSuccess(code){
        vm.promoCode = code;
        }
        function onError(){
        console.log('error')
        }
        }
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

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

            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
