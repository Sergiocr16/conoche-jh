(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('Message', Message);

    Message.$inject = ['$resource', 'DateUtils'];

    function Message ($resource, DateUtils) {
        var resourceUrl =  'api/messages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationTime = DateUtils.convertDateTimeFromServer(data.creationTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'delete': { method:'DELETE' }
        });
    }
})();
