(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('PromotionCode', PromotionCode);

    PromotionCode.$inject = ['$resource'];

    function PromotionCode ($resource) {
        var resourceUrl =  'api/promotion-codes/:id';

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
