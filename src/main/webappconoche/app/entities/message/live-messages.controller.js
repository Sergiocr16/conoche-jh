(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('LiveMessagesController', LiveMessagesController);

    LiveMessagesController.$inject = ['ParseLinks', 'AlertService', 'paginationConstants', '$stateParams', 'WSRealTimeEventMessages', '$timeout','User'];

    function LiveMessagesController(ParseLinks, AlertService, paginationConstants, $stateParams, WSRealTimeEventMessages, $timeout, User) {

        var vm = this;
        vm.idEvent = $stateParams.idEvent;
        vm.liveMessages = [];
        console.log(vm.idEvent);
        vm.loadPage = loadPage;
        vm.isShowing = 0;
        vm.totalMessages = 0;
        vm.currentMessage = 0;
        WSRealTimeEventMessages.receive(vm.idEvent)
            .then(null, null, addNewMessage);
        function addNewMessage(message) {

        if(vm.isShowing == 1){
          loadMessage(message);
          vm.totalMessages = vm.totalMessages + 1;
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
                   console.log("MOSTRANDO "+vm.isShowing)
                   console.log("CANTIDAD DE MENSAJES "+vm.totalMessages)
                   console.log("MENSAJE ACTUAL "+vm.currentMessage)
           finishLoop();
           vm.isShowing = 0;
          },10000)
        }
        function loopMessages(){
        $timeout(function(){
           if(vm.totalMessages > 0){
            vm.currentMessage = vm.currentMessage + 1;
           showMessage(vm.liveMessages[vm.currentMessage-1])
           removeItemFromArr(vm.liveMessages,vm.liveMessages[vm.currentMessage-1]);
           vm.totalMessages = vm.totalMessages - 1;
           }
        },700)
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
          console.log("metiendo msj");
        }
        function loadPage(page) {
            vm.page = page;
            console.log("page", page);
        }
    }
})();
