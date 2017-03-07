(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('PromotionCodeAngDetailController', PromotionCodeAngDetailController);

    PromotionCodeAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PromotionCode', 'Promotion', 'User'];

    function PromotionCodeAngDetailController($scope, $rootScope, $stateParams, previousState, entity, PromotionCode, Promotion, User) {
        var vm = this;

        vm.promotionCode = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('conocheApp:promotionCodeUpdate', function(event, result) {
            vm.promotionCode = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
