


/**
 *
 * Created by melvin on 2/6/2017.
 * Transpile file
 */

(function () {
    'use strict';

    var _slicedToArray = function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; }();

    var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

    function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

    angular.module('conocheApp').factory('ParticleAnimator', ParticleImageAnimatorService);

    ParticleImageAnimatorService.$inject = ['Particle', '$window', 'ImageUtil', 'shuffleFilter'];

    function ParticleImageAnimatorService(Particle, $window, ImageUtil, shuffleFilter) {
        var ParticleImage = function () {
            function ParticleImage(builder, context) {
                _classCallCheck(this, ParticleImage);

                this.observer = Particle.observer(builder);
                this.context = context;
                this.spacing = 3;
                this.animating = false;
                this.color = null;
            }

            _createClass(ParticleImage, [{
                key: 'setSpacing',
                value: function setSpacing(spacing) {
                    this.spacing = spacing || this.spacing;
                    return this;
                }
            }, {
                key: 'setColor',
                value: function setColor(color) {
                    this.color = color;
                    return this;
                }
            }, {
                key: 'reset',
                value: function reset(callback) {
                    var context = this.context;
                    callback(context);

                    var height = context.canvas.height;
                    var width = context.canvas.width;
                    shuffleFilter(this.observer.particles);
                    this.observer.builder.setX(Math.random() * width).setY(Math.random() * height);

                    var gen = ImageUtil.forEachAlphaPixel(context, this.spacing);
                    var i = 0;
                    var _iteratorNormalCompletion = true;
                    var _didIteratorError = false;
                    var _iteratorError = undefined;

                    try {
                        for (var _iterator = gen[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
                            var _ref = _step.value;

                            var _ref2 = _slicedToArray(_ref, 3);

                            var x = _ref2[0];
                            var y = _ref2[1];
                            var _ref2$ = _ref2[2];
                            var R = _ref2$.R;
                            var G = _ref2$.G;
                            var B = _ref2$.B;
                            var A = _ref2$.A;

                            var color = this.color || 'rgba(' + R + ',' + G + ',' + B + ',' + A + ')';
                            this.observer.reset(i++, x, y, color);
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

                    this.observer.resize(i);
                    return this;
                }
            }, {
                key: 'start',
                value: function start(mouse, radius) {
                    this.animating = true;
                    animate(this, mouse, radius);
                    return this;
                }
            }, {
                key: 'stop',
                value: function stop() {
                    this.animating = false;
                    return this;
                }
            }]);

            return ParticleImage;
        }();

        function animate(self, mouse, radius) {
            if (!self.animating) {
                return;
            }
            self.observer.update(mouse, radius);
            self.observer.render(self.context);
            $window.requestAnimationFrame(function () {
                return animate(self, mouse, radius);
            });
        }

        return {
            create: function create(builder, context) {
                return new ParticleImage(builder, context);
            }
        };
    }
})();
