(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('SwapCouponController', SwapCouponController);

    SwapCouponController.$inject = ['$timeout', '$scope', '$stateParams', 'PromotionCode', 'Promotion', 'User','WSPromotionCodeService','Principal','AlertService'];

    function SwapCouponController ($timeout, $scope, $stateParams, PromotionCode, Promotion, User,WSPromotionCodeService,Principal,AlertService) {
        var vm = this;

        vm.promotionCode = {};
        vm.clear = clear;
        vm.save = save;
        vm.found=false;


        vm.swapCoupon = function(){
            vm.promoCode.promotion = undefined;
            WSPromotionCodeService.discardPromotionCode(vm.promoCode.userId,vm.promoCode);
            vm.promoCode = undefined;
            vm.found = false;
            vm.borderColor = "normal-border";
            vm.bgColor = "normal-swap";
            vm.query = undefined;
            AlertService.success('conocheApp.promotionCode.swapedPromo');
        };

        vm.findCoupon = function(){
            vm.found = false;
            vm.borderColor = "normal-border";
            vm.bgColor = "normal-swap";
            if(vm.query.length === 5){
                PromotionCode.findCoupon({ code: vm.query},onSuccess,onError);
            }

            function onSuccess(code){
                vm.promoCode = code;
                vm.borderColor = "border-success";
                vm.bgColor = "swap-coupon-found";
                vm.found = true;
            }
            function onError(){
                vm.borderColor = "border-error";
                vm.bgColor = "swap-coupon-no-found";
                vm.found = false;
            }
        };
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            vm.query = undefined;
            vm.found = false;
            vm.borderColor = "normal-border";
            vm.bgColor = "normal-swap";
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
