(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ObjectChangeDeleteController',ObjectChangeDeleteController);

    ObjectChangeDeleteController.$inject = ['$uibModalInstance', 'entity', 'ObjectChange'];

    function ObjectChangeDeleteController($uibModalInstance, entity, ObjectChange) {
        var vm = this;

        vm.objectChange = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ObjectChange.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
