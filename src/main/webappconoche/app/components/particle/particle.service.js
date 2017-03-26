/**
 * Created by melvin on 2/4/2017.
 */
/**
 * Created by melvin on 2/4/2017.
 */
(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('Particle', ParticleFactory);


    function ParticleFactory () {


        class Particle {
            constructor(x, y, originX, originY, color, atributes) {
                this.originalColor = this.color = color;
                this.originX       = originX;
                this.originY       = originY;
                this.atributes     = atributes;

                this.x  = x;
                this.y  = y;
                this.vx = 0;
                this.vy = 0;
            }

            inOrigin() {
                return Math.abs(this.originY - this.y) < 1
                    && Math.abs(this.originX - this.x) < 1;
            }

            update({x: mx, y: my}, r) {
                let rx = mx - this.x;
                let ry = my - this.y;
                let distance = rx * rx + ry * ry;

                if (distance < r) {
                    let force = -r / distance;
                    let angle = Math.atan2(ry, rx);
                    this.vx += force * Math.cos(angle);
                    this.vy += force * Math.sin(angle);
                }
                this.x += (this.vx *= this.atributes.friction)
                       + (this.originX - this.x)
                       * this.atributes.ease;

                this.y += (this.vy *= this.atributes.friction)
                       + (this.originY - this.y)
                       * this.atributes.ease;

                this.color = this.inOrigin()
                    ? this.originalColor
                    : this.atributes.motionColor;

                this.color = this.color
                    || this.originalColor;
            }

            reset(x, y, color) {
                this.originalColor = color;
                this.originX = x;
                this.originY = y;
            }

            draw(context) {
                context.fillStyle = this.color;
                context.fillRect(this.x, this.y,
                    this.atributes.size,
                    this.atributes.size);
            }
        }
        class ParticleBuilder {

            constructor() {
                this.x = this.originX = 0;
                this.y = this.originY = 0 ;
                this.color = '#000000';

                this.commonAttr = {
                    friction:  0.95,
                    ease: 0.1,
                    size:  3,
                    motionColor: undefined,
                };
            }

            build() {
                return new Particle(
                    this.x, this.y,
                    this.originX,
                    this.originY,
                    this.color,
                    this.commonAttr);
            }

            setColor(color) {
                this.color = color;
                return this;
            }

            setOriginX(x) {
                this.originX = x;
                return this;
            }
            setOriginY(y) {
                this.originY = y;
                return this;
            }

            setX(x) {
                this.x = x;
                return this;
            }

            setY(y) {
                this.y = y;
                return this;
            }

            setLocation (x, y) {
                return this
                    .setX(x)
                    .setY(y)
                    .setOriginX(x)
                    .setOriginY(y);
            }


            setMotionColor (color) {
                this.commonAttr.motionColor = color
                    ||  this.commonAttr.motionColor;
                return this;
            }

            setEase(ease) {
                this.commonAttr.ease = ease
                    ||  this.commonAttr.ease;
                return this;
            }

            setFriction(friction) {
                this.commonAttr.friction = friction
                    || this.commonAttr.friction;
                return this;
            }

            setSize(size) {
                this.commonAttr.size = size
                    || this.commonAttr.size;
                return this;
            }
        }


        class ParticleObserver {

            constructor(builder) {
                this.particles = [];
                this.builder = builder;
            }

            update(mouse, r) {
                for(let p of  this.particles) {
                    p.update(mouse, r);
                }
            }

            render(context) {
                let canvas = context.canvas;
                context.clearRect(0, 0, canvas.width,  canvas.height);
                for(let p of  this.particles) {
                    p.draw(context);
                }
            }

            reset(index, x, y, color) {
                if(index < this.particles.length) {
                    this.particles[index].reset(x, y, color);
                }
                else {
                    let p = this.builder
                        .setOriginX(x)
                        .setOriginY(y)
                        .setColor(color)
                        .build();
                    this.particles.push(p);
                }
            }

            resize(len) {
                this.particles.length = len;
            }

        }

        return {
            builder : () => new ParticleBuilder(),
            observer : builder => new ParticleObserver(builder)
        };
    }
})();
