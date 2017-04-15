/**
 * Created by melvin on 4/12/2017.
 */
(function() {
    'use strict';

    angular.module('conocheApp')
        .directive("slider", Slider);
    function  Slider () {
        return {
            restrict: 'E',
            scope: {
                image: '@',
                nextFn: '&',
                prevFn: '&',
                closeFn: '&'
            },
            link: function (scope, element, attrs) {
                var idleTime = attrs.idleTime || 4000;
                var timeoutid = 0;
                $(element).mousemove(function() {
                    changeShowNav(true);
                    clearTimeout(timeoutid);
                    timeoutid = setTimeout(myFunctionToHideMenu, idleTime);
                });

                function myFunctionToHideMenu() {
                    changeShowNav(false);
                }

                function changeShowNav(show) {
                    scope.$apply(function() {
                        scope.vm.showOptions = show;
                    })
                }
            },
            controllerAs: "vm",
            controller: ['$scope', function MyTabsController($scope) {
                var vm = this;

                var nextFn = $scope.nextFn();
                var prevFn = $scope.prevFn();

                vm.close       = $scope.closeFn();
                vm.showOptions =  true;
                vm.isNext      = true;
                vm.next        = nextFn && next;
                vm.prev        = prevFn && prev;

                vm.keyEvent         = keyEvent;
                vm.toggleFullScreen = toggleFullScreen;

                function toggleFullScreen() {
                    vm.fullScreen = !vm.fullScreen;
                }

                function next() {
                    if(!nextFn) { return; }
                    vm.isNext = true;
                    nextFn();
                }

                function prev() {
                    if(!prevFn) { return; }
                    vm.isNext = false;
                    prevFn();
                }

                function keyEvent(e) {
                    switch(e.which) {
                        //right arrow
                        case 39:
                            next();
                            break;
                         //left arrow
                        case 37:
                            prev();
                            break;
                        default:
                    }
                }
            }],
            templateUrl: 'app/components/slider/slider.html'
        };
    }
})();
