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
        const DISCARD_ACTIVITY_URL = '/topic/RealTimeEventMessageDiscard/';

        var service = {
            receive: receive,
            sendMessage: sendMessage,
            subscribe: subscribe,
            unsubscribe: unsubscribe,
            discardViewedMessage,
        };

        return service;

        function receive (idEvent) {
            return StompManager.getListener(SUBSCRIBE_TRACKER_URL + idEvent);
        }

        function sendMessage(message) {
            StompManager.send(SEND_ACTIVITY_URL + message.eventId, message);
        }

        function discardViewedMessage(message) {
             StompManager.send(DISCARD_ACTIVITY_URL, message);
        }

        function subscribe (idEvent) {
            StompManager.subscribe(SUBSCRIBE_TRACKER_URL + idEvent);
        }

        function unsubscribe (idEvent) {
            StompManager.unsubscribe(SUBSCRIBE_TRACKER_URL + idEvent);
        }
    }
})();
