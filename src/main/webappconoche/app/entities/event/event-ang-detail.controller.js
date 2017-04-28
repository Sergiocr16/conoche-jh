(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('EventAngDetailController', EventAngDetailController);

    EventAngDetailController.$inject = ['$state', '$scope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Event', 'Promotion', 'EventImage', 'RealTimeEventImage','Servicio', 'User', 'Local', 'Message','Principal','$rootScope'];

    function EventAngDetailController ($state, $scope, $stateParams, previousState, DataUtils, entity, Event, Promotion, EventImage, RealTimeEventImage,Servicio, User, Local, Message,Principal,$rootScope) {

        var vm = this;
        vm.event = entity;
        $rootScope.pageTitle = vm.event.name;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;


        Principal.identity().then(function (account) {
            vm.event.flag = 0;
            angular.forEach(vm.event.attendingUsers, function (item, index) {
                if (parseInt(item.id) == parseInt(account.id)) {
                    vm.event.flag = 1;
                }
            })
        });

        var unsubscribe = $rootScope.$on('conocheApp:eventUpdate', function (event, result) {
            vm.event = result;
        });


        vm.viewLiveMessages = function (event) {

            $state.go('live-messages', {idEvent: event.id});
        };

        vm.attend = function () {
            Event.attendToEvent(vm.event.id.toString());
            vm.event.flag = 1;
        }

        vm.dismiss = function () {
            Event.dismissEvent(vm.event.id.toString());
            vm.event.flag = 0;
        }
        vm.createComment = function (event) {

            $state.go('event-ang-detail.live-messages.newComment', {idEvent: event.id});
        };
        $scope.$on('$destroy', unsubscribe);

        Local.get({id: vm.event.localId}, onSuccess);

        function onSuccess(data, headers) {
          vm.local = data;

             if(vm.event.price==0){
                vm.event.price="Gratis";
             }
           setTimeout(function() {
              $("#container").fadeIn(500);
          }, 200);
        }
}

})();
