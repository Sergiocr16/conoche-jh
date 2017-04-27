(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('ObjectChange', ObjectChange);

    ObjectChange.$inject = ['$resource'];

    function ObjectChange ($resource) {
        var resourceUrl =  'api/object-changes/:id';

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
