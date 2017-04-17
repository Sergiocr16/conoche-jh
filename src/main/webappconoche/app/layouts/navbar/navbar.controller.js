(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', '$rootScope', '$scope'];

    function NavbarController ($state, Auth, Principal, ProfileService, LoginService, $rootScope, $scope) {
        var vm = this;
         angular.element(document).ready(function () {
           Layout.initHeader();
           getAccount();
           $('#loaded').show();
           $('#loading').fadeOut(30);
          });


        var unsubLogin = $scope.$on('authenticationSuccess', getAccount);
        var unsubLogo  = $rootScope.$on('$stateChangeStart', logoChange);


        var logo       = ['Conoche', 'Costa Rica', 'Por la noche'];
        var current    = 0;

        vm.logo = logo[current];
        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login     = login;
        vm.logout    = logout;
        vm.$state    = $state;
        vm.pageTitle = $state.current.data.pageTitle;

        function login() {
            LoginService.open();
        }

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
            });
        }

        function logout() {
            Auth.logout();
            vm.account = undefined;
            $state.go('home');
        }

        function logoChange(event, toState) {
            vm.logo = logo[++current % logo.length];
            vm.pageTitle = toState.data.pageTitle;
            vm.phrase = toState.data.phrase;
        }



        $scope.$on('$destroy', unsubLogin);
        $scope.$on('$destroy', unsubLogo);
    }
})();
