/**
 * Created by melvin on 3/10/2017.
 */

(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(CloudinaryConfig);

    CloudinaryConfig.$inject = ['cloudinaryProvider'];

    function CloudinaryConfig(cloudinaryProvider) {
        cloudinaryProvider
            .set("cloud_name", "dgh2svgxo")
            .set("upload_preset", "ieo8vml9")
            .set("base_url", "https://api.cloudinary.com/v1_1/");
    }
})()
