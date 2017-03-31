/**
 * Created by melvin on 3/30/2017.
 */
(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('EventServicesController', EventServicesController);

    EventServicesController.$inject = ['$scope', '$rootScope', 'event'];
    function EventServicesController($scope, $rootScope, event) {
        var vm = this;
        vm.event = event;
        var unsubscribe = $rootScope.$on('conocheApp:eventUpdate', function(e, result) {
            vm.event = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
