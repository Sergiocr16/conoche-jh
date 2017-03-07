(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('EventImageAngDialogController', EventImageAngDialogController);

    EventImageAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'EventImage', 'Event'];

    function EventImageAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, EventImage, Event) {
        var vm = this;

        vm.eventImage = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.eventImage.id !== null) {
                EventImage.update(vm.eventImage, onSaveSuccess, onSaveError);
            } else {
                EventImage.save(vm.eventImage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:eventImageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, eventImage) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        eventImage.image = base64Data;
                        eventImage.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
