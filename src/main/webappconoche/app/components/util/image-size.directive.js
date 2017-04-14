/**
 * Created by melvin on 4/13/2017.
 */
(function() {
    'use strict';


    angular.module('conocheApp')
        .directive("imageSize", ImageSize);

    ImageSize.$inject = ['$animate'];

    function ImageSize($animate) {
        return {
            restrict: 'A',
            scope: {},
            link: function (scope, element, attrs) {
                var img = $(element[0]);

                function modify_image(image_src) {
                    img.on('load', rePosition)
                        .attr("src", image_src)
                        .each(function () {
                            if (this.complete) $(this).trigger('load');
                        });
                }

                function rePosition() {
                    img.css({top: $(window).height() / 2 - img.height() / 2});
                }

                $(window).on('resize', rePosition);
                attrs.$observe('watchSrc', modify_image);
                $animate.on('enter', img, rePosition);
            }
        };
    }
})();
