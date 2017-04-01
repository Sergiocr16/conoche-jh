(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('event-ang-detail.live-messages', {
            parent: 'event-ang-detail',
            url: '/real-time-event-messages',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_OWNER','ROLE_USER']
            },
            views: {
                'eventContent@event-ang-detail': {
                    templateUrl: 'app/entities/message/live-messages.html',
                    controller: 'LiveMessagesController',
                    controllerAs: 'vm'
                }
            },
            params: {
//                page: {
//                    value: '1',
//                    squash: true
//                },
//                sort: {
//                    value: 'id,asc',
//                    squash: true
//                },
//                search: null
            },
            resolve: {
//                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
//                    return {
//                        page: PaginationUtil.parsePage($stateParams.page),
//                        sort: $stateParams.sort,
//                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
//                        ascending: PaginationUtil.parseAscending($stateParams.sort),
//                        search: $stateParams.search
//                    };
//                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('message');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            },
            onEnter: ['$stateParams', 'WSRealTimeEventMessages','idEvent', function($stateParams, WSRealTimeEventMessages,idEvent) {
                WSRealTimeEventMessages.subscribe(idEvent);
            }],
            onExit: ['$stateParams', 'WSRealTimeEventMessages','idEvent', function($stateParams, WSRealTimeEventMessages,idEvent) {
                WSRealTimeEventMessages.unsubscribe(idEvent);
            }]
        })
        .state('message-ang', {
            parent: 'entity',
            url: '/message-ang?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.message.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/message/messagesang.html',
                    controller: 'MessageAngController',
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
                    $translatePartialLoader.addPart('message');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('message-ang-detail', {
            parent: 'message-ang',
            url: '/message-ang/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.message.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/message/message-ang-detail.html',
                    controller: 'MessageAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('message');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Message', function($stateParams, Message) {
                    return Message.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'message-ang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('message-ang-detail.edit', {
            parent: 'message-ang-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/message/message-ang-dialog.html',
                    controller: 'MessageAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Message', function(Message) {
                            return Message.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('event-ang-detail.live-messages.newComment', {
            parent: 'event-ang-detail.live-messages',
            url: '/newComment/',
            data: {
                authorities: ['ROLE_USER','ROLE_ADMIN','ROLE_OWNER']
            },
            onEnter: ['WSRealTimeEventMessages','$stateParams', '$state', '$uibModal', function(WSRealTimeEventMessages,$stateParams, $state, $uibModal) {
              WSRealTimeEventMessages.subscribe(Number($stateParams.id));
                $uibModal.open({
                    templateUrl: 'app/entities/message/message-ang-dialog.html',
                    controller: 'MessageAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                payload: null,
                                creationTime: null,
                                id: null,
                                eventId: Number($stateParams.id)
                            };
                        },
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('message');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                    }
                }).result.then(function() {
                    $state.go('event-ang-detail.live-messages', null, { reload: false });
                }, function() {
                    $state.go('event-ang-detail.live-messages');
                });
            }],
            onExit: ['$stateParams', 'WSRealTimeEventMessages', function($stateParams, WSRealTimeEventMessages) {
                WSRealTimeEventMessages.unsubscribe($stateParams.idEvent);
            }]
        })
        .state('message-ang.edit', {
            parent: 'message-ang',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/message/message-ang-dialog.html',
                    controller: 'MessageAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Message', function(Message) {
                            return Message.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('message-ang', null, { reload: 'message-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('message-ang.delete', {
            parent: 'message-ang',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/message/message-ang-delete-dialog.html',
                    controller: 'MessageAngDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Message', function(Message) {
                            return Message.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('message-ang', null, { reload: 'message-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
