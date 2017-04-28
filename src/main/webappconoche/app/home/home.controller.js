(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Event', 'Local', '$q', '$timeout'];

    function HomeController ($scope, Principal, LoginService, $state, Event, Local, $q, $timeout) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.history = false;
        vm.login = LoginService.open;
        vm.register = register;
        vm.event = 'event';
        vm.local = 'local';
        vm.action = vm.event;
        vm.search = search;
        vm.onKeypressed = onKeypressed;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        var map = {};
        map[vm.event] = eventsSearch;
        map[vm.local] = localSearch;

        getAccount();
        setCount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                $timeout(function() {
                    $("#homeContainer").fadeIn(1000);
                }, 200);
            });
        }
        function register () {
            $state.go('register');
        }

        function eventsSearch() {
            $state.go('event-ang', {
                history: vm.history,
                provincia: vm.provincia,
                search: vm.name,
            });
        }

        function localSearch() {
            $state.go('local-ang', {
                provincia: vm.provincia,
                search: vm.name,
            });
        }

        function onKeypressed(event) {
            if (event.which !== 13) {
                return;
            }
            search();
        }

        function search() {
            map[vm.action]();
        }

        function setCount() {
            var events =  Event.countFuture().$promise;
            var locals =  Local.count().$promise;
            $q.all([events, locals])
                .then(success);

            function success(response) {
                vm.counters = {
                    events: response[0].data,
                    locals: response[1].data
                };
            }
        }
    }
})();
