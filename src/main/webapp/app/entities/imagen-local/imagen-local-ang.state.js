(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('imagen-local-ang', {
            parent: 'entity',
            url: '/imagen-local-ang?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.imagenLocal.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/imagen-local/imagen-localsang.html',
                    controller: 'ImagenLocalAngController',
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
                    $translatePartialLoader.addPart('imagenLocal');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('imagen-local-ang-detail', {
            parent: 'imagen-local-ang',
            url: '/imagen-local-ang/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.imagenLocal.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/imagen-local/imagen-local-ang-detail.html',
                    controller: 'ImagenLocalAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('imagenLocal');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ImagenLocal', function($stateParams, ImagenLocal) {
                    return ImagenLocal.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'imagen-local-ang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('imagen-local-ang-detail.edit', {
            parent: 'imagen-local-ang-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/imagen-local/imagen-local-ang-dialog.html',
                    controller: 'ImagenLocalAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ImagenLocal', function(ImagenLocal) {
                            return ImagenLocal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('imagen-local-ang.new', {
            parent: 'imagen-local-ang',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/imagen-local/imagen-local-ang-dialog.html',
                    controller: 'ImagenLocalAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                imageUrl: null,
                                image: null,
                                imageContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('imagen-local-ang', null, { reload: 'imagen-local-ang' });
                }, function() {
                    $state.go('imagen-local-ang');
                });
            }]
        })
        .state('imagen-local-ang.edit', {
            parent: 'imagen-local-ang',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/imagen-local/imagen-local-ang-dialog.html',
                    controller: 'ImagenLocalAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ImagenLocal', function(ImagenLocal) {
                            return ImagenLocal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('imagen-local-ang', null, { reload: 'imagen-local-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('imagen-local-ang.delete', {
            parent: 'imagen-local-ang',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/imagen-local/imagen-local-ang-delete-dialog.html',
                    controller: 'ImagenLocalAngDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ImagenLocal', function(ImagenLocal) {
                            return ImagenLocal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('imagen-local-ang', null, { reload: 'imagen-local-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
