(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('promotion-code-ang', {
            parent: 'entity',
            url: '/promotion-code-ang?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.promotionCode.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/promotion-code/promotion-codesang.html',
                    controller: 'PromotionCodeAngController',
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
                    $translatePartialLoader.addPart('promotionCode');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('promotion-code-ang-detail', {
            parent: 'promotion-code-ang',
            url: '/promotion-code-ang/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.promotionCode.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/promotion-code/promotion-code-ang-detail.html',
                    controller: 'PromotionCodeAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('promotionCode');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PromotionCode', function($stateParams, PromotionCode) {
                    return PromotionCode.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'promotion-code-ang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('swapCoupon', {
            url: '/swapCoupon',
            parent: 'entity',
            data: {
                authorities: ['ROLE_OWNER'],
                pageTitle: 'global.menu.entities.canjearPromo'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/promotion-code/swap-coupon.html',
                    controller: 'SwapCouponController',
                    controllerAs: 'vm',
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('promotionCode');
                    $translatePartialLoader.addPart('event');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'promotion-code-ang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('home.myPromotions', {
            parent: 'home',
            url: '/myPromotions',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal','WSPromotionCodeService','Principal', function($stateParams, $state, $uibModal,WSPromotionCodeService,Principal) {
             Principal.identity().then(function(user){
              WSPromotionCodeService.subscribe(user.id);
             })
                $uibModal.open({
                    templateUrl: 'app/entities/promotion-code/my-promotions-codes.html',
                    controller: 'MyPromotionCodesController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                        $translatePartialLoader.addPart('event');
                                        $translatePartialLoader.addPart('global');
                                        return $translate.refresh();
                                    }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }],
            onExit: ['$stateParams', 'WSPromotionCodeService','Principal', function($stateParams, WSPromotionCodeService,Principal) {
                Principal.identity().then(function(user){
                WSPromotionCodeService.unsubscribe(user.id)
                })
            }]
        })
        .state('promotion-code-ang-detail.edit', {
            parent: 'promotion-code-ang-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promotion-code/promotion-code-ang-dialog.html',
                    controller: 'PromotionCodeAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PromotionCode', function(PromotionCode) {
                            return PromotionCode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('promotion-code-ang.new', {
            parent: 'promotion-code-ang',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promotion-code/promotion-code-ang-dialog.html',
                    controller: 'PromotionCodeAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                active: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('promotion-code-ang', null, { reload: 'promotion-code-ang' });
                }, function() {
                    $state.go('promotion-code-ang');
                });
            }]
        })
        .state('promotion-code-ang.edit', {
            parent: 'promotion-code-ang',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promotion-code/promotion-code-ang-dialog.html',
                    controller: 'PromotionCodeAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PromotionCode', function(PromotionCode) {
                            return PromotionCode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('promotion-code-ang', null, { reload: 'promotion-code-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('promotion-code-ang.delete', {
            parent: 'promotion-code-ang',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/promotion-code/promotion-code-ang-delete-dialog.html',
                    controller: 'PromotionCodeAngDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PromotionCode', function(PromotionCode) {
                            return PromotionCode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('promotion-code-ang', null, { reload: 'promotion-code-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
