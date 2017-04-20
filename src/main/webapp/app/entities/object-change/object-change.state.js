(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('object-change', {
            parent: 'entity',
            url: '/object-change?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.objectChange.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/object-change/object-changes.html',
                    controller: 'ObjectChangeController',
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
                    $translatePartialLoader.addPart('objectChange');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('object-change-detail', {
            parent: 'object-change',
            url: '/object-change/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.objectChange.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/object-change/object-change-detail.html',
                    controller: 'ObjectChangeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('objectChange');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ObjectChange', function($stateParams, ObjectChange) {
                    return ObjectChange.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'object-change',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('object-change-detail.edit', {
            parent: 'object-change-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/object-change/object-change-dialog.html',
                    controller: 'ObjectChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ObjectChange', function(ObjectChange) {
                            return ObjectChange.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('object-change.new', {
            parent: 'object-change',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/object-change/object-change-dialog.html',
                    controller: 'ObjectChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fieldName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('object-change', null, { reload: 'object-change' });
                }, function() {
                    $state.go('object-change');
                });
            }]
        })
        .state('object-change.edit', {
            parent: 'object-change',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/object-change/object-change-dialog.html',
                    controller: 'ObjectChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ObjectChange', function(ObjectChange) {
                            return ObjectChange.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('object-change', null, { reload: 'object-change' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('object-change.delete', {
            parent: 'object-change',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/object-change/object-change-delete-dialog.html',
                    controller: 'ObjectChangeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ObjectChange', function(ObjectChange) {
                            return ObjectChange.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('object-change', null, { reload: 'object-change' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
