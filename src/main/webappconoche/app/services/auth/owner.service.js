/**
 * Created by melvin on 3/28/2017.
 */
(function() {
    'use strict';

    angular
        .module('conocheApp')
        .factory('Owner', OwnerService);

    OwnerService.$inject = ['$http'];

    function OwnerService ($http) {

        var services = { isOwner: isOwner };
        return services;

        function isOwner(EventId) {
            var resourceUrl =  'api/is-owner/';
            return $http.get(resourceUrl + EventId)
                .then(function(result) { return result.data; });
        }
    }
})();
