(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('RatingLocal', RatingLocal);

    RatingLocal.$inject = ['$resource', 'DateUtils'];

    function RatingLocal ($resource, DateUtils) {
        var resourceUrl =  'api/rating-locals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
