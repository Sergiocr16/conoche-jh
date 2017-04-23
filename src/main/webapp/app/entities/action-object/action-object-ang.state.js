(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('action-object-ang', {
            parent: 'entity',
            url: '/action-object-ang?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.actionObject.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/action-object/action-objectsang.html',
                    controller: 'ActionObjectAngController',
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
                    $translatePartialLoader.addPart('actionObject');
                    $translatePartialLoader.addPart('actionType');
                    $translatePartialLoader.addPart('actionObjectType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('action-object-ang-detail', {
            parent: 'action-object-ang',
            url: '/action-object-ang/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.actionObject.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/action-object/action-object-ang-detail.html',
                    controller: 'ActionObjectAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('actionObject');
                    $translatePartialLoader.addPart('actionType');
                    $translatePartialLoader.addPart('actionObjectType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ActionObject', function($stateParams, ActionObject) {
                    return ActionObject.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'action-object-ang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('action-object-ang-detail.edit', {
            parent: 'action-object-ang-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action-object/action-object-ang-dialog.html',
                    controller: 'ActionObjectAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ActionObject', function(ActionObject) {
                            return ActionObject.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('action-object-ang.new', {
            parent: 'action-object-ang',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action-object/action-object-ang-dialog.html',
                    controller: 'ActionObjectAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                objectId: null,
                                actionType: null,
                                objectType: null,
                                creationTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('action-object-ang', null, { reload: 'action-object-ang' });
                }, function() {
                    $state.go('action-object-ang');
                });
            }]
        })
        .state('action-object-ang.edit', {
            parent: 'action-object-ang',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action-object/action-object-ang-dialog.html',
                    controller: 'ActionObjectAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ActionObject', function(ActionObject) {
                            return ActionObject.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('action-object-ang', null, { reload: 'action-object-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('action-object-ang.delete', {
            parent: 'action-object-ang',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action-object/action-object-ang-delete-dialog.html',
                    controller: 'ActionObjectAngDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ActionObject', function(ActionObject) {
                            return ActionObject.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('action-object-ang', null, { reload: 'action-object-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
