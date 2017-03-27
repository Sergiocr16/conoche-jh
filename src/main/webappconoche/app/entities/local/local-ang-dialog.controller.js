(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('LocalAngDialogController', LocalAngDialogController);

    LocalAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Local', 'ImagenLocal', 'Event', 'Schedule', 'Servicio', 'User', 'RatingLocal','Category'];

    function LocalAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Local, ImagenLocal, Event, Schedule, Servicio, User, RatingLocal,Category) {
        var vm = this;

        vm.local = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.imagenlocals = ImagenLocal.query();
        vm.events = Event.query();
        vm.schedules = Schedule.query();
        vm.servicios = Servicio.query();
        vm.users = User.query();
        vm.ratinglocals = RatingLocal.query();
        vm.categories = Category.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.local.id !== null) {
                Local.update(vm.local, onSaveSuccess, onSaveError);
            } else {
                Local.save(vm.local, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:localUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setBanner = function ($file, local) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        local.banner = base64Data;
                        local.bannerContentType = $file.type;
                    });
                });
            }
        };

    }
})();
