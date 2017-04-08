(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RatingLocalAngController', RatingLocalAngController);

    RatingLocalAngController.$inject = ['$state', 'RatingLocal', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','$stateParams'];

    function RatingLocalAngController($state, RatingLocal, ParseLinks, AlertService, paginationConstants, pagingParams,$stateParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        loadAll();
        function defineLabelColor(rating){
        switch((rating)){
            case 1:
            return "label-danger";
            break;
            case 2:
            return "label-warning";
            break;
            case 3:
            return "label-primary";
            break;
            case 4:
            return "label-almost-good";
            break;
            case 5:
            return "label-success";
            break;
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
            RatingLocal.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                localId: $stateParams.id
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
                vm.ratingLocals = data;
                vm.page = pagingParams.page;
                angular.forEach(data,function(value,key){
                value.stars = populateStars(value.rating);
                value.qualityText = "conocheApp.ratingLocal.califications."+(value.rating);
                value.labelColor = defineLabelColor(value.rating);
                value.creationDate = moment(value.creationDate).fromNow();
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
    }
})();
