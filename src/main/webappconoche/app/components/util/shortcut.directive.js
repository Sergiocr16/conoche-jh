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
                $(document).on('keydown', function(e){
                    scope.$apply(keyPressed(e));
                });
            }
        };
    }
})();
