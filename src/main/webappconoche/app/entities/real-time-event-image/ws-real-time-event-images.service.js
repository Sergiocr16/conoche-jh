/**
 * Created by melvin on 3/11/2017.
 */
/**
 * Created by melvin on 3/4/2017.
 */
(function() {
    'use strict';


    angular
        .module('conocheApp')
        .factory('WSRealTimeEventImages', WSRealTimeEventImages);

    WSRealTimeEventImages.$inject = ['StompManager'];

    function WSRealTimeEventImages(StompManager) {
        const SUBSCRIBE_TRACKER_URL = '/topic/RealTimeEventImage/';
        const SEND_ACTIVITY_URL = '/topic/saveRealTimeEventImage/';

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

        function sendImage(image) {
            StompManager.send(SEND_ACTIVITY_URL + image.idEvent, image);
        }

        function subscribe (idEvent) {
            StompManager.subscribe(SUBSCRIBE_TRACKER_URL + idEvent);
        }

        function unsubscribe (idEvent) {
            StompManager.unsubscribe(SUBSCRIBE_TRACKER_URL + idEvent);
        }
    }
})();
