(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('EventPromotionsController', EventPromotionsController);

    EventPromotionsController.$inject = ['$scope', '$rootScope', 'event','Promotion'];
    function EventPromotionsController($scope, $rootScope, event,Promotion) {
        var vm = this;
        vm.event = event;
        var unsubscribe = $rootScope.$on('conocheApp:eventUpdate', function(e, result) {
            vm.event = result;
        });
            getPromotions();
        function getPromotions(){
        console.log('id del evento: ' + vm.event.id);
                  Promotion.geyByEvent({ eventId: vm.event.id}).$promise.then(onSuccess, onError);
        }
        function onSuccess(data, headers) {
            vm.promotions = data;

        }
         function onError(error) {
                        AlertService.error(error.data.message);
                    }
        $scope.$on('$destroy', unsubscribe);
    }
})();
