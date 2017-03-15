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
        var service = {
            save: save
        };
        return service;

        function save (file, image) {
            return CloudinaryService.uploadFile(file)
                .then(onSuccess);

            function onSuccess(response) {
                image.imageUrl = response.data.public_id;
                image.width    = response.data.width;
                image.height   = response.data.height;
                WSRealTimeEventImages.sendImage(image);
                return image;
            }
        }


    }
})();
