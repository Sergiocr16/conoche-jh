(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('PromotionAngDeleteController',PromotionAngDeleteController);

    PromotionAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'Promotion'];

    function PromotionAngDeleteController($uibModalInstance, entity, Promotion) {
        var vm = this;

        vm.promotion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Promotion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
