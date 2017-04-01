(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('LandingPageController', LandingPageController);

    LandingPageController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function LandingPageController ($scope, Principal, LoginService, $state) {
        var vm = this;

         angular.element(document).ready(function () {
                $('#all').fadeIn(1000);
            });

    }
})();
