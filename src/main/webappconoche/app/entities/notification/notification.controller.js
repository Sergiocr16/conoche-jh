/**
 * Created by melvin on 4/22/2017.
 */
(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('NotificationController', NotificationController);

    NotificationController.$inject = ['WSNotification', '$scope', 'Principal'];

    function NotificationController (WSNotification, $scope, Principal) {
        var vm = this;

        Principal.identity()
            .then(subscribe);


        function subscribe(user) {
            WSNotification.receiveNotifications(user.login)
                .then(null, null, onNewNotification);

            function onNewNotification(n) {
                console.log(JSON.stringify(n));
            }
        }

        $scope.$on('$destroy', function() {
            console.log('destroy');
        });


    }
})();
