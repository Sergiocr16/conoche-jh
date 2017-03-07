(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('ImagenLocalAngDetailController', ImagenLocalAngDetailController);

    ImagenLocalAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ImagenLocal', 'Local'];

    function ImagenLocalAngDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ImagenLocal, Local) {
        var vm = this;

        vm.imagenLocal = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('conocheApp:imagenLocalUpdate', function(event, result) {
            vm.imagenLocal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
