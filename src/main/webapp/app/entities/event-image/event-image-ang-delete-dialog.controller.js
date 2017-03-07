(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('EventImageAngDeleteController',EventImageAngDeleteController);

    EventImageAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'EventImage'];

    function EventImageAngDeleteController($uibModalInstance, entity, EventImage) {
        var vm = this;

        vm.eventImage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EventImage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
