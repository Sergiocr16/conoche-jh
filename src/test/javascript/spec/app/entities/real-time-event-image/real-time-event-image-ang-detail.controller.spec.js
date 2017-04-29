'use strict';

describe('Controller Tests', function() {

    describe('RealTimeEventImage Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRealTimeEventImage, MockEvent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRealTimeEventImage = jasmine.createSpy('MockRealTimeEventImage');
            MockEvent = jasmine.createSpy('MockEvent');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'RealTimeEventImage': MockRealTimeEventImage,
                'Event': MockEvent
            };
            createController = function() {
                $injector.get('$controller')("RealTimeEventImageAngDetailController", locals);
            };
        }));

    });

});
