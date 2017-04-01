(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('servicio-ang', {
            parent: 'entity',
            url: '/servicio-ang?page&sort&search',
            data: {
      authorities: ['ROLE_USER','ROLE_OWNER','ROLE_ADMIN'],
                pageTitle: 'conocheApp.servicio.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/servicio/serviciosang.html',
                    controller: 'ServicioAngController',
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
                    $translatePartialLoader.addPart('servicio');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('servicio-ang-detail', {
            parent: 'servicio-ang',
            url: '/servicio-ang/{id}',
            data: {
                               authorities: ['ROLE_USER','ROLE_OWNER','ROLE_ADMIN'],
                pageTitle: 'conocheApp.servicio.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/servicio/servicio-ang-detail.html',
                    controller: 'ServicioAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('servicio');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Servicio', function($stateParams, Servicio) {
                    return Servicio.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'servicio-ang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('servicio-ang-detail.edit', {
            parent: 'servicio-ang-detail',
            url: '/detail/edit',
            data: {
                           authorities: ['ROLE_USER','ROLE_OWNER','ROLE_ADMIN'], authorities: ['ROLE_USER,ROLE_OWNER,ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/servicio/servicio-ang-dialog.html',
                    controller: 'ServicioAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Servicio', function(Servicio) {
                            return Servicio.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('servicio-ang.new', {
            parent: 'servicio-ang',
            url: '/new',
            data: {
                     authorities: ['ROLE_USER','ROLE_OWNER','ROLE_ADMIN'],
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/servicio/servicio-ang-dialog.html',
                    controller: 'ServicioAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                descripcion: null,
                                icon: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('servicio-ang', null, { reload: 'servicio-ang' });
                }, function() {
                    $state.go('servicio-ang');
                });
            }]
        })
        .state('servicio-ang.edit', {
            parent: 'servicio-ang',
            url: '/{id}/edit',
            data: {
                              authorities: ['ROLE_USER','ROLE_OWNER','ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/servicio/servicio-ang-dialog.html',
                    controller: 'ServicioAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Servicio', function(Servicio) {
                            return Servicio.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('servicio-ang', null, { reload: 'servicio-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('servicio-ang.delete', {
            parent: 'servicio-ang',
            url: '/{id}/delete',
            data: {
                              authorities: ['ROLE_USER','ROLE_OWNER','ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/servicio/servicio-ang-delete-dialog.html',
                    controller: 'ServicioAngDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Servicio', function(Servicio) {
                            return Servicio.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('servicio-ang', null, { reload: 'servicio-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
            .state('event-ang-detail.services', {
                parent: 'event-ang-detail',
                url: '/services',
                data: {
                      authorities: ['ROLE_USER','ROLE_OWNER','ROLE_ADMIN'],
                    pageTitle: 'conocheApp.servicio.home.title'
                },
                views: {
                    'eventContent@event-ang-detail': {
                        templateUrl: 'app/entities/servicio/event-services.html',
                        controller: 'EventServicesController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('servicio');
                        return $translate.refresh();
                    }],

                    event: ['idEvent', 'Event', function(idEvent, Event) {
                        return Event.get({id : idEvent}).$promise;
                    }],
                }
            });
    }

})();
