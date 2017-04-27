
(function() {
    'use strict';

    /**
     * Created by melvin on 3/15/2017.
     */
    angular.module('conocheApp').directive("particleText", ParticleText);

    ParticleText.$inject = ['Particle', 'ParticleAnimator', '$document'];

    function ParticleText(Particle, ParticleAnimator, $document) {
        return {
            restrict: 'E',
            template: '<canvas/>',
            link: function link(scope, element, attrs) {

                var canvas = element.find('canvas')[0];
                var context = canvas.getContext('2d');
                var r = parseInt(attrs.radius, 10) || 20;
                var radius = r * r;
                var mouse = {x: canvas.width, y: canvas.height};
                var fontFamilly = attrs.fontFamilly || 'Arial';
                var fontSize = parseInt(attrs.fontSize, 10) || 30;
                var padding = parseInt(attrs.heightPadding, 10) || 3;
                var color = attrs.color || 'black';
                var text = attrs.text;
                canvas.height = fontSize + padding;
                context.globalAlpha = 0.7;
                addEvents();

                var builder = Particle.builder()
                    .setFriction(parseFloat(attrs.friction))
                    .setEase(parseFloat(attrs.ease))
                    .setSize(parseInt(attrs.size, 10))
                    .setMotionColor(attrs.motionColor);

                var pia = ParticleAnimator.create(builder, context)
                    .setSpacing(parseInt(attrs.spacing, 10))
                    .setColor(color);

                function paintText(context) {
                    var font = fontSize + 'pt ' + fontFamilly;
                    context.font = font;
                    var textSize = context.measureText(text);
                    var height = context.canvas.height;
                    var width = context.canvas.width = textSize.width;

                    context.clearRect(0, 0, width, height);
                    context.font = font;
                    context.fillText(text, 0, height / 2 + fontSize / 2);
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

                attrs.$observe('text', function (interpolatedText) {
                    text = interpolatedText;
                    pia.reset(paintText);
                });
                pia.reset(paintText);
                pia.start(mouse, radius);
                scope.$on('$destroy', onDestroy);
            }
        };
    }
})();
