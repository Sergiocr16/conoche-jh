/**
 * Created by melvin on 3/11/2017.
 */
/**
 * Created by melvin on 2/16/2017.
 * Cambiar a ecmascript 5
 */
(function() {
    'use strict';
    /* globals SockJS, Stomp */

    angular
        .module('conocheApp')
        .factory('StompManager', StompManager);

    StompManager.$inject = ['$window', '$q', 'AuthServerProvider'];

    function StompManager ($window, $q, AuthServerProvider) {

        const END_POINT = 'websocket/tracker';
        const RECONECT_TIME = 7000;

        let stompClient = null;
        let connected = $q.defer();
        let subscribeMap = new Map();


        var service = {
            connect: reconnect,
            disconnect: disconnect,
            getListener: getListener,
            send: send,
            subscribe: subscribe,
            unsubscribe: unsubscribe,
            isConnected: isConnected
        };

        return service;

        function connect (headers = {}) {
            let socket = new SockJS(buildUrl());
            stompClient = Stomp.over(socket);
            stompClient.connect(headers,
                () => connected.resolve('success'),
                () => onConnectionLost(headers));

            return connected.promise;
        }

        function onConnectionLost(headers) {
            stompClient = null;
            connected.reject('Connection_Lost');
            connected = $q.defer();
            if(AuthServerProvider.getToken()) {
                setTimeout(() => reconnect(headers), RECONECT_TIME)
            }
        }

        function isConnected() {
            return stompClient !== null && stompClient.connected;
        }

        function buildUrl() {
            let loc = $window.location;
            let url = `//${loc.host}${loc.pathname}${END_POINT}`;

            let authToken = AuthServerProvider.getToken();
            if(authToken){
                url += `?access_token=${authToken}`;
            }
            return url;
        }

        function disconnect () {
            if (stompClient !== null) {
                connected = $q.defer();
                stompClient.disconnect();
                stompClient = null;
            }
        }

        function getListener (url) {
            let value = subscribeMap.get(url);
            if(!value) {
                throw `there is no subscription to url = ${url}.`
            }
            return value.listener.promise;
        }

        function send(url, payload, headers = {}) {
            connected.promise.then(() => {
                if (isConnected()) {
                    stompClient.send(url, headers, angular.toJson(payload));
                }
            });
        }

        function reconnect(headers = {}) {
            connect(headers);
            for (let [url, value] of subscribeMap) {
                subscribeKeyValue(url, value);
            }
            return connected.promise;
        }

        function subscribe (url) {
            let value = subscribeMap.get(url)
                || { subscriber: null, listener: $q.defer() };
            subscribeMap.set(url, value);
            subscribeKeyValue(url, value);
            console.log(subscribeMap);
        }
        function subscribeKeyValue(url, value) {
            connected.promise.then(() => {
                value.subscriber = stompClient.subscribe(url,
                    data => value.listener.notify(angular.fromJson(data.body)));
            });
        }

        function unsubscribe (url) {
            let value = subscribeMap.get(url);
            if (subscribeMap.delete(url)
                && value.subscriber !== null) {
                value.subscriber.unsubscribe();
            }
        }
    }
})();
