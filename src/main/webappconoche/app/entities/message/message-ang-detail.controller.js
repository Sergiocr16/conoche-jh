(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('MessageAngDetailController', MessageAngDetailController);

    MessageAngDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Message', 'User', 'Event'];

    function MessageAngDetailController($scope, $rootScope, $stateParams, previousState, entity, Message, User, Event) {
        var vm = this;

        vm.message = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('conocheApp:messageUpdate', function(event, result) {
            vm.message = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
