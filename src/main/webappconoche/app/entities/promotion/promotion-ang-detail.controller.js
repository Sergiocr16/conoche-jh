(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('PromotionAngDetailController', PromotionAngDetailController);

    PromotionAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Promotion', 'PromotionCode', 'Event'];

    function PromotionAngDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Promotion, PromotionCode, Event) {
        var vm = this;

        vm.promotion = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('conocheApp:promotionUpdate', function(event, result) {
            vm.promotion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
