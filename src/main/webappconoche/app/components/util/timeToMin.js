/**
 * Created by melvin on 2/5/2017.
 */
/**
 * Created by melvin on 1/29/2017.
 */

(function () {
    'use strict';
    angular.module('conocheApp')
        .filter('timeToMin',timeToMin);

    function timeToMin() {
        return function(t) {
            var v = t.split(':');
            if (v.length < 2) {
                throw "invalid format error";
            }
            return parseInt(v[0], 10) * 60
                + parseInt(v[1], 10);
        };
    }

})();


