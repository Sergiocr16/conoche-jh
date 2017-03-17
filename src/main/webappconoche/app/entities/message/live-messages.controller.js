(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('LiveMessagesController', LiveMessagesController);

    LiveMessagesController.$inject = ['ParseLinks','Message', 'AlertService', 'paginationConstants', '$stateParams', 'WSRealTimeEventMessages', '$timeout','User','$rootScope'];

    function LiveMessagesController(ParseLinks, Message, AlertService, paginationConstants, $stateParams, WSRealTimeEventMessages, $timeout, User, $rootScope) {

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
                console.log(vm.isShowing);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

         WSRealTimeEventMessages.receive(vm.idEvent).then(null, null, addNewMessage);

         function addNewMessage(message) {
         console.log("ENTRE");
          console.log("ESTADO MOS CUANDO ENTRA MENSAJE "+vm.isShowing);

         function isMessageAlreadyIn(element) {
          return element.id == message.id;
         }
         if(vm.isShowing == 1){
          loadMessage(message);
          }else{
          showMessage(message);
         }
         }

        function showMessage(message){
         vm.isShowing = 1;
          vm.message = message;
          console.log($rootScope.currentLanguage)
          if($rootScope.currentLanguage == 'es'){
          moment.locale('es');
          }else{
          moment.locale('en');
          }
          vm.message.fromNow = moment(message.creationTime).fromNow();
          $('#no-message').fadeOut(0);
          $('#message').fadeIn(700);
          $timeout(function(){
          $('#message').fadeOut(700);
           vm.isShowing = 0;
           loopMessages();
           console.log("ESTA MOSTRANDO? "+ vm.isShowing)
          WSRealTimeEventMessages.discardViewedMessage(message);
          console.log("TOTAL MESAGES" +  vm.totalMessages);
          if(vm.totalMessages == 0 && vm.isShowing == 0){
              $timeout(function(){
               $('#no-message').fadeIn(700)
              },700)
           }
          ;
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
         finishLoop();
        }

        function finishLoop(){
        if(vm.totalMessages < 0){
         vm.totalMessages = 0;
         vm.currentMessage = 0;
         vm.liveMessages = [];
         vm.isShowing = 0;
         }

        }
        var removeItemFromArr = ( arr, item ) => {
            return arr.filter( e => e !== item );
        };
        function loadMessage(message){
        function isMessageAlreadyIn(element) {
          return element.id == message.id;
        }
         if(vm.liveMessages.find(isMessageAlreadyIn)==undefined){
          vm.liveMessages.push(message);
           vm.totalMessages = vm.totalMessages + 1;
          }
          console.log("TOTAL MESAGES" +  vm.totalMessages);
        }


        function loadPage(page) {
            vm.page = page;
            console.log("page", page);
        }
    }
})();
