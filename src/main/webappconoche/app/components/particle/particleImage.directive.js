/**
 * Created by melvin on 2/4/2017.
 */

    //cambiar a una version mas verstatil con data binding
angular.module('conocheApp')
       .directive("particleImage", ParticleImage);

ParticleImage.$inject = ['$window', '$document', 'Particle', 'ImageUtil', 'ParticleAnimator', '$timeout'];

function ParticleImage($window, $document, Particle, ImageUtil, ParticleAnimator, $timeout) {
    return {
        restrict: 'E',
        template: '<canvas/>',
        scope: {
            src: "="
        },
        link: function(scope, element, attrs) {
            let canvas    = element.find('canvas')[0];
            let context   = canvas.getContext('2d');
            let r         = parseInt(attrs.radius, 10) || 20;
            let radius    =  r * r;
            let mouse     = {x: canvas.width, y: canvas.height};;
            let timeout   = parseInt(attrs.timeout, 10) || 5000;
            canvas.width  = parseInt(attrs.width, 10) || canvas.width;
            canvas.height = parseInt(attrs.height, 10) || canvas.height;

            addEvents();
            context.globalAlpha = 0.7;

            if (!angular.isArray(scope.src)
                && scope.src.length !== 0) {
                return;
            }

            let builder = Particle.builder()
                .setFriction(parseFloat(attrs.friction))
                .setEase(parseFloat(attrs.ease))
                .setSize(parseInt(attrs.size, 10))
                .setMotionColor(attrs.motionColor);

            let pia = ParticleAnimator.create(builder, context)
                .setSpacing(parseInt(attrs.spacing, 10));


            function paintImage(image, context) {
                let height = context.canvas.height;
                let width  = context.canvas.width;
                context.clearRect(0, 0, width, height);
                context.drawImage(image, 0, 0, width, height);
            }

            function slideShow(images) {
                if(images.length  === 1) {
                    pia.reset(context => paintImage(images[0], context));
                }
                else {
                    (function p(i) {
                        pia.reset(context => paintImage(images[i], context));
                        $timeout(()=> p((i + 1) % images.length), timeout);
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

            ImageUtil.preloadImages(scope.src)
                .then(slideShow);

            scope.$on('$destroy', onDestroy);
        }
    };
}
