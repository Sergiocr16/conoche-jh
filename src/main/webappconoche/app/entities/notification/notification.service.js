(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('Notification', Notification);

    Notification.$inject = ['$resource'];

    function Notification ($resource) {
        var resourceUrl =  'api/notifications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },

            'getNotifications' : {
                method: 'GET',
                isArray: true,
                url: 'api/notification/user'
            },
            'readNotification' : {
                method: 'POST',
                url: 'api/notification/:id/read'
            },

        });
    }
})();