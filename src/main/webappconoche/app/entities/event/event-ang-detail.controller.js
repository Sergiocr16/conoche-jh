(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('EventAngDetailController', EventAngDetailController);

    EventAngDetailController.$inject = ['$scope','$state', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Event', 'Promotion', 'EventImage', 'RealTimeEventImage', 'User', 'Servicio', 'Local', 'Message'];

    function EventAngDetailController($scope,$state, $rootScope, $stateParams, previousState, DataUtils, entity, Event, Promotion, EventImage, RealTimeEventImage, User, Servicio, Local, Message) {
        var vm = this;

        vm.event = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('conocheApp:eventUpdate', function(event, result) {
            vm.event = result;
        });

        vm.viewLiveMessages = function(event){
        $state.go('live-messages',{ idEvent: event.id })
        }
        vm.createComment = function(event){

         $state.go('event-ang-detail.newComment',{ idEvent: event.id})
        }
        $scope.$on('$destroy', unsubscribe);
    }
})();
