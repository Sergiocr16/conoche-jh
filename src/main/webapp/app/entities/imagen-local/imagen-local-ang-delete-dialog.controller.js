(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ImagenLocalAngDeleteController',ImagenLocalAngDeleteController);

    ImagenLocalAngDeleteController.$inject = ['$uibModalInstance', 'entity', 'ImagenLocal'];

    function ImagenLocalAngDeleteController($uibModalInstance, entity, ImagenLocal) {
        var vm = this;

        vm.imagenLocal = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ImagenLocal.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
