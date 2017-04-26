/**
 * Created by melvin on 3/28/2017.
 */
(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RealTimeEventImageGalleryDeleteController', RealTimeEventImageGalleryDeleteController);

    RealTimeEventImageGalleryDeleteController.$inject = ['$uibModalInstance', 'entity', 'WSRealTimeEventImages'];

    function RealTimeEventImageGalleryDeleteController($uibModalInstance, entity, WSRealTimeEventImages) {
        var vm = this;

        vm.realTimeEventImage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;


        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (image) {
            WSRealTimeEventImages.deleteImage(image);
            $uibModalInstance.close(true);
        }
    }
})();
