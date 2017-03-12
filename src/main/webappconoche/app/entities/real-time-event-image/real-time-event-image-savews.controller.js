/**
 * Created by melvin on 3/11/2017.
 */
(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RealTimeEventImageSaveWS', RealTimeEventImageAngDialogController);

    RealTimeEventImageAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'Event', 'RealTimeEventImageCloudinary'];

    function RealTimeEventImageAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, Event, RealTimeEventImageCloudinary) {
        var vm = this;
        var fileImage = null;

        vm.realTimeEventImage = {idEvent: $stateParams.idEvent};
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            vm.isSaving = true
            RealTimeEventImageCloudinary
                .save(fileImage, vm.realTimeEventImage)
                .then(onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:realTimeEventImageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        vm.displayImage = base64Data;
                        vm.displayImageType = $file.type;
                    });
                });
                fileImage = $file;
            }
        };
        vm.datePickerOpenStatus.creationTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
