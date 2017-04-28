/**
 * Created by melvin on 4/13/2017.
 */
(function() {
    'use strict';


    angular.module('conocheApp')
        .directive("imageSize", ImageSize);

    ImageSize.$inject = ['$animate', '$interval'];

    function ImageSize($animate, $interval) {
        return {
            restrict: 'A',
            scope: {},
            link: function (scope, element, attrs) {

                var img         = $(element[0]);
                var parent      = img.offsetParent();
                var refreshRate = 30;

                function positionImage(imgWidth, imgHeight) {
                    img.css(biggestDimentionPosible(imgWidth, imgHeight));
                    img.css({marginTop: parent.height() / 2 - img.height() / 2});
                }

                function biggestDimentionPosible(imgWidth, imgHeight) {
                    var aspectRatio = imgWidth / imgHeight;
                    if(imgWidth > imgHeight){
                        var height =  Math.min((1 / aspectRatio) * parent.width(), parent.height());
                        return { height: height, width: (aspectRatio * height) };
                    }
                    var width =  Math.min(aspectRatio * parent.height(), parent.width());
                    return { height: width * (1 / aspectRatio), width: width };
                }

                function onSrcChange() {
                    getImageSize(positionImage);
                }

                function onImageChange() {
                    positionImage(img.width(), img.height());
                }

                function onDestroy() {
                    $(window).off('resize', onImageChange);
                }

                function getImageSize(callback) {
                    var wait = $interval(function() {
                        var w = img[0].naturalWidth,
                            h = img[0].naturalHeight;
                        if (w && h) {
                            clearInterval(wait);
                            callback(w, h);
                        }
                    }, refreshRate);
                }
                $(window).resize(onImageChange);
                $animate.on('enter', img, onImageChange);
                attrs.$observe('ngSrc', onSrcChange);
                scope.$on('$destroy', onDestroy);
            }
        };
    }
})();
