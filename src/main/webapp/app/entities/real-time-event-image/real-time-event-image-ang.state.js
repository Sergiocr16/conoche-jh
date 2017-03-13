(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('real-time-event-image-ang', {
            parent: 'entity',
            url: '/real-time-event-image-ang?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.realTimeEventImage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/real-time-event-image/real-time-event-imagesang.html',
                    controller: 'RealTimeEventImageAngController',
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
                    $translatePartialLoader.addPart('realTimeEventImage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('real-time-event-image-ang-detail', {
            parent: 'real-time-event-image-ang',
            url: '/real-time-event-image-ang/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.realTimeEventImage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/real-time-event-image/real-time-event-image-ang-detail.html',
                    controller: 'RealTimeEventImageAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('realTimeEventImage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RealTimeEventImage', function($stateParams, RealTimeEventImage) {
                    return RealTimeEventImage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'real-time-event-image-ang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('real-time-event-image-ang-detail.edit', {
            parent: 'real-time-event-image-ang-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/real-time-event-image/real-time-event-image-ang-dialog.html',
                    controller: 'RealTimeEventImageAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RealTimeEventImage', function(RealTimeEventImage) {
                            return RealTimeEventImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('real-time-event-image-ang.new', {
            parent: 'real-time-event-image-ang',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/real-time-event-image/real-time-event-image-ang-dialog.html',
                    controller: 'RealTimeEventImageAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                imageUrl: null,
                                creationTime: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('real-time-event-image-ang', null, { reload: 'real-time-event-image-ang' });
                }, function() {
                    $state.go('real-time-event-image-ang');
                });
            }]
        })
        .state('real-time-event-image-ang.edit', {
            parent: 'real-time-event-image-ang',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/real-time-event-image/real-time-event-image-ang-dialog.html',
                    controller: 'RealTimeEventImageAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RealTimeEventImage', function(RealTimeEventImage) {
                            return RealTimeEventImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('real-time-event-image-ang', null, { reload: 'real-time-event-image-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('real-time-event-image-ang.delete', {
            parent: 'real-time-event-image-ang',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/real-time-event-image/real-time-event-image-ang-delete-dialog.html',
                    controller: 'RealTimeEventImageAngDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RealTimeEventImage', function(RealTimeEventImage) {
                            return RealTimeEventImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('real-time-event-image-ang', null, { reload: 'real-time-event-image-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
