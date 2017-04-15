/**
 * Created by melvin on 4/14/2017.
 */

(function () {
    'use strict';
    angular.module('conocheApp')
        .filter('cloudinaryURL', CloudinaryURL);

    CloudinaryURL.$inject = ['cloudinary'];
    function CloudinaryURL(cloudinary) {
        return CloudinaryURLFilter;

        function CloudinaryURLFilter(public_id) {
            if(!public_id) { return public_id; }
            return cloudinary.config().image_url
             + cloudinary.config().cloud_name
             + '/image/upload/'
             + public_id;
        }
    }

})();
