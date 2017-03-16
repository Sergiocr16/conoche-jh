/**
 * Created by melvin on 2/4/2017.
 */
(function () {
    'use strict';
    angular.module('conocheApp')
        .filter('shuffle', shuffle);

    function shuffle() {
        return function (a) {
            for (let i = a.length; i; i--) {
                let j = Math.floor(Math.random() * i);
                [ a[i - 1], a[j]] = [a[j], a[i - 1]];
            }
            return a;
        };
    }

})();
