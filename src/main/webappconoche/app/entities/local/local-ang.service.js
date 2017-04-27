(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('Local', Local);

    Local.$inject = ['$resource'];

    function Local ($resource) {
        var resourceUrl =  'api/locals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET',  isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'getByCategory':{
                url: 'api/locals/byCategory/:categoryId',
                method: 'GET',
                isArray: 'TRUE'
            },
            'update': { method:'PUT' },
            'count': {
                method: 'GET',
                url: 'api/local/count'
            },
            'getByProvincia': {
                url: 'api/local/search',
                method: 'GET',
                isArray: true
            },
             'subscribeToLocal': {
                method: 'POST',
                url: 'api/locals/subscribeLocal',
                         },
             'unsubscribeToLocal': {
                method: 'POST',
                url: 'api/locals/unsubscribeLocal',
                         }
        });
    }
})();
