/**
 * Created by melvin on 3/15/2017.
 */
angular.module('conocheApp')
    .directive("particleText", ParticleText);

ParticleText.$inject = ['Particle', 'ParticleAnimator', '$document'];

function ParticleText(Particle, ParticleAnimator, $document) {
    return {
        restrict: 'E',
        template: '<canvas/>',
        link: function(scope, element, attrs) {

            let canvas       = element.find('canvas')[0];
            let context      = canvas.getContext('2d');
            let r            = parseInt(attrs.radius, 10) || 20;
            let radius       =  r * r;
            let mouse        =  {x: canvas.width, y: canvas.height};
            let fontFamilly  =  attrs.fontFamilly || 'Arial';
            let fontSize     = parseInt(attrs.fontSize, 10) || 30;
            let padding      = parseInt(attrs.heightPadding, 10) || 3;
            let color        = attrs.color || 'black';
            let text         = attrs.text;
            canvas.height    = fontSize + padding;
            context.globalAlpha = 0.7;
            addEvents();

            let builder = Particle.builder()
                .setFriction(parseFloat(attrs.friction))
                .setEase(parseFloat(attrs.ease))
                .setSize(parseInt(attrs.size, 10))
                .setMotionColor(attrs.motionColor);

            let pia = ParticleAnimator.create(builder, context)
                .setSpacing(parseInt(attrs.spacing, 10))
                .setColor(color);


            function paintText(context) {
                let font     = `${fontSize}pt ${fontFamilly}`;
                context.font = font;
                let textSize = context.measureText(text);
                let height   = context.canvas.height;
                let width    = context.canvas.width = textSize.width;

                context.clearRect(0, 0, width, height);
                context.font = font;
                context.fillText(text,  0, (height / 2) + (fontSize/2));
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
                setMouse(event.changedTouches[0].clientX,
                    event.changedTouches[0].clientY);
            }

            function onTouchMove(event) {
                event.preventDefault();
                setMouse(event.targetTouches[0].clientX,
                    event.targetTouches[0].clientY);
            }
            function onTouchend(event){
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
