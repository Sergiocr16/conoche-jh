(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', '$rootScope'];

    function NavbarController ($state, Auth, Principal, ProfileService, LoginService, $rootScope) {
        var vm = this;
         angular.element(document).ready(function () {
           Layout.initHeader();
           Principal.identity().then(function(account) {
               vm.account = account;
           });
           $('#loaded').show();
           $('#loading').fadeOut(30);
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
        vm.pageTitle = $state.current.data.pageTitle;

        function login() {
            LoginService.open();

        }

        function logout() {
            Auth.logout();

            $state.go('home');
        }

        $rootScope.$on('$stateChangeStart',
            function(event, toState, toParams, fromState, fromParams){
                vm.pageTitle = toState.data.pageTitle;
            });

    }
})();
