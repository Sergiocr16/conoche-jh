(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RealTimeEventImageAngDeleteController',RealTimeEventImageAngDeleteController);

    RealTimeEventImageAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'RealTimeEventImage'];

    function RealTimeEventImageAngDeleteController($uibModalInstance, entity, RealTimeEventImage) {
        var vm = this;

        vm.realTimeEventImage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RealTimeEventImage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
