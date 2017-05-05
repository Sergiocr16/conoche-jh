/**
 * Created by melvin on 5/4/2017.
 */
angular.module('conocheApp')
    .directive('myPostRepeatDirective', ['$timeout', function($timeout) {
        return function(scope, element, attrs) {
            var isConsumed = false;
            if (scope.$last && !isConsumed){
                // iteration is complete, do whatever post-processing
                // is necessary
                $timeout(reload, 400);
                $timeout(reload, 700);
                $timeout(reload, 1500);
                isConsumed = true;
                function reload() {
                    scope.$broadcast('masonry.reload');
                }
            }
        };
    }]);
