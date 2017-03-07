(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('ActionObject', ActionObject);

    ActionObject.$inject = ['$resource'];

    function ActionObject ($resource) {
        var resourceUrl =  'api/action-objects/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
