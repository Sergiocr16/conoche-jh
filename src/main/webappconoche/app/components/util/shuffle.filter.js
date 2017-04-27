'use strict';

/**
 * Created by melvin on 2/4/2017.
 */
(function () {
    'use strict';
    angular.module('conocheApp').filter('shuffle', shuffle);

    function shuffle() {
        return function (a) {
            for (var i = a.length; i; i--) {
                var j = Math.floor(Math.random() * i);
                var ref = a[j];
                a[j] = a[i - 1];
                a[i - 1] = ref;
            }
            return a;
        };
    }
})();
