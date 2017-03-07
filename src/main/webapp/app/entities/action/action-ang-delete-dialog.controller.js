(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ActionAngDeleteController',ActionAngDeleteController);

    ActionAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'Action'];

    function ActionAngDeleteController($uibModalInstance, entity, Action) {
        var vm = this;

        vm.action = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Action.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
