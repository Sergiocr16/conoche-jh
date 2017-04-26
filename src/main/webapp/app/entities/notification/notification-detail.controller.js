(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('NotificationDetailController', NotificationDetailController);

    NotificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Notification', 'User', 'ActionObject'];

    function NotificationDetailController($scope, $rootScope, $stateParams, previousState, entity, Notification, User, ActionObject) {
        var vm = this;

        vm.notification = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('conocheApp:notificationUpdate', function(event, result) {
            vm.notification = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
