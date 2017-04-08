(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('LocalAngController', LocalAngController);

    LocalAngController.$inject = ['$state', 'DataUtils', 'Local', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Category'];

    function LocalAngController($state, DataUtils, Local, ParseLinks, AlertService, paginationConstants, pagingParams,Category) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.reloadAll = loadAll;

        loadAll();
        loadCategories();
        function loadCategories(){
            Category.query({}, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.categories = data;


                 vm.links = ParseLinks.parse(headers('link'));
                 vm.totalItems = headers('X-Total-Count');
                 vm.queryCount = vm.totalItems;
                 vm.events = data;
                 vm.page = pagingParams.page;
                 setTimeout(function() {
                    $("#tableData").fadeIn(700);
                 }, 200)

            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


         vm.loadByCategory = function(categoryId) {
                    Local.getByCategory({
                        categoryId: categoryId,
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage,
                        sort: sort()
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
                       vm.locals = data;
                       vm.page = pagingParams.page;
                    }
                    function onError(error) {
                        AlertService.error(error.data.message);
                    }
                }




        function populateStars(rating){

         var stars = [];

         function paintFullStars(quan){
              for(var i = 0;i<quan;i++){
                 stars.push({class:'fa fa-star yellow-star'})
              }
          }

          function paintEmptyStars(quan){
            for(var i = 0;i<quan;i++){
               stars.push({class:'fa fa-star-o yellow-star'})
            }
        }
            var fullStars = Math.floor(rating);
            var rest = fullStars - rating;
            var noStar = 5 -fullStars;
            if(rest == 0){
              paintFullStars(fullStars);
              paintEmptyStars(noStar);
              return stars;
            }else{
             paintFullStars(fullStars);
             if(rest<=0){
             stars.push({class:'fa fa-star-half-o yellow-star'})
             paintEmptyStars((noStar-1));
             }else{
              paintEmptyStars(noStar);
             }
            }
            return stars;
        }


        function loadAll () {
            Local.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
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
                vm.locals = data;
                vm.page = pagingParams.page;
                angular.forEach(data,function(local,key){
                 local.stars = populateStars(local.rating);
                })
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }



        vm.paintStarsRating = function(rating){




        }
    }
})();
