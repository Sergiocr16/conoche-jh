(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('LocalAngDeleteController',LocalAngDeleteController);

    LocalAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'Local'];

    function LocalAngDeleteController($uibModalInstance, entity, Local) {
        var vm = this;

        vm.local = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Local.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
