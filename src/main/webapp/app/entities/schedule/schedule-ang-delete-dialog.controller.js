(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ScheduleAngDeleteController',ScheduleAngDeleteController);

    ScheduleAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'Schedule'];

    function ScheduleAngDeleteController($uibModalInstance, entity, Schedule) {
        var vm = this;

        vm.schedule = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Schedule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
