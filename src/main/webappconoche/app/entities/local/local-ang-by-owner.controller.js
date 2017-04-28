(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('LocalAngByOwnerController', LocalAngByOwnerController);

    LocalAngByOwnerController.$inject = ['Principal','$state', 'DataUtils', 'Local', 'ParseLinks', 'AlertService', 'paginationConstants', 'optionalParams','Category'];

    function LocalAngByOwnerController(Principal, $state, DataUtils, Local, ParseLinks, AlertService, paginationConstants, optionalParams, Category) {
        var SORT = 'rating,desc';
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = optionalParams.predicate;
        vm.reverse = optionalParams.ascending;

        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.predicate = optionalParams.predicate;
        vm.reverse = optionalParams.ascending;
        vm.loadAll = loadAll;

        loadAll();
        function loadAll () {
            Principal.identity().then(function(user){
                Local.getByOwner({
                    ownerId: user.id,
                    search: optionalParams.search,
                    page: optionalParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: SORT
                },onSuccess, onError);
            });
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data) {
                vm.queryCount = vm.totalItems;
                console.log(data);
                vm.locals = data;
                vm.page = optionalParams.page;

            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;

        }

    }
})();
