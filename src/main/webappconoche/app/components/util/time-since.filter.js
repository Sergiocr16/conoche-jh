/**
 * Created by melvin on 4/22/2017.
 */
/**
 * Created by melvin on 2/5/2017.
 */
/**
 * Created by melvin on 1/29/2017.
 */

(function () {
    'use strict';
    angular.module('conocheApp')
        .filter('timeSince', TimeSince);

    function TimeSince() {
        return TimeSinceFilter;

        function TimeSinceFilter(dateString) {
            var date = Date.parse(dateString);
            var seconds = Math.floor((new Date() - date) / 1000);

            var interval = Math.floor(seconds / 31536000);

            if (interval > 1) {
                return interval + " years";
            }
            interval = Math.floor(seconds / 2592000);
            if (interval > 1) {
                return interval + " months";
            }
            interval = Math.floor(seconds / 86400);
            if (interval > 1) {
                return interval + " days";
            }
            interval = Math.floor(seconds / 3600);
            if (interval > 1) {
                return interval + " hours";
            }
            interval = Math.floor(seconds / 60);
            if (interval > 1) {
                return interval + " minutes";
            }
            return Math.floor(seconds) + " seconds";
        }
    }
})();
