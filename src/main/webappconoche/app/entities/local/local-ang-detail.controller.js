(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('LocalAngDetailController', LocalAngDetailController);

    LocalAngDetailController.$inject = ['$state','$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Local', 'ImagenLocal', 'Event', 'Schedule', 'Servicio', 'User', 'RatingLocal','Principal'];

    function LocalAngDetailController($state,$scope, $rootScope, $stateParams, previousState, DataUtils, entity, Local, ImagenLocal, Event, Schedule, Servicio, User, RatingLocal,Principal) {
        var vm = this;

        vm.local = entity;
        $rootScope.pageTitle = vm.local.name;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.isAuthenticated = Principal.isAuthenticated;

        var unsubscribe = $rootScope.$on('conocheApp:localUpdate', function(event, result) {
            vm.local = result;
        });

        $rootScope.currentLocal = vm.local;
        $rootScope.currentLocal.stars = populateStars(vm.local.rating);
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


        $scope.$on('$destroy', unsubscribe);
    }
})();
