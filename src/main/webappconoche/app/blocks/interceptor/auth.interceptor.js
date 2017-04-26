(function() {
    'use strict';

    angular
        .module('conocheApp')
        .factory('authInterceptor', authInterceptor);

    authInterceptor.$inject = ['$rootScope', '$q', '$location', '$localStorage', '$sessionStorage', 'cloudinary'];

    function authInterceptor ($rootScope, $q, $location, $localStorage, $sessionStorage, cloudinary) {
        var service = {
            request: request
        };

        return service;

        function request (config) {
            /*jshint camelcase: false */
            config.headers = config.headers || {};
            if (config.url.startsWith(cloudinary.config().base_url)) {
                return config;
            }

            console.log('local token', $localStorage.authenticationToken);
            console.log('session token', $sessionStorage.authenticationToken);

            var token = $localStorage.authenticationToken || $sessionStorage.authenticationToken;
            if (token) {
                config.headers.Authorization = 'Bearer ' + token;
            }
            return config;
        }
    }
})();
