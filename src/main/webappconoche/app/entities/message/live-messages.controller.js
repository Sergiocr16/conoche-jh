(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('LiveMessagesController', LiveMessagesController);

    LiveMessagesController.$inject = ['ParseLinks','Message', 'AlertService', 'paginationConstants', '$stateParams', 'WSRealTimeEventMessages', '$timeout','User'];

    function LiveMessagesController(ParseLinks, Message, AlertService, paginationConstants, $stateParams, WSRealTimeEventMessages, $timeout, User) {

        var vm = this;
        vm.idEvent = $stateParams.idEvent;
        vm.liveMessages = [];
        vm.loadPage = loadPage;
        vm.isShowing = 0;
        vm.totalMessages = 0;
        vm.currentMessage = 0;
        loadAll();
        function loadAll () {
            Message.query({
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.liveMessages = data;
                if(vm.liveMessages.length>0){
                vm.totalMessages = vm.liveMessages.length-1;
                vm.currentMessage = vm.currentMessage+1;
                showMessage(vm.liveMessages[0]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

         WSRealTimeEventMessages.receive(vm.idEvent).then(null, null, addNewMessage);

         function addNewMessage(message) {
         console.log("ALGO PASO");
            if(vm.isShowing == 1){
              loadMessage(message);
              console.log("TOTAL MESAGES" +  vm.totalMessages);
            }else{
              showMessage(message);
            }
         }

        function showMessage(message){

        vm.isShowing = 1;
          vm.message = message;
          $('#message').fadeIn(700);
          $timeout(function(){
          $('#message').fadeOut(700);
           loopMessages();
           finishLoop();
           vm.isShowing = 0;
           WSRealTimeEventMessages.discardViewedMessage(message);
             console.log("TOTAL MESAGES" +  vm.totalMessages);
          },4000)
        }
        function loopMessages(){
//          console.log("ANTES")
//           console.log("MENSAJE CURRENT"  +  (vm.currentMessage));
//           console.log("TOTAL MESAGES" +  vm.totalMessages);
        $timeout(function(){
           if(vm.totalMessages > 0){
           vm.currentMessage = vm.currentMessage + 1;
           showMessage(vm.liveMessages[vm.currentMessage-1])
           removeItemFromArr(vm.liveMessages,vm.liveMessages[vm.currentMessage-1]);
           vm.totalMessages = vm.totalMessages - 1;
           }
//            console.log("DESPUES")
//           console.log("MENSAJE CURRENT"  +  (vm.currentMessage));
//           console.log("TOTAL MESAGES" +  vm.totalMessages);
        },700)
         finishLoop();
        }

        function finishLoop(){
        if(vm.totalMessages < 0){
         vm.totalMessages = 0;
         vm.currentMessage = 0;
         vm.liveMessages = [];
         }
        }
        var removeItemFromArr = ( arr, item ) => {
            return arr.filter( e => e !== item );
        };
        function loadMessage(message){
          vm.liveMessages.push(message);
          vm.totalMessages = vm.totalMessages + 1;
        }
        function loadPage(page) {
            vm.page = page;
            console.log("page", page);
        }
    }
})();
