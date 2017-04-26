/**
 * Created by melvin on 4/22/2017.
 */
(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('NotificationController', NotificationController);

    NotificationController.$inject = ['WSNotification', '$scope', 'Principal', 'Notification', 'AlertService', 'HrefNotification'];

    function NotificationController (WSNotification, $scope, Principal, Notification, AlertService, HrefNotification) {
        var vm = this;
        const NUMBER_OF_NOTIFICATIONS = 10;

        vm.read = read;

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
            WSNotification.receiveDeadEntities(user.login)
                .then(null, null, onDeadLink)

            function onNewNotification(n) {
                if(NUMBER_OF_NOTIFICATIONS == vm.notifications.length) {
                    vm.notifications.pop();
                }
                vm.notifications.unshift(n);
                ++vm.totalItems;
            }

            function onDeadLink() {
                loadAll();
            }
        }

        function read(notification) {
            Notification.readNotification({id: notification.id},
                null, onSuccess);
            function onSuccess() {
                HrefNotification.goToState(notification);
                loadAll();
            }
        }
    }
})();
