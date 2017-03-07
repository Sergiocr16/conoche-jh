(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ImagenLocalAngDialogController', ImagenLocalAngDialogController);

    ImagenLocalAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ImagenLocal', 'Local'];

    function ImagenLocalAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ImagenLocal, Local) {
        var vm = this;

        vm.imagenLocal = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.locals = Local.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.imagenLocal.id !== null) {
                ImagenLocal.update(vm.imagenLocal, onSaveSuccess, onSaveError);
            } else {
                ImagenLocal.save(vm.imagenLocal, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:imagenLocalUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, imagenLocal) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        imagenLocal.image = base64Data;
                        imagenLocal.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
