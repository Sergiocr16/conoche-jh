(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('ActionObject', ActionObject);

    ActionObject.$inject = ['$resource', 'DateUtils'];

    function ActionObject ($resource, DateUtils) {
        var resourceUrl =  'api/action-objects/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
