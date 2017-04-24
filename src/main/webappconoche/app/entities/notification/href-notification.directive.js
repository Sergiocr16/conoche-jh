/**
 * Created by melvin on 4/23/2017.
 */
/**
 * Created by melvin on 4/12/2017.
 */
(function() {
    'use strict';

    angular.module('conocheApp')
        .directive("hrefNotification", hrefNotification);
    function  hrefNotification () {
        return {
            restrict: 'A',
            scope: {
                object: '=',
            },
            link: function (scope, element, attrs) {

                $(element[0]).on( "click", function() {
                   console.log('aqui');
                    if(scope.goToState) {
                        console.log('aqui2');
                       scope.goToState();
                   }
                });
            },

            controller: ['$scope', '$state', function MyTabsController($scope, $state) {


                $scope.goToState = goToState;

                function goToState() {

                    if(!$scope.object
                        || !$scope.object.objectId
                        || !$scope.object.objectType) {
                        return;
                    }

                    var id = $scope.object.objectId;
                    switch ($scope.object.objectType) {
                        case "EVENT":
                            $state.go('event-ang-detail', { id: id });
                            break;
                        case "LOCAL":
                            throw 'NotImplementedError';
                            break;
                        case "PROMOTION":
                            throw 'NotImplementedError';
                            break;
                        case "USER":
                            throw 'NotImplementedError';
                            break;
                        case "REALTIME_EVENT_IMAGE":
                            throw 'NotImplementedError';
                            break;
                        case "RATING":
                            throw 'NotImplementedError';
                            break;
                        default:
                            throw 'NotSupportedError';
                    }
                }

            }]
        };
    }
})();
