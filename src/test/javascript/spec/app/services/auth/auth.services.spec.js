'use strict';

describe('Service Tests', function () {
    beforeEach(mockApiAccountCall);
    beforeEach(mockI18nCalls);
    beforeEach(mockScriptsCalls);

    describe('Auth', function () {
        var $httpBackend, localStorageService, sessionStorageService, authService, spiedAuthServerProvider;

        beforeEach(inject(function($injector, $localStorage, $sessionStorage, Auth, AuthServerProvider) {
            $httpBackend = $injector.get('$httpBackend');
            localStorageService = $localStorage;
            sessionStorageService = $sessionStorage;
            authService = Auth;
            spiedAuthServerProvider = AuthServerProvider;
        }));
        //make sure no expectations were missed in your tests.
        //(e.g. expectGET or expectPOST)
        afterEach(function() {
            $httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.verifyNoOutstandingRequest();
        });
    });
});
