/**
 * Created by sergio on 3/14/2017.
 */

(function() {
    'use strict';


    angular
        .module('conocheApp')
        .factory('WSPromotionCodeService', WSPromotionCodeService);

    WSPromotionCodeService.$inject = ['StompManager'];

    function WSPromotionCodeService(StompManager) {
        var SUBSCRIBE_TRACKER_URL = '/topic/deletePromotionCode/';
        var DISCARD_ACTIVITY_URL = '/topic/deletedPromotionCode/';

        var service = {
            receive: receive,
            subscribe: subscribe,
            unsubscribe: unsubscribe,
            discardPromotionCode: discardPromotionCode,
        };

        return service;

        function receive (userId) {
            return StompManager.getListener(SUBSCRIBE_TRACKER_URL + userId);
        }

        function discardPromotionCode(userId,promotionCode) {
            StompManager.send(DISCARD_ACTIVITY_URL+userId, promotionCode);
        }

        function subscribe (userId) {
            StompManager.subscribe(SUBSCRIBE_TRACKER_URL + userId);
        }

        function unsubscribe (userId) {
            StompManager.unsubscribe(SUBSCRIBE_TRACKER_URL + userId);
        }
    }
})();
