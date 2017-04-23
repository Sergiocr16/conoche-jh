/**
 * Created by melvin on 4/22/2017.
 */
(function() {
    'use strict';


    angular
        .module('conocheApp')
        .factory('WSNotification', WSNotification);

    WSNotification.$inject = ['StompManager'];

    function WSNotification(StompManager) {

        var service = {
            subscribeNotification: subscribeNotification,
            receiveNotifications: receiveNotifications,
            unsubcribeNotification: unsubcribeNotification,
        };

        return service;

        function buildUrl(login) {
            return '/user/' + login + '/queue/notifications';
        }

        function receiveNotifications (login) {
            return StompManager.getListener(buildUrl(login));
        }

        function unsubcribeNotification(login) {
            StompManager.unsubscribe(buildUrl(login));
        }


        function subscribeNotification(login) {
            StompManager.subscribe(buildUrl(login));
        }
    }
})();
