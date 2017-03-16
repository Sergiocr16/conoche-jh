/**
 * Created by melvin on 3/15/2017.
 */
angular.module('conocheApp')
    .directive("particleText", ParticleText);

ParticleText.$inject = ['Particle', 'ParticleAnimator', 'MousePosition'];

function ParticleText(Particle, ParticleAnimator, MousePosition) {
    return {
        restrict: 'E',
        template: '<canvas/>',
        link: function(scope, element, attrs) {

            let canvas       = element.find('canvas')[0];
            let context      = canvas.getContext('2d');
            let r            = parseInt(attrs.radius, 10) || 20;
            let radius       =  r * r;
            let mouse        =  MousePosition.createMouseEvents(canvas);
            let timeout      = parseInt(attrs.timeout, 10) || 5000;
            let fontFamilly  =  attrs.fontFamilly || 'Arial';
            let fontSize     = parseInt(attrs.fontSize, 10) || 30;
            let padding      = parseInt(attrs.heightPadding, 10) || 3;
            let color        = attrs.color || 'black';
            let text         = attrs.text;
            canvas.height    = fontSize + padding;

            context.globalAlpha = 0.7;
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


            attrs.$observe('text', function (interpolatedText) {
                text = interpolatedText;
                pia.reset(paintText);
            });

            pia.reset(paintText);
           pia.start(mouse, radius);
        }
    };
}
