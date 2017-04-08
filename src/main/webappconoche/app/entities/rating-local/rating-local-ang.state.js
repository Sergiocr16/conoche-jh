(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('local-ang-detail.rating-local-ang', {
            parent: 'local-ang-detail',
            url: '/rating-local-ang?page&sort&search',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER','ROLE_ADMIN'],
                pageTitle: 'conocheApp.ratingLocal.home.title'
            },
            views: {
                'localContent@local-ang-detail': {
                    templateUrl: 'app/entities/rating-local/rating-localsang.html',
                    controller: 'RatingLocalAngController',
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
                    $translatePartialLoader.addPart('ratingLocal');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('rating-local-ang-detail', {
            parent: 'rating-local-ang',
            url: '/rating-local-ang/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.ratingLocal.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rating-local/rating-local-ang-detail.html',
                    controller: 'RatingLocalAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ratingLocal');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RatingLocal', function($stateParams, RatingLocal) {
                    return RatingLocal.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'rating-local-ang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('rating-local-ang-detail.edit', {
            parent: 'rating-local-ang-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rating-local/rating-local-ang-dialog.html',
                    controller: 'RatingLocalAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RatingLocal', function(RatingLocal) {
                            return RatingLocal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('local-ang-detail.newRating', {
            parent: 'local-ang-detail',
            url: '/rate',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rating-local/rating-local-ang-dialog.html',
                    controller: 'RatingLocalAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                rating: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('local-ang-detail', null, { reload: true });
                }, function() {
                    $state.go('local-ang-detail');
                });
            }]
        })
        .state('rating-local-ang.edit', {
            parent: 'rating-local-ang',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rating-local/rating-local-ang-dialog.html',
                    controller: 'RatingLocalAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RatingLocal', function(RatingLocal) {
                            return RatingLocal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rating-local-ang', null, { reload: 'rating-local-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rating-local-ang.delete', {
            parent: 'rating-local-ang',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rating-local/rating-local-ang-delete-dialog.html',
                    controller: 'RatingLocalAngDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RatingLocal', function(RatingLocal) {
                            return RatingLocal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rating-local-ang', null, { reload: 'rating-local-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
