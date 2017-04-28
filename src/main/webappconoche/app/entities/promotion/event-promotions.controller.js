(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('EventPromotionsController', EventPromotionsController);

    EventPromotionsController.$inject = ['$scope', '$rootScope', 'event','Promotion'];
    function EventPromotionsController($scope, $rootScope, event,Promotion) {
        var vm = this;
        vm.event = event;
               vm.promotions = [];
        var unsubscribe = $rootScope.$on('conocheApp:eventUpdate', function(e, result) {
            vm.event = result;
        });
        getPromotions();
        function getPromotions(){

        Promotion.geyByEvent({ eventId: vm.event.id}).$promise.then(onSuccess, onError);

        }
        function onSuccess(data, headers) {

          angular.forEach(data, function(promotion ,key) {
                     if(moment(new Date()).isBefore(promotion.finalTime)){
                      vm.promotions.push(promotion);
                     }
                });
        }
       function onError() {

        }
        $scope.$on('$destroy', unsubscribe);
    }
})();
