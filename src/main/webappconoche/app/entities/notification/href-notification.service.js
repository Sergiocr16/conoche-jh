
/**
 * Created by melvin on 4/22/2017.
 */
(function() {
    'use strict';


    angular
        .module('conocheApp')
        .factory('HrefNotification', HrefNotification);

    HrefNotification.$inject = ['$state'];

    function HrefNotification($state) {

        return {
            goToState : goToState
        };
        function goToState(notification) {
            if(!notification) {
                return;
            }
            var object = notification.actionObject;
            if(!object
                || !object.objectId
                || !object.objectType) {
                return;
            }

            var id = object.objectId;
            switch (object.objectType) {
            case "EVENT":
                $state.go('event-ang-detail', { id: id });
                break;
            case "LOCAL":
                $state.go('local-ang-detail', { id: id });
                break;
            case "PROMOTION":
                $state.go('event-ang.promotionDetail', { id: id});
                break;
            case "USER":
                throw 'NotImplementedError';
            case "REALTIME_EVENT_IMAGE":
                throw 'NotImplementedError';
            case "RATING":
                throw 'NotImplementedError';
            default:
                throw 'NotSupportedError';
            }
        }
    }
})();


