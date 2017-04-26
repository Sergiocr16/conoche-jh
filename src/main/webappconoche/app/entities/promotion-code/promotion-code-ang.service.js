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
            'findCoupon': {
                method: 'GET',
                url: 'api/promotion-codes/findCode/:code',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                },
                params:{
                  code:'@code'
                }
            },
            'getByUserId': {
                method: 'GET',
                isArray: true,
                url: 'api/promotion-codes/byUser/:userId',
                params:{
                  userId:'@userId'
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

