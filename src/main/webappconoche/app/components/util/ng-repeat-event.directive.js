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
                $timeout(function() { scope.$broadcast('masonry.reload'); }, 400);
                isConsumed = true;
            }
        };
    }]);
