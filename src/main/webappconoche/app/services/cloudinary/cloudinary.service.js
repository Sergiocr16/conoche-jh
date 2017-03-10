/**
 * Created by melvin on 3/10/2017.
 */
(function() {
    'use strict';

    angular
        .module('conocheApp')
        .factory('CloudinaryService', CloudinaryService);

    CloudinaryService.$inject = ['Upload', 'cloudinary'];

    function CloudinaryService(Upload, cloudinary) {

        var services = {
            uploadFile: uploadFile
        }
        return services;

        function uploadFile(file, tags) {
            if (!file || file.$error ) {
                return;
            }
            tags = tags || [];

            return Upload.upload({
                url: cloudinary.config().base_url
                     + cloudinary.config().cloud_name
                     + "/upload",

                data: {
                    upload_preset: cloudinary.config().upload_preset,
                    tags: tags,
                    file: file,
                }
            });
        }
    }
})();
