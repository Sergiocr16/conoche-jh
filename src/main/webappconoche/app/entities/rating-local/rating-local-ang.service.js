(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('RatingLocal', RatingLocal);

    RatingLocal.$inject = ['$resource'];

    function RatingLocal ($resource) {
        var resourceUrl =  'api/rating-locals/:id';

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
            'getByUserAndLocal': {
                    method: 'GET',
                    url: 'api/rating-locals/findByUserAndLocal/:userLogin/:localId',
                    params:{
                      userLogin:'@userLogin',
                      localId: '@localId'
                    },
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
