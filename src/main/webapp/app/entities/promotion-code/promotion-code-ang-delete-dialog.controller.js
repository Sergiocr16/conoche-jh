(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('PromotionCodeAngDeleteController',PromotionCodeAngDeleteController);

    PromotionCodeAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'PromotionCode'];

    function PromotionCodeAngDeleteController($uibModalInstance, entity, PromotionCode) {
        var vm = this;

        vm.promotionCode = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PromotionCode.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
