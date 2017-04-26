(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('LandingPageController', LandingPageController);

    function LandingPageController () {
        angular.element(document).ready(function () {
            angular.element('#all').fadeIn(1000);
        });

    }
})();
