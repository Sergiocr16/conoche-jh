(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('ImagenLocal', ImagenLocal);

    ImagenLocal.$inject = ['$resource'];

    function ImagenLocal ($resource) {
        var resourceUrl =  'api/imagen-locals/:id';

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
