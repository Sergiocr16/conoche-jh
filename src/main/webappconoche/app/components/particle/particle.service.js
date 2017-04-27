

/**
 * Created by melvin on 2/4/2017.
 * Archivo transpilado
 */
(function () {
    'use strict';

    var _createClass = (function () {
        function defineProperties(target, props) {
            for (var i = 0; i < props.length; i++) {
                var descriptor = props[i];
                descriptor.enumerable = descriptor.enumerable
                    || false; descriptor.configurable = true;
                if ("value" in descriptor)
                    descriptor.writable = true;
                Object.defineProperty(target, descriptor.key, descriptor);
            } }
        return function (Constructor, protoProps, staticProps) {
            if (protoProps)
                defineProperties(Constructor.prototype, protoProps);
            if (staticProps)
                defineProperties(Constructor, staticProps);
            return Constructor; };
    })();

    function _classCallCheck(instance, Constructor) {
        if (!(instance instanceof Constructor)) {
            throw new TypeError("Cannot call a class as a function");
        }
    }


    angular.module('conocheApp').factory('Particle', ParticleFactory);

    function ParticleFactory() {
        var Particle = (function () {
            function Particle(x, y, originX, originY, color, atributes) {
                _classCallCheck(this, Particle);

                this.originalColor = this.color = color;
                this.originX = originX;
                this.originY = originY;
                this.atributes = atributes;

                this.x = x;
                this.y = y;
                this.vx = 0;
                this.vy = 0;
            }

            _createClass(Particle, [{
                key: 'inOrigin',
                value: function inOrigin() {
                    return Math.abs(this.originY - this.y) < 1 && Math.abs(this.originX - this.x) < 1;
                }
            }, {
                key: 'update',
                value: function update(_ref, r) {
                    var mx = _ref.x,
                        my = _ref.y;

                    var rx = mx - this.x;
                    var ry = my - this.y;
                    var distance = rx * rx + ry * ry;

                    if (distance < r) {
                        var force = -r / distance;
                        var angle = Math.atan2(ry, rx);
                        this.vx += force * Math.cos(angle);
                        this.vy += force * Math.sin(angle);
                    }
                    this.x += (this.vx *= this.atributes.friction) + (this.originX - this.x) * this.atributes.ease;

                    this.y += (this.vy *= this.atributes.friction) + (this.originY - this.y) * this.atributes.ease;

                    this.color = this.inOrigin() ? this.originalColor : this.atributes.motionColor;

                    this.color = this.color || this.originalColor;
                }
            }, {
                key: 'reset',
                value: function reset(x, y, color) {
                    this.originalColor = color;
                    this.originX = x;
                    this.originY = y;
                }
            }, {
                key: 'draw',
                value: function draw(context) {
                    context.fillStyle = this.color;
                    context.fillRect(this.x, this.y, this.atributes.size, this.atributes.size);
                }
            }]);

            return Particle;
        })();

        var ParticleBuilder = (function () {
            function ParticleBuilder() {
                _classCallCheck(this, ParticleBuilder);

                this.x = this.originX = 0;
                this.y = this.originY = 0;
                this.color = '#000000';

                this.commonAttr = {
                    friction: 0.95,
                    ease: 0.1,
                    size: 3,
                    motionColor: undefined
                };
            }

            _createClass(ParticleBuilder, [{
                key: 'build',
                value: function build() {
                    return new Particle(this.x, this.y, this.originX, this.originY, this.color, this.commonAttr);
                }
            }, {
                key: 'setColor',
                value: function setColor(color) {
                    this.color = color;
                    return this;
                }
            }, {
                key: 'setOriginX',
                value: function setOriginX(x) {
                    this.originX = x;
                    return this;
                }
            }, {
                key: 'setOriginY',
                value: function setOriginY(y) {
                    this.originY = y;
                    return this;
                }
            }, {
                key: 'setX',
                value: function setX(x) {
                    this.x = x;
                    return this;
                }
            }, {
                key: 'setY',
                value: function setY(y) {
                    this.y = y;
                    return this;
                }
            }, {
                key: 'setLocation',
                value: function setLocation(x, y) {
                    return this.setX(x).setY(y).setOriginX(x).setOriginY(y);
                }
            }, {
                key: 'setMotionColor',
                value: function setMotionColor(color) {
                    this.commonAttr.motionColor = color || this.commonAttr.motionColor;
                    return this;
                }
            }, {
                key: 'setEase',
                value: function setEase(ease) {
                    this.commonAttr.ease = ease || this.commonAttr.ease;
                    return this;
                }
            }, {
                key: 'setFriction',
                value: function setFriction(friction) {
                    this.commonAttr.friction = friction || this.commonAttr.friction;
                    return this;
                }
            }, {
                key: 'setSize',
                value: function setSize(size) {
                    this.commonAttr.size = size || this.commonAttr.size;
                    return this;
                }
            }]);

            return ParticleBuilder;
        })();

        var ParticleObserver = (function () {
            function ParticleObserver(builder) {
                _classCallCheck(this, ParticleObserver);

                this.particles = [];
                this.builder = builder;
            }

            _createClass(ParticleObserver, [{
                key: 'update',
                value: function update(mouse, r) {
                    var _iteratorNormalCompletion = true;
                    var _didIteratorError = false;
                    var _iteratorError = undefined;

                    try {
                        for (var _iterator = this.particles[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
                            var p = _step.value;

                            p.update(mouse, r);
                        }
                    } catch (err) {
                        _didIteratorError = true;
                        _iteratorError = err;
                    } finally {
                        try {
                            if (!_iteratorNormalCompletion && _iterator.return) {
                                _iterator.return();
                            }
                        } finally {
                            if (_didIteratorError) {
                                throw _iteratorError;
                            }
                        }
                    }
                }
            }, {
                key: 'render',
                value: function render(context) {
                    var canvas = context.canvas;
                    context.clearRect(0, 0, canvas.width, canvas.height);
                    var _iteratorNormalCompletion2 = true;
                    var _didIteratorError2 = false;
                    var _iteratorError2 = undefined;

                    try {
                        for (var _iterator2 = this.particles[Symbol.iterator](), _step2; !(_iteratorNormalCompletion2 = (_step2 = _iterator2.next()).done); _iteratorNormalCompletion2 = true) {
                            var p = _step2.value;

                            p.draw(context);
                        }
                    } catch (err) {
                        _didIteratorError2 = true;
                        _iteratorError2 = err;
                    } finally {
                        try {
                            if (!_iteratorNormalCompletion2 && _iterator2.return) {
                                _iterator2.return();
                            }
                        } finally {
                            if (_didIteratorError2) {
                                throw _iteratorError2;
                            }
                        }
                    }
                }
            }, {
                key: 'reset',
                value: function reset(index, x, y, color) {
                    if (index < this.particles.length) {
                        this.particles[index].reset(x, y, color);
                    } else {
                        var p = this.builder.setOriginX(x).setOriginY(y).setColor(color).build();
                        this.particles.push(p);
                    }
                }
            }, {
                key: 'resize',
                value: function resize(len) {
                    this.particles.length = len;
                }
            }]);

            return ParticleObserver;
        })();

        return {
            builder: function builder() {
                return new ParticleBuilder();
            },
            observer: function observer(builder) {
                return new ParticleObserver(builder);
            }
        };
    }
})();
