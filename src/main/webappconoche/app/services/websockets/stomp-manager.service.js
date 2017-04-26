/**
 * Created by melvin on 3/11/2017.
 */
/**
 * Created by melvin on 2/16/2017.
 * Cambiar a ecmascript 5
 */
(function () {
    'use strict';
    /* globals SockJS, Stomp */

    var _slicedToArray = function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; }();

    angular.module('conocheApp').factory('StompManager', StompManager);

    StompManager.$inject = ['$window', '$q', 'AuthServerProvider'];

    function StompManager($window, $q, AuthServerProvider) {

        var END_POINT = 'websocket/tracker';
        var RECONECT_TIME = 7000;

        var stompClient = null;
        var connected = $q.defer();
        var subscribeMap = new Map();

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

        function connect() {
            var headers = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

            var socket = new SockJS(buildUrl());
            stompClient = Stomp.over(socket);
            stompClient.connect(headers, function () {
                return connected.resolve('success');
            }, function () {
                return onConnectionLost(headers);
            });

            return connected.promise;
        }

        function onConnectionLost(headers) {
            stompClient = null;
            connected.reject('Connection_Lost');
            connected = $q.defer();
            if (AuthServerProvider.getToken()) {
                setTimeout(function () {
                    return reconnect(headers);
                }, RECONECT_TIME);
            }
        }

        function isConnected() {
            return stompClient !== null && stompClient.connected;
        }

        function buildUrl() {
            var loc = $window.location;
            var url = '//' + loc.host + loc.pathname + END_POINT;

            var authToken = AuthServerProvider.getToken();
            if (authToken) {
                url += '?access_token=' + authToken;
            }
            return url;
        }

        function disconnect() {
            connected = $q.defer();
            if (stompClient !== null) {
                stompClient.disconnect();
                stompClient = null;
            }
        }

        function getListener(url) {
            var value = subscribeMap.get(url);
            if (!value) {
                throw 'there is no subscription to url = ' + url + '.';
            }
            return value.listener.promise;
        }

        function send(url, payload) {
            var headers = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};

            connected.promise.then(function () {
                if (isConnected()) {
                    stompClient.send(url, headers, angular.toJson(payload));
                }
            });
        }

        function reconnect() {
            var headers = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

            connect(headers);
            var _iteratorNormalCompletion = true;
            var _didIteratorError = false;
            var _iteratorError = undefined;

            try {
                for (var _iterator = subscribeMap[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
                    var _ref = _step.value;

                    var _ref2 = _slicedToArray(_ref, 2);

                    var url = _ref2[0];
                    var value = _ref2[1];

                    subscribeKeyValue(url, value);
                }
            } catch (err) {
                _didIteratorError = true;
                _iteratorError = err;
            } finally {
                try {
                    if (!_iteratorNormalCompletion && _iterator.return) {
                        _iterator.return();
                    }
                } finally {
                    if (_didIteratorError) {
                        throw _iteratorError;
                    }
                }
            }

            return connected.promise;
        }

        function subscribe(url) {
            var value = subscribeMap.get(url) || { subscriber: null, listener: $q.defer() };
            subscribeMap.set(url, value);
            subscribeKeyValue(url, value);
            console.log(subscribeMap);
        }
        function subscribeKeyValue(url, value) {
            connected.promise.then(function () {
                if (isConnected()) {
                    value.subscriber = stompClient.subscribe(url, function (data) {
                        return value.listener.notify(angular.fromJson(data.body));
                    });
                }
            });
        }

        function unsubscribe(url) {
            var value = subscribeMap.get(url);
            if (subscribeMap.delete(url) && value.subscriber !== null) {
                value.subscriber.unsubscribe();
            }
        }
    }
})();
