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
        const NUMBER_OF_NOTIFICATIONS = 6;

        Principal.identity()
            .then(subscribe);



        function loadAll () {
            Notification.getNotifications({
                read: false,
                page: 1,
                size: vm.itemsPerPage,
                sort: SORT
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                var link = ParseLinks.parse(headers('link'));
                vm.links               = link;
                vm.totalItems          = headers('X-Total-Count');
                vm.notifications = data;
                vm.page                = page;
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
            }
        }



    }
})();
