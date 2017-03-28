/**
 * Created by melvin on 3/11/2017.
 */
/**
 * Created by melvin on 3/11/2017.
 */
/**
 * Created by melvin on 3/4/2017.
 */
(function() {
    'use strict';


    angular
        .module('conocheApp')
        .factory('RealTimeEventImageCloudinary', RealTimeEventImageCloudinary);

    RealTimeEventImageCloudinary.$inject = ['WSRealTimeEventImages', 'CloudinaryService'];

    function RealTimeEventImageCloudinary (WSRealTimeEventImages, CloudinaryService) {
        const REAL_TIME_EVENT_IMAGE_TAG = "RealTimeEventImage";
        var service = {
            save: save
        };
        return service;

        function save (file, image) {
            const TAGS = [REAL_TIME_EVENT_IMAGE_TAG, "Event: " + image.idEvent];
            return CloudinaryService.uploadFile(file, TAGS)
                .then(onSuccess);

            function onSuccess(response) {
                var height        = response.data.height;
                var width         = response.data.width;
                image.imageUrl    = response.data.public_id;
                image.aspectRatio = height / width;

                WSRealTimeEventImages.sendImage(image);
                return image;
            }
        }


    }
})();
