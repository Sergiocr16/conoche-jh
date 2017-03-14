(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('LiveMessagesController', LiveMessagesController);

    LiveMessagesController.$inject = ['ParseLinks', 'AlertService', 'paginationConstants', '$stateParams', 'WSRealTimeEventMessages', '$timeout'];

    function LiveMessagesController(ParseLinks, AlertService, paginationConstants, $stateParams, WSRealTimeEventMessages, $timeout) {

        var vm = this;
        vm.idEvent = $stateParams.idEvent;
        vm.liveMessages = [];
        console.log(vm.idEvent);
        vm.loadPage = loadPage;

//        vm.reset = reset;

//        loadAll();
        WSRealTimeEventMessages.receive(vm.idEvent)
            .then(null, null, addNewMessage);
        function addNewMessage(message) {

            vm.liveMessages.push(message);
        }

//        function loadAll () {
//            vm.infiniteScrollDisable = true;
//            RealTimeEventImage.eventRealTimeImages({
//                idEvent:  vm.idEvent,
//                page: vm.page,
//                size: ITEMS_PER_PAGE,
//                sort: SORT
//            }, onSuccess, onError);
//
//
//            function onSuccess(data, headers) {
//                vm.links = ParseLinks.parse(headers('link'));
//                console.log(JSON.stringify(vm.links));
//                vm.totalItems = headers('X-Total-Count');
//                for (var i = 0; i < data.length; i++) {
//                    vm.realTimeEventImages.push(data[i]);
//                }
//                timeoutDisable()
//            }

//            function onError(error) {
//                AlertService.error(error.data.message);
//                timeoutDisable()
//            }
//        }

        //solucion sucia pero no encontre
        // ningun evento para cuando se carga masonry en la directiva.
//        function timeoutDisable() {
//            $timeout(function() {vm.infiniteScrollDisable = false;} , 500);
//        }

//        function reset () {
//            vm.page = 0;
//            vm.realTimeEventImages = [];
//            loadAll();
//        }
//
        function loadPage(page) {
            vm.page = page;
            console.log("page", page);
        }
    }
})();
