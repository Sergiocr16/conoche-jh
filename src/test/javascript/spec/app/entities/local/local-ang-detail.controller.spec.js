'use strict';

describe('Controller Tests', function() {

    describe('Local Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLocal, MockImagenLocal, MockEvent, MockSchedule, MockServicio, MockUser, MockRatingLocal;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLocal = jasmine.createSpy('MockLocal');
            MockImagenLocal = jasmine.createSpy('MockImagenLocal');
            MockEvent = jasmine.createSpy('MockEvent');
            MockSchedule = jasmine.createSpy('MockSchedule');
            MockServicio = jasmine.createSpy('MockServicio');
            MockUser = jasmine.createSpy('MockUser');
            MockRatingLocal = jasmine.createSpy('MockRatingLocal');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Local': MockLocal,
                'ImagenLocal': MockImagenLocal,
                'Event': MockEvent,
                'Schedule': MockSchedule,
                'Servicio': MockServicio,
                'User': MockUser,
                'RatingLocal': MockRatingLocal
            };
            createController = function() {
                $injector.get('$controller')("LocalAngDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'conocheApp:localUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
