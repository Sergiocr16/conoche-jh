/**
 * Created by melvin on 4/14/2017.
 */
/**
 * Created by melvin on 4/13/2017.
 */
(function() {
    'use strict';


    angular.module('conocheApp')
        .directive("shortcut", Shortcut);



    function Shortcut() {
        return {
            restrict: 'E',
            scope: {
                keyPressed: '&'
            },
            link: function (scope, element, attrs) {
                var keyPressed = scope.keyPressed();
                function handler (e) {
                    scope.$apply(keyPressed(e));
                };

                $(document).on('keydown', handler);

                scope.$on('$destroy', function() {
                    $(document).off('keydown', handler);
                });
            }
        };
    }
})();
