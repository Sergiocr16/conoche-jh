(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('EventAngController', EventAngController);


    EventAngController.$inject = ['Promotion','$rootScope','$state', 'DataUtils', 'Event', 'ParseLinks', 'AlertService', 'paginationConstants', 'optionalParams'];

    function EventAngController(Promotion, $rootScope, $state, DataUtils, Event, ParseLinks, AlertService, paginationConstants, optionalParams) {



        var vm = this;
        vm.loadPage = loadPage;
        vm.predicate = optionalParams.predicate;
        vm.reverse = optionalParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.promotions = [];
        loadAll();

        function loadAll () {
            Event.search({
                history: optionalParams.history,
                provincia: optionalParams.provincia,
                search: optionalParams.search,
                page: optionalParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.events = data;
//
//                vm.page = pagingParams.page;
                loadPromotions();

                vm.page = optionalParams.page;
                setTimeout(function() {
                    $("#tableData").fadeIn(700);
                }, 200);

            }



            function onError(error) {
                AlertService.error(error.data.message);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function loadPromotions(){
                Promotion.query({}, onSuccessPromotions, onError);
            }

            function onSuccessPromotions(data, headers) {
                    angular.forEach(data, function(promotion ,key) {
                         if(moment(new Date()).isBefore(promotion.finalTime)){
                          vm.promotions.push(promotion);
                         }
                    });
                setTimeout(function() {
                    $("#tableData").fadeIn(700);
                }, 200);
            }


        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                history: optionalParams.history,
                provincia: optionalParams.provincia,
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: optionalParams.search
            });
        }
    }
})();
