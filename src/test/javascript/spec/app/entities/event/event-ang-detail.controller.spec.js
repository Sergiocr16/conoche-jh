'use strict';

describe('Controller Tests', function() {

    describe('Event Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEvent, MockPromotion, MockEventImage, MockRealTimeEventImage, MockUser, MockServicio, MockLocal, MockMessage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEvent = jasmine.createSpy('MockEvent');
            MockPromotion = jasmine.createSpy('MockPromotion');
            MockEventImage = jasmine.createSpy('MockEventImage');
            MockRealTimeEventImage = jasmine.createSpy('MockRealTimeEventImage');
            MockUser = jasmine.createSpy('MockUser');
            MockServicio = jasmine.createSpy('MockServicio');
            MockLocal = jasmine.createSpy('MockLocal');
            MockMessage = jasmine.createSpy('MockMessage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Event': MockEvent,
                'Promotion': MockPromotion,
                'EventImage': MockEventImage,
                'RealTimeEventImage': MockRealTimeEventImage,
                'User': MockUser,
                'Servicio': MockServicio,
                'Local': MockLocal,
                'Message': MockMessage
            };
            createController = function() {
                $injector.get('$controller')("EventAngDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'conocheApp:eventUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
