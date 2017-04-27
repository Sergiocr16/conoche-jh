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
//        loadCategories();

//
//        function loadCategories(){
//            Category.query({}, onSuccess, onError);
//            function onSuccess(data, headers) {
//                vm.categories = data;
//                 setTimeout(function() {
//                    $("#tableData").fadeIn(700);
//                 }, 200)
//
//            }
//            function onError(error) {
//                AlertService.error(error.data.message);
//            }
//        }



//
//        function populateStars(rating){
//
//         var stars = [];
//
//         function paintFullStars(quan){
//              for(var i = 0;i<quan;i++){
//                 stars.push({class:'fa fa-star yellow-star'})
//              }
//          }
//
//          function paintEmptyStars(quan){
//            for(var i = 0;i<quan;i++){
//               stars.push({class:'fa fa-star-o yellow-star'})
//            }
//        }
//            var fullStars = Math.floor(rating);
//            var rest = fullStars - rating;
//            var noStar = 5 -fullStars;
//            if(rest == 0){
//              paintFullStars(fullStars);
//              paintEmptyStars(noStar);
//              return stars;
//            }else{
//             paintFullStars(fullStars);
//             if(rest<=0){
//             stars.push({class:'fa fa-star-half-o yellow-star'})
//             paintEmptyStars((noStar-1));
//             }else{
//              paintEmptyStars(noStar);
//             }
//            }
//            return stars;
//        }


        function loadAll () {
            Principal.identity().then(function(user){

                Local.getByOwner({
                    ownerId: 5,
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
       console.log(vm.locals);
                vm.page = optionalParams.page;

//                angular.forEach(data,function(local,key){
//                 local.stars = populateStars(local.rating);
//                })

            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;

        }

//        function transition() {
//            $state.transitionTo($state.$current, {
//                idCategory: optionalParams.idCategory,
//                provincia: optionalParams.provincia,
//                page: vm.page,
//                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
//                search: optionalParams.search
//            });
//        }


//        function transitionCategory() {
//              $state.transitionTo($state.$current, {
//                    idCategory: vm.idCategoria,
//                    provincia: optionalParams.provincia,
//                    sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
//                    search: optionalParams.search
//                });
//
//        vm.paintStarsRating = function(rating){
//
//        }
//        }
    }
})();
