(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('Servicio', Servicio);

    Servicio.$inject = ['$resource'];

    function Servicio ($resource) {
        var resourceUrl =  'api/servicios/:id';

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
