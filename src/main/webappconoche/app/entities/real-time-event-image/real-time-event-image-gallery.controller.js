(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RealTimeEventImageGalleryController', RealTimeEventImageGalleryController);

    RealTimeEventImageGalleryController.$inject = ['RealTimeEventImage', 'ParseLinks', 'AlertService', 'paginationConstants', '$stateParams', 'WSRealTimeEventImages', '$timeout'];

    function RealTimeEventImageGalleryController(RealTimeEventImage, ParseLinks, AlertService, paginationConstants, $stateParams, WSRealTimeEventImages, $timeout) {

        const ITEMS_PER_PAGE = 10;
        const SORT = 'creationTime,desc';

        var vm = this;
        vm.idEvent = $stateParams.idEvent;
        vm.realTimeEventImages = [];
        vm.infiniteScrollDisable = true;
        vm.loadPage = loadPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.reset = reset;

        loadAll();
        WSRealTimeEventImages.receive(vm.idEvent)
            .then(null, null, addNewImage);

        function loadAll () {
            vm.infiniteScrollDisable = true;
            RealTimeEventImage.eventRealTimeImages({
                idEvent:  vm.idEvent,
                page: vm.page,
                size: ITEMS_PER_PAGE,
                sort: SORT
            }, onSuccess, onError);


            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                console.log(JSON.stringify(vm.links));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.realTimeEventImages.push(data[i]);
                }
                timeoutDisable()
            }

            function onError(error) {
                AlertService.error(error.data.message);
                timeoutDisable()
            }
        }

        //solucion sucia pero no encontre
        // ningun evento para cuando se carga masonry en la directiva.
        function timeoutDisable() {
            $timeout(function() {vm.infiniteScrollDisable = false;} , 500);
        }

        function addNewImage(image) {
            if(vm.realTimeEventImages.length
                % ITEMS_PER_PAGE === 0) {
                vm.realTimeEventImages.pop();
            }
            //tengo que updateriear la ultima pagina
            vm.realTimeEventImages.unshift(image);
        }

        function reset () {
            vm.page = 0;
            vm.realTimeEventImages = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            console.log("page", page);
            loadAll();
        }
    }
})();
