(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function NavbarController ($state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;
         angular.element(document).ready(function () {
           Layout.initHeader();
           Principal.identity().then(function(account) {
               vm.account = account;
           });
          });
        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.$state = $state;

        function login() {
            LoginService.open();

        }

        function logout() {
            Auth.logout();

            $state.go('home');
        }



    }
})();
