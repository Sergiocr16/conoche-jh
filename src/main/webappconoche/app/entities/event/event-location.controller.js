/**
 * Created by melvin on 4/1/2017.
 */
(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('EventLocationController', EventLocationController);

    EventLocationController.$inject = ['$stateParams'];

    function EventLocationController($stateParams) {
        var vm = this;

    }
})();
