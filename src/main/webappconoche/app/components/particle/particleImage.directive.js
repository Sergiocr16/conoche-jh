/**
 * Created by melvin on 2/4/2017.
 */

    //cambiar a una version mas verstatil con data binding
angular.module('conocheApp')
       .directive("particleImage", ParticleImage);

ParticleImage.$inject = ['$window', '$document', 'Particle', 'ImageUtil', 'ParticleAnimator', '$timeout', 'MousePosition'];

function ParticleImage($window, $document, Particle, ImageUtil, ParticleAnimator, $timeout, MousePosition) {
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
            let mouse     =  MousePosition.createMouseEvents(canvas);
            let timeout   = parseInt(attrs.timeout, 10) || 5000;
            canvas.width  = parseInt(attrs.width, 10) || canvas.width;
            canvas.height = parseInt(attrs.height, 10) || canvas.height;

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

            ImageUtil.preloadImages(scope.src)
                .then(slideShow);
        }
    };
}
