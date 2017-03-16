/**
 * Created by melvin on 2/6/2017.
 */

(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('ParticleAnimator', ParticleImageAnimatorService);

    ParticleImageAnimatorService.$inject = ['Particle', '$window', 'ImageUtil', 'shuffleFilter'];

    function ParticleImageAnimatorService (Particle, $window, ImageUtil, shuffleFilter) {

        class ParticleImage {
            constructor(builder, context) {
                this.observer  = Particle.observer(builder);
                this.context   = context;
                this.spacing   = 3;
                this.animating = false;
                this.color = null;
            }
            setSpacing(spacing){
                this.spacing = spacing
                    || this.spacing;
                return this;
            }

            setColor(color){
                this.color = color;
                return this;
            }

            reset(callback) {
                let context = this.context;
                callback(context);

                let height = context.canvas.height;
                let width  = context.canvas.width;
                shuffleFilter(this.observer.particles);
                this.observer.builder
                    .setX(Math.random() * width)
                    .setY(Math.random() * height);

                let gen = ImageUtil
                    .forEachAlphaPixel(context, this.spacing);
                let i = 0;
                for (let [x, y, {R, G, B, A}] of gen) {
                    let color = this.color || `rgba(${R},${G},${B},${A})`;
                    this.observer.reset(i++, x, y, color);
                }
                this.observer.resize(i);
                return this;
            }


            start(mouse, radius) {
                this.animating = true;
                animate(this, mouse, radius);
                return this;
            }

            stop() {
                this.animating = false;
                return this;
            }
        }

        function animate(self, mouse, radius) {
            if(!self.animating) {
                return;
            }
            self.observer.update(mouse, radius);
            self.observer.render(self.context);
            $window.requestAnimationFrame(
                ()=> animate(self, mouse, radius));
        }

        return {
            create : (builder, context) => new ParticleImage(builder, context),
        };
    }
})();
