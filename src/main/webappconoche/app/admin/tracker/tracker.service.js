(function() {
    'use strict';


    angular
        .module('conocheApp')
        .factory('JhiTrackerService', JhiTrackerService);

    JhiTrackerService.$inject = ['$rootScope', 'StompManager'];

    function JhiTrackerService ($rootScope, StompManager) {
        var SUBSCRIBE_TRACKER_URL = '/topic/tracker';
        var SEND_ACTIVITY_URL ='/topic/activity';

        var alreadyConnectedOnce = false;

        var service = {
            connect: connect,
            receive: receive,
            sendActivity: sendActivity,
            subscribe: subscribe,
            unsubscribe: unsubscribe
        };

        return service;

        function connect () {
            var stateChangeStart;
            StompManager.connect().then(function() {
                sendActivity();
                if (!alreadyConnectedOnce) {
                    stateChangeStart = $rootScope.$on('$stateChangeStart', function () {
                        sendActivity();
                    });
                    alreadyConnectedOnce = true;
                }
            });
            $rootScope.$on('$destroy', function () {
                if(angular.isDefined(stateChangeStart) && stateChangeStart !== null){
                    stateChangeStart();
                }
            });
        }


        function receive () {
            return StompManager.getListener(SUBSCRIBE_TRACKER_URL);
        }

        function sendActivity() {
            StompManager.send(SEND_ACTIVITY_URL, {'page': $rootScope.toState.name});
        }

        function subscribe () {
            StompManager.subscribe(SUBSCRIBE_TRACKER_URL);
        }

        function unsubscribe () {
            StompManager.unsubscribe(SUBSCRIBE_TRACKER_URL);
        }
    }
})();

