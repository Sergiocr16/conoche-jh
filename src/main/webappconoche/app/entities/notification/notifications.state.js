/**
 * Created by melvin on 4/25/2017.
 */
(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('all-notification', {
                parent: 'entity',
                url: '/notification?page',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER'],
                    pageTitle: 'global.menu.notifications.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/notification/all-notifications.html',
                        controller: 'AllNotificationController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    }
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page)
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    }

})();
