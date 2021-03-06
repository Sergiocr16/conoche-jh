(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('Event', Event);

    Event.$inject = ['$resource', 'DateUtils'];

    function Event ($resource, DateUtils) {
        var resourceUrl =  'api/events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.initialTime = DateUtils.convertDateTimeFromServer(data.initialTime);
                        data.finalTime = DateUtils.convertDateTimeFromServer(data.finalTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
