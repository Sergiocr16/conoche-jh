'use strict';

describe('Controller Tests', function() {

    describe('Notification Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockNotification, MockUser, MockActionObject;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockNotification = jasmine.createSpy('MockNotification');
            MockUser = jasmine.createSpy('MockUser');
            MockActionObject = jasmine.createSpy('MockActionObject');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Notification': MockNotification,
                'User': MockUser,
                'ActionObject': MockActionObject
            };
            createController = function() {
                $injector.get('$controller')("NotificationDetailController", locals);
            };
        }));
    });

});
