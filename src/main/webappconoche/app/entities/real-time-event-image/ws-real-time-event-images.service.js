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
        var SAVE_SUBSCRIBE_IMAGE_URL = '/topic/RealTimeEventImage/';
        var SAVE_SEND_IMAGE_URL = '/topic/saveRealTimeEventImage/';
        var DELETE_SUBSCRIBE_IMAGE_URL = '/topic/deletedRealTimeEventImage/';
        var DELETE_SEND_IMAGE_URL = '/topic/deleteRealTimeEventImage/';


        var service = {
            receiveNewImages: receiveNewImages,
            sendImage: sendImage,
            subscribeNewImages: subscribeNewImages,
            unsubscribeNewImages: unsubscribeNewImages,
            receiveDeleteImages: receiveDeleteImages,
            deleteImage: deleteImage,
            subscribeDeleteImages: subscribeDeleteImages,
            unsubscribeDeleteImages: unsubscribeDeleteImages

        };

        return service;

        function receiveNewImages (idEvent) {
            return StompManager.getListener(SAVE_SUBSCRIBE_IMAGE_URL + idEvent);
        }

        function sendImage(image) {
            StompManager.send(SAVE_SEND_IMAGE_URL + image.idEvent, image);
        }

        function subscribeNewImages (idEvent) {
            StompManager.subscribe(SAVE_SUBSCRIBE_IMAGE_URL + idEvent);
        }

        function unsubscribeNewImages (idEvent) {
            StompManager.unsubscribe(SAVE_SUBSCRIBE_IMAGE_URL + idEvent);
        }

        function receiveDeleteImages (idEvent) {
            return StompManager.getListener(DELETE_SUBSCRIBE_IMAGE_URL + idEvent);
        }

        function deleteImage(image) {
            StompManager.send(DELETE_SEND_IMAGE_URL + image.id);
        }

        function subscribeDeleteImages (idEvent) {
            StompManager.subscribe(DELETE_SUBSCRIBE_IMAGE_URL + idEvent);
        }

        function unsubscribeDeleteImages (idEvent) {
            StompManager.unsubscribe(DELETE_SUBSCRIBE_IMAGE_URL + idEvent);
        }
    }
})();
