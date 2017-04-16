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
                var interval = null;
                var refreshRate = 200;
                function onImageChange() {
                    var parent      = img.offsetParent();
                    var aspectRatio = img.width() / img.height();
                    if(img.width() > img.height()){
                        var height =  Math.min((1 / aspectRatio) * parent.width(), parent.height());
                        img.css({height: height, width: (aspectRatio * height)});
                    }
                    else {
                        var width =  Math.min(aspectRatio * parent.height(), parent.width());
                        img.css({height: width * (1 / aspectRatio), width: width});
                    }
                    rePosition();
                }
                function rePosition() {
                    img.css({top: img.offsetParent().height() / 2 - img.height() / 2});
                }

                function onLoad() {
                    clearInterval(interval);
                    onImageChange();
                }

                function onChange() {
                    interval = setInterval(onImageChange, refreshRate);
                }

                function onDestroy() {
                    $(window).off('resize', onImageChange);
                }

                $(window).resize(onImageChange);
                $animate.on('enter', img, onImageChange);
                element.bind('load', onLoad);
                attrs.$observe('ngSrc', onChange);
                scope.$on('$destroy', onDestroy);
            }
        };
    }
})();
