(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('promotion-ang', {
            parent: 'entity',
            url: '/promotion-ang?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.promotion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/promotion/promotionsang.html',
                    controller: 'PromotionAngController',
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
                    $translatePartialLoader.addPart('promotion');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('promotion-ang-detail', {
            parent: 'promotion-ang',
            url: '/promotion-ang/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.promotion.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/promotion/promotion-ang-detail.html',
                    controller: 'PromotionAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('promotion');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Promotion', function($stateParams, Promotion) {
                    return Promotion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'promotion-ang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('promotion-ang-detail.edit', {
            parent: 'promotion-ang-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promotion/promotion-ang-dialog.html',
                    controller: 'PromotionAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Promotion', function(Promotion) {
                            return Promotion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('event-ang-detail.new', {
            parent: 'event-ang-detail',
            url: '/promotions/new',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promotion/promotion-ang-dialog.html',
                    controller: 'PromotionAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                imageUrl: null,
                                image: null,
                                imageContentType: null,
                                initialTime: null,
                                finalTime: null,
                                maximumCodePerUser: null,
                                id: null
                            };
                        },   translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                              $translatePartialLoader.addPart('promotion');
                                              $translatePartialLoader.addPart('global');
                                              return $translate.refresh();
                                          }]
                    }
                }).result.then(function() {
                    $state.go('event-ang-detail', null, { reload: 'event-ang-detail' });
                }, function() {
                    $state.go('event-ang-detail');
                });
            }]
        })
        .state('promotion-ang.edit', {
            parent: 'promotion-ang',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promotion/promotion-ang-dialog.html',
                    controller: 'PromotionAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Promotion', function(Promotion) {
                            return Promotion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('promotion-ang', null, { reload: 'promotion-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('promotion-ang.delete', {
            parent: 'promotion-ang',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promotion/promotion-ang-delete-dialog.html',
                    controller: 'PromotionAngDetailController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Promotion', function(Promotion) {
                            return Promotion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', null, { reload: '^' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('event-ang-detail.promotions', {
            parent: 'event-ang-detail',
            url: '/promotions',
            data: {
                  authorities: ['ROLE_USER','ROLE_OWNER','ROLE_ADMIN'],
                pageTitle: 'conocheApp.promotion.home.title'
            },
            views: {
                'eventContent@event-ang-detail': {
                    templateUrl: 'app/entities/promotion/event-promotions.html',
                    controller: 'EventPromotionsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('promotion');
                    return $translate.refresh();
                }],

                event: ['idEvent', 'Event', function(idEvent, Event) {
                    return Event.get({id : idEvent}).$promise;
                }],
            }
        });
    }

})();
