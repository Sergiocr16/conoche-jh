/**
 * Created by melvin on 4/22/2017.
 */
(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('NotificationController', NotificationController);

    NotificationController.$inject = ['WSNotification', '$scope', 'Principal', 'Notification'];

    function NotificationController (WSNotification, $scope, Principal, Notification, AlertService) {
        var vm = this;
        const NUMBER_OF_NOTIFICATIONS = 10;

        Principal.identity()
            .then(subscribe);

        loadAll();
        function loadAll () {
            Notification.getNotifications({
                read: false,
                page: 0,
                size: NUMBER_OF_NOTIFICATIONS,
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.totalItems    = headers('X-Total-Count');
                vm.notifications = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function subscribe(user) {
            WSNotification.receiveNotifications(user.login)
                .then(null, null, onNewNotification);

            function onNewNotification(n) {
                vm.notifications.unshift(n);
                vm.notifications.pop();
                ++vm.totalItems;
            }
        }



    }
})();
