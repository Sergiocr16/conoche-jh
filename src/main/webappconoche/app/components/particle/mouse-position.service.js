/**
 * Created by melvin on 3/15/2017.
 */


(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('MousePosition', MousePosition);

    MousePosition.$inject = ['$document'];

    function MousePosition ($document) {

        var services = {
            createMouseEvents: createMouseEvents,
        };
        return services;

        function createMouseEvents(element) {
            var mouse = { x: element.width, y: element.height };
            function setMouse(x, y) {
                var rect = element.getBoundingClientRect();
                mouse.x = x - rect.left;
                mouse.y = y - rect.top;
            }
            $document.bind("mousemove", event => {
                setMouse(event.clientX, event.clientY);
            });

            $document.bind("touchstart", event => {
                setMouse(event.changedTouches[0].clientX,
                    event.changedTouches[0].clientY);
            }, false);

            $document.bind("touchmove", event => {
                event.preventDefault();
                setMouse( event.targetTouches[0].clientX,
                    event.targetTouches[0].clientY);
            }, false);

            $document.bind("touchend", event => {
                event.preventDefault();
                setMouse(0, 0);
            }, false);

            return mouse;
        }
    }
})();
