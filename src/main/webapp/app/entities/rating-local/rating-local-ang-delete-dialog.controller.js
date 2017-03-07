(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RatingLocalAngDeleteController',RatingLocalAngDeleteController);

    RatingLocalAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'RatingLocal'];

    function RatingLocalAngDeleteController($uibModalInstance, entity, RatingLocal) {
        var vm = this;

        vm.ratingLocal = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RatingLocal.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
