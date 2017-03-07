(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('event-image-ang', {
            parent: 'entity',
            url: '/event-image-ang?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.eventImage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/event-image/event-imagesang.html',
                    controller: 'EventImageAngController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('eventImage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('event-image-ang-detail', {
            parent: 'event-image-ang',
            url: '/event-image-ang/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.eventImage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/event-image/event-image-ang-detail.html',
                    controller: 'EventImageAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('eventImage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EventImage', function($stateParams, EventImage) {
                    return EventImage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'event-image-ang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('event-image-ang-detail.edit', {
            parent: 'event-image-ang-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event-image/event-image-ang-dialog.html',
                    controller: 'EventImageAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EventImage', function(EventImage) {
                            return EventImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('event-image-ang.new', {
            parent: 'event-image-ang',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event-image/event-image-ang-dialog.html',
                    controller: 'EventImageAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                image: null,
                                imageContentType: null,
                                imageUrl: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('event-image-ang', null, { reload: 'event-image-ang' });
                }, function() {
                    $state.go('event-image-ang');
                });
            }]
        })
        .state('event-image-ang.edit', {
            parent: 'event-image-ang',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event-image/event-image-ang-dialog.html',
                    controller: 'EventImageAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EventImage', function(EventImage) {
                            return EventImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('event-image-ang', null, { reload: 'event-image-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('event-image-ang.delete', {
            parent: 'event-image-ang',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event-image/event-image-ang-delete-dialog.html',
                    controller: 'EventImageAngDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EventImage', function(EventImage) {
                            return EventImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('event-image-ang', null, { reload: 'event-image-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
