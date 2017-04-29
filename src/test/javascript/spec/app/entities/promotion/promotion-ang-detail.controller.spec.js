'use strict';

describe('Controller Tests', function() {

    describe('Promotion Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPromotion, MockPromotionCode, MockEvent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPromotion = jasmine.createSpy('MockPromotion');
            MockPromotionCode = jasmine.createSpy('MockPromotionCode');
            MockEvent = jasmine.createSpy('MockEvent');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Promotion': MockPromotion,
                'PromotionCode': MockPromotionCode,
                'Event': MockEvent
            };
            createController = function() {
                $injector.get('$controller')("PromotionAngDetailController", locals);
            };
        }));

    });

});
