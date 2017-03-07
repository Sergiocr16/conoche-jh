(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ActionObjectAngDeleteController',ActionObjectAngDeleteController);

    ActionObjectAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'ActionObject'];

    function ActionObjectAngDeleteController($uibModalInstance, entity, ActionObject) {
        var vm = this;

        vm.actionObject = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ActionObject.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
