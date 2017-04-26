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
            subscribeToDeadEntities: subscribeToDeadEntities,
            unsubscribeToDeadEntities: unsubscribeToDeadEntities,
            receiveDeadEntities: receiveDeadEntities
        };

        return service;

        function buildUrl(login) {
            return '/user/' + login + '/queue/notifications';
        }

        function buildDeadUrl(login) {
            return '/user/' + login + '/queue/notifications/dead'
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

        function subscribeToDeadEntities(login) {
            StompManager.subscribe(buildDeadUrl(login));
        }

        function unsubscribeToDeadEntities(login) {
            StompManager.unsubscribe(buildDeadUrl(login));
        }

        function receiveDeadEntities(login) {
            return StompManager.getListener(buildDeadUrl(login));
        }
    }
})();
