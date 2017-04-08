(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('event-ang', {
            parent: 'entity',
            url: '/event-ang?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_OWNER','ROLE_USER'],
                pageTitle: 'conocheApp.event.home.title',
                phrase: 'conocheApp.event.home.phrase'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/event/eventsang.html',
                    controller: 'EventAngController',
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
                    $translatePartialLoader.addPart('event');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
             .state('event-ang-by-owner', {
                    parent: 'entity',
                    url: '/event-ang-by-owner?page&sort&search',
                    data: {
                        authorities: ['ROLE_OWNER'],
                        pageTitle: 'conocheApp.event.home.myEvents',
                        phrase: 'conocheApp.event.home.phrase'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/event/event-ang-by-owner.html',
                            controller: 'EventAngByOwnerController',
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
                            $translatePartialLoader.addPart('event');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                    }
                })
        .state('event-ang-detail', {
            parent: 'event-ang',
            url: '/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_OWNER','ROLE_USER']

            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/event/event-ang-detail.html',
                    controller: 'EventAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('event');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Event', function($stateParams, Event) {
                    return Event.get({id : $stateParams.id}).$promise;
                }],
                idEvent: ['$stateParams', function($stateParams, Event) {
                    return $stateParams.id;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'event-ang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('event-ang-detail.edit', {
            parent: 'event-ang-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event/event-ang-dialog.html',
                    controller: 'EventAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Event', function(Event) {
                            return Event.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
            .state('event-ang-detail.event-location', {
                parent: 'event-ang-detail',
                url: '/location',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_OWNER','ROLE_USER'],
                    pageTitle: 'conocheApp.event.Location'
                },
                views: {
                    'eventContent@event-ang-detail': {
                        templateUrl: 'app/entities/event/event-location.html',
                        controller: 'EventLocationController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('event');
                        return $translate.refresh();
                    }]
                }
            })

         .state('event-ang.promotionDetail', {
             parent: 'event-ang',
             url: '/{id}/edit',
             data: {
                 authorities: ['ROLE_USER']
             },
             onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                 $uibModal.open({
                     templateUrl: 'app/entities/promotion/promotion-ang-idetail-in-events.html',
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
                     $state.go('event-ang', null, { reload: 'event-ang' });
                 }, function() {
                     $state.go('^');
                 });
             }]
         })
        .state('event-ang-by-owner.new', {
            parent: 'event-ang-by-owner',
            url: '/new',
            data: {
                authorities: ['ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event/event-ang-dialog.html',
                    controller: 'EventAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                details: null,
                                price: null,
                                banner: null,
                                initialTime: null,
                                finalTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('event-ang-by-owner', null, { reload: 'event-ang-by-owner' });
                }, function() {
                    $state.go('event-ang-by-owner');
                });
            }]
        })
        .state('event-ang.edit', {
            parent: 'event-ang',
            url: '/{id}/edit',
            data: {
                  authorities: ['ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event/event-ang-dialog.html',
                    controller: 'EventAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Event', function(Event) {
                            return Event.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('event-ang', null, { reload: 'event-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('event-ang.delete', {
            parent: 'event-ang',
            url: '/{id}/delete',
            data: {
                  authorities: ['ROLE_OWNER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event/event-ang-delete-dialog.html',
                    controller: 'EventAngDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Event', function(Event) {
                            return Event.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('event-ang', null, { reload: 'event-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
