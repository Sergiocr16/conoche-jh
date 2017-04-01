(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('landing-page', {
            parent: 'app',
            url: '/landing-page',
            data: {
                authorities: []
            },
            views: {
                'landingPage@': {
                    templateUrl: 'app/layouts/landing-page/landing-page.html',
                    controller: 'LandingPageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }],
            }

        });
    }
})();
