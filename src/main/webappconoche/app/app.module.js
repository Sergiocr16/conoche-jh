(function() {
    'use strict';

    angular
        .module('conocheApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'cloudinary',
            'wu.masonry',
            'FBAngular',
            'ngAnimate'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler', '$rootScope', '$state', '$stateParams'];

    function run(stateHandler, translationHandler, $rootScope, $state, $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
        stateHandler.initialize();
        translationHandler.initialize();
        Metronic.init();
        Metronic.initComponents();
        Metronic.initAjax();
        angular.element(document).ready(function () {
            $('#loaded').show();
            $('#loading').fadeOut(30);
        });

    }
})();
