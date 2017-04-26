(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('LiveMessagesController', LiveMessagesController);

    LiveMessagesController.$inject = ['ParseLinks','Message', 'AlertService', 'paginationConstants', '$stateParams', 'WSRealTimeEventMessages','$scope', '$timeout','User','$rootScope','idEvent'];

    function LiveMessagesController(ParseLinks, Message, AlertService, paginationConstants, $stateParams, WSRealTimeEventMessages,$scope, $timeout, User, $rootScope,idEvent) {

        var vm = this;
        vm.idEvent = idEvent;
        vm.liveMessages = [];
        vm.loadPage = loadPage;
        vm.isShowing = 0;
        vm.totalMessages = 0;
        vm.currentMessage = 0;
        loadAll();
        var dataTimeout=[];

        function loadAll () {
            Message.findByEventId({
                eventId: vm.idEvent
            }, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                if(data.length>0){
                    loadMessages(data);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadMessages(data){
            angular.forEach(data, function(message, key) {
                addNewMessage(message);
            });
        }
        WSRealTimeEventMessages.receive(vm.idEvent).then(null, null, addNewMessage);

        function addNewMessage(message) {
            function isMessageAlreadyIn(element) {
                return element.id == message.id;
            }
            if(vm.liveMessages.find(isMessageAlreadyIn)==undefined){
                if(vm.isShowing == 1){
                    loadMessage(message);
                }else{
                    showMessage(message);
                }
            }
        }

        function showMessage(message){
            vm.isShowing = 1;
            vm.message = message;
            defineLanguage();
            getFromNowDate(message);
            hideNoMessagesTitle();
            $('#message').fadeIn(700);
            setTimeout(function(){
                $('#message').fadeOut(700);
                vm.isShowing = 0;
                isMoreComingMessages();
                deleteViewedMessage(message);
                showNoMessagesTitle();
            },7000);
        }

        function showNoMessagesTitle(){
            var newTimer;
            if(vm.totalMessages == 0 && vm.isShowing == 0){
                newTimer =  $timeout(function(){
                    setInitialState();
                    $('#no-message').fadeIn(700);
                },699);

            }
            dataTimeout.push( newTimer );
        }

        function hideNoMessagesTitle(){
            $('#no-message').fadeOut(0);
        }
        function isMoreComingMessages(){
            var newTimer;
            newTimer =  $timeout(function(){
                if(vm.totalMessages > 0){
                    vm.currentMessage = vm.currentMessage + 1;
                    showMessage(vm.liveMessages[vm.currentMessage-1]);
                    removeItemFromArr(vm.liveMessages,vm.liveMessages[vm.currentMessage-1]);
                    vm.totalMessages = vm.totalMessages - 1;
                }else{
                    vm.isShowing = 0;
                }
            },699);
            dataTimeout.push( newTimer );
        }

        function setInitialState(){
            vm.totalMessages = 0;
            vm.currentMessage = 0;
            vm.liveMessages = [];
            vm.isShowing = 0;
            vm.message = null;
        }
        var removeItemFromArr = function( arr, item ) {
            return arr.filter( function(e) { return e !== item; });
        };
        function loadMessage(message){
            vm.liveMessages.push(message);
            vm.totalMessages = vm.totalMessages + 1;
        }

        function loadPage(page) {
            vm.page = page;
            console.log("page", page);
        }

        function getFromNowDate(message){
            vm.message.fromNow = moment(message.creationTime).fromNow();
        }

        function defineLanguage(){
            if($rootScope.currentLanguage == 'es'){
                moment.locale('es');
            }else{
                moment.locale('en');
            }
        }

        function deleteViewedMessage(message){
            WSRealTimeEventMessages.discardViewedMessage(message);
        }

        $scope.$on("$destroy", function () {
            dataTimeout.forEach(function(timeout){
                $timeout.cancel(timeout);
            });
        });
    }
})();
