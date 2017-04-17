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
            'getByUserIdAndPromotionId': {
                method: 'GET',
                isArray: true,
                url: 'api/promotion-codes/byUserAndPromotion/:userId/:promotionId',
                params:{
                  userId:'@userId',
                  promotionId: '@promotionId'
                }
            },
            'getAvailableByPromotion': {
                method: 'GET',
                isArray: true,
                url:'api/promotion-codes/availableByPromotion/:promotionId'
            },
            'redeemCode': {
              method:'GET',
              url: 'api/promotion-codes/redeem/:promotionId/:userId',
              params:{
                userId:'@userId',
                promotionId: '@promotionId'
              }
             },
            'update': { method:'PUT' }
        });
    }
})();

