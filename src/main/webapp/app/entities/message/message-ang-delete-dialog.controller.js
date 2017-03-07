(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('MessageAngDeleteController',MessageAngDeleteController);

    MessageAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'Message'];

    function MessageAngDeleteController($uibModalInstance, entity, Message) {
        var vm = this;

        vm.message = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Message.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
