(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('local-ang', {
            parent: 'entity',
            url: '/local-ang?page&sort&search&provincia&idCategory',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER'],
                pageTitle: 'conocheApp.local.home.title',
                phrase: 'conocheApp.local.home.phrase'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/local/localsang.html',
                    controller: 'LocalAngController',
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
                search: null,
                provincia: null,
                idCategory: null
            },
            resolve: {
                optionalParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search,
                        provincia: $stateParams.provincia,
                        idCategory: $stateParams.idCategory
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('local');
                    $translatePartialLoader.addPart('provincia');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('local-ang-detail', {
            parent: 'local-ang',
            url: '/local-ang/{id}',
            data: {
               authorities: ['ROLE_USER','ROLE_OWNER'],
                pageTitle: 'conocheApp.local.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/local/local-ang-detail.html',
                    controller: 'LocalAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('local');
                    $translatePartialLoader.addPart('provincia');
                    $translatePartialLoader.addPart('ratingLocal');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Local', function($stateParams, Local) {
                    return Local.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'local-ang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('local-ang-detail.edit', {
            parent: 'local-ang-detail',
            url: '/detail/edit',
            data: {
                 authorities: ['ROLE_USER','ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/local/local-ang-dialog.html',
                    controller: 'LocalAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Local', function(Local) {
                            return Local.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('local-ang.new', {
            parent: 'local-ang',
            url: '/new',
            data: {
             authorities: ['ROLE_USER','ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/local/local-ang-dialog.html',
                    controller: 'LocalAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                longitud: null,
                                banner: null,
                                bannerContentType: null,
                                bannerUrl: null,
                                latitud: null,
                                descripcion: null,
                                provincia: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('local-ang', null, { reload: 'local-ang' });
                }, function() {
                    $state.go('local-ang');
                });
            }]
        })
        .state('local-ang.edit', {
            parent: 'local-ang',
            url: '/{id}/edit',
            data: {
               authorities: ['ROLE_USER','ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/local/local-ang-dialog.html',
                    controller: 'LocalAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Local', function(Local) {
                            return Local.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('local-ang', null, { reload: 'local-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('local-ang.delete', {
            parent: 'local-ang',
            url: '/{id}/delete',
            data: {
                  authorities: ['ROLE_USER','ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/local/local-ang-delete-dialog.html',
                    controller: 'LocalAngDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Local', function(Local) {
                            return Local.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('local-ang', null, { reload: 'local-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
