
(function () {

    'use strict';

    /**
     * Created by melvin on 2/4/2017.
     */

    angular.module('conocheApp').directive("particleImage", ParticleImage);

    ParticleImage.$inject = ['$window', '$document', 'Particle', 'ImageUtil', 'ParticleAnimator', '$timeout'];

    function ParticleImage($window, $document, Particle, ImageUtil, ParticleAnimator, $timeout) {
        return {
            restrict: 'E',
            template: '<canvas/>',
            scope: {
                src: "="
            },
            link: function link(scope, element, attrs) {
                var canvas = element.find('canvas')[0];
                var context = canvas.getContext('2d');
                var r = parseInt(attrs.radius, 10) || 20;
                var radius = r * r;
                var mouse = {x: canvas.width, y: canvas.height};
                
                var timeout = parseInt(attrs.timeout, 10) || 5000;
                canvas.width = parseInt(attrs.width, 10) || canvas.width;
                canvas.height = parseInt(attrs.height, 10) || canvas.height;

                addEvents();
                context.globalAlpha = 0.7;

                if (!angular.isArray(scope.src) && scope.src.length !== 0) {
                    return;
                }

                var builder = Particle.builder()
                    .setFriction(parseFloat(attrs.friction))
                    .setEase(parseFloat(attrs.ease))
                    .setSize(parseInt(attrs.size, 10))
                    .setMotionColor(attrs.motionColor);

                var pia = ParticleAnimator.create(builder, context)
                    .setSpacing(parseInt(attrs.spacing, 10));

                function paintImage(image, context) {
                    var height = context.canvas.height;
                    var width = context.canvas.width;
                    context.clearRect(0, 0, width, height);
                    context.drawImage(image, 0, 0, width, height);
                }

                function slideShow(images) {
                    if (images.length === 1) {
                        pia.reset(function (context) {
                            return paintImage(images[0], context);
                        });
                    } else {
                        (function p(i) {
                            pia.reset(function (context) {
                                return paintImage(images[i], context);
                            });
                            $timeout(function () {
                                return p((i + 1) % images.length);
                            }, timeout);
                        })(0);
                    }
                    pia.start(mouse, radius);
                }

                function addEvents() {
                    $document.bind("mousemove", onMouseMove);
                    $document.bind("touchstart", onTouchStart, false);
                    $document.bind("touchmove", onTouchMove, false);
                    $document.bind("touchend", onTouchend, false);
                }

                function onDestroy() {
                    $document.unbind("mousemove", onMouseMove);
                    $document.unbind("touchstart", onTouchStart);
                    $document.unbind("touchmove", onTouchMove);
                    $document.unbind("touchend", onTouchend);
                }

                function setMouse(x, y) {
                    var rect = canvas.getBoundingClientRect();
                    mouse.x = x - rect.left;
                    mouse.y = y - rect.top;
                }

                function onMouseMove(event) {
                    setMouse(event.clientX, event.clientY);
                }

                function onTouchStart(event) {
                    setMouse(event.changedTouches[0].clientX, event.changedTouches[0].clientY);
                }

                function onTouchMove(event) {
                    event.preventDefault();
                    setMouse(event.targetTouches[0].clientX, event.targetTouches[0].clientY);
                }

                function onTouchend(event) {
                    event.preventDefault();
                    setMouse(0, 0);
                }

                ImageUtil.preloadImages(scope.src).then(slideShow);

                scope.$on('$destroy', onDestroy);
            }
        };
    }
})();
