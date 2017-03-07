(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('PromotionCodeAngDialogController', PromotionCodeAngDialogController);

    PromotionCodeAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PromotionCode', 'Promotion', 'User'];

    function PromotionCodeAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PromotionCode, Promotion, User) {
        var vm = this;

        vm.promotionCode = entity;
        vm.clear = clear;
        vm.save = save;
        vm.promotions = Promotion.query();
        vm.users = User.query();

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
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
