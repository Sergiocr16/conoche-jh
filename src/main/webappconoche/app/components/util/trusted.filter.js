
(function () {
    'use strict';
    angular.module('conocheApp')
        .filter('trusted', Trusted);

    Trusted.$inject = ['$sce'];
    function Trusted($sce) {
        return TrustedFilter;

        function TrustedFilter(url) {
            return $sce.trustAsResourceUrl(url);
        }
    }
})();
