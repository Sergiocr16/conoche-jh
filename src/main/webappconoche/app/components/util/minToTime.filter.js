/**
 * Created by melvin on 2/5/2017.
 */
/**
 * Created by melvin on 1/29/2017.
 */

(function () {
    'use strict';
    angular.module('conocheApp')
        .filter('minToTime', MinToTime);

    function MinToTime() {
        return MinToTimeFilter;

        function MinToTimeFilter(m) {
            return addZero(Math.floor(m / 60))
                + ":"
                + addZero(m % 60);
            function addZero(m) {
                return (m < 10 ? "0" : "").concat(m);
            }
        }
    }
})();
