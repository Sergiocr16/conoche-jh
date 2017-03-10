(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RealTimeEventImageAngDialogController', RealTimeEventImageAngDialogController);

    RealTimeEventImageAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'RealTimeEventImage', 'Event', 'CloudinaryService'];

    function RealTimeEventImageAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, RealTimeEventImage, Event, CloudinaryService) {
        var vm = this;

        vm.realTimeEventImage = entity;
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
            vm.isSaving = true;
            if (vm.realTimeEventImage.id !== null) {
                RealTimeEventImage.update(vm.realTimeEventImage, onSaveSuccess, onSaveError);
            } else {
                RealTimeEventImage.save(vm.realTimeEventImage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:realTimeEventImageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, realTimeEventImage) {
            if ($file && $file.$error === 'pattern') {
                return;
            }

            if ($file) {
                CloudinaryService.uploadFile($file).then(function(data) {

                    var propValue;
                    for(var propName in data) {
                        propValue = data[propName]

                        console.log(propName,propValue);
                    }
                });
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        realTimeEventImage.image = base64Data;
                        realTimeEventImage.imageContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.creationTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
