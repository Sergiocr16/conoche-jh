(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('AllNotificationController', AllNotificationController);

    AllNotificationController.$inject = ['$state', 'Notification', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams' , 'Principal', 'WSNotification', 'HrefNotification'];

    function AllNotificationController($state, Notification, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, WSNotification, HrefNotification) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.read = read;
        Principal.identity()
            .then(subscribe);
        loadAll();

        function loadAll () {
            Notification.getNotifications({
                read : false,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.notifications = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


        function subscribe(user) {
            WSNotification.receiveNotifications(user.login)
                .then(null, null, loadAll);
            WSNotification.receiveDeadEntities(user.login)
                .then(null, null, loadAll)
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
            });
        }

        function read(notification) {
            Notification.readNotification({id: notification.id},
                null, onSuccess);
            function onSuccess() {
                HrefNotification.goToState(notification);
            }
        }
    }
})();
