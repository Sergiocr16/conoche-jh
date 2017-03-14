/**
 * Created by sergio on 3/14/2017.
 */

(function() {
    'use strict';


    angular
        .module('conocheApp')
        .factory('WSRealTimeEventMessages', WSRealTimeEventMessages);

    WSRealTimeEventMessages.$inject = ['StompManager'];

    function WSRealTimeEventMessages(StompManager) {
        const SUBSCRIBE_TRACKER_URL = '/topic/RealTimeEventMessage/';
        const SEND_ACTIVITY_URL = '/topic/saveRealTimeEventMessage/';

        var service = {
            receive: receive,
            sendImage: sendImage,
            subscribe: subscribe,
            unsubscribe: unsubscribe
        };

        return service;

        function receive (idEvent) {
            return StompManager.getListener(SUBSCRIBE_TRACKER_URL + idEvent);
        }

        function sendImage(message) {
            StompManager.send(SEND_ACTIVITY_URL + image.idEvent, message);
        }

        function subscribe (idEvent) {
            StompManager.subscribe(SUBSCRIBE_TRACKER_URL + idEvent);
        }

        function unsubscribe (idEvent) {
            StompManager.unsubscribe(SUBSCRIBE_TRACKER_URL + idEvent);
        }
    }
})();