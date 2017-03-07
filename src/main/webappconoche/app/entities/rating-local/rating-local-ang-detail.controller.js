(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RatingLocalAngDetailController', RatingLocalAngDetailController);

    RatingLocalAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RatingLocal', 'User', 'Local'];

    function RatingLocalAngDetailController($scope, $rootScope, $stateParams, previousState, entity, RatingLocal, User, Local) {
        var vm = this;

        vm.ratingLocal = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('conocheApp:ratingLocalUpdate', function(event, result) {
            vm.ratingLocal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
