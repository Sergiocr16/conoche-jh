(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('EventAngDetailController', EventAngDetailController);

    EventAngDetailController.$inject = ['$scope','$state', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Event', 'Promotion', 'EventImage', 'RealTimeEventImage', 'User', 'Servicio', 'Local', 'Message'];

    function EventAngDetailController($scope,$state, $rootScope, $stateParams, previousState, DataUtils, entity, Event, Promotion, EventImage, RealTimeEventImage, User, Servicio, Local, Message) {

        var vm = this;
        vm.event = entity;
        $rootScope.pageTitle = vm.event.name;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;



        var unsubscribe = $rootScope.$on('conocheApp:eventUpdate', function(event, result) {
            vm.event = result;
        });

        console.log("load controller");

        vm.viewLiveMessages = function(event){
        $state.go('live-messages',{ idEvent: event.id })
        }
        vm.createComment = function(event){

         $state.go('event-ang-detail.live-messages.newComment',{ idEvent: event.id})
        }
        $scope.$on('$destroy', unsubscribe);

        Local.get({id: vm.event.localId},onSuccess);
        function onSuccess(data, headers) {
          vm.local = data;
             if(vm.event.price==0){
                vm.event.price="Gratis";
             }
           setTimeout(function() {
              $("#container").fadeIn(500);
          }, 200)

        }

    }
})();
