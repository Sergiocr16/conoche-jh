(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('Action', Action);

    Action.$inject = ['$resource', 'DateUtils'];

    function Action ($resource, DateUtils) {
        var resourceUrl =  'api/actions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creation = DateUtils.convertDateTimeFromServer(data.creation);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
