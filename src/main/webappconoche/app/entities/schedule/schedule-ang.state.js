(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('schedule-ang', {
            parent: 'entity',
            url: '/schedule-ang?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.schedule.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/schedule/schedulesang.html',
                    controller: 'ScheduleAngController',
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
                    $translatePartialLoader.addPart('schedule');
                    $translatePartialLoader.addPart('day');
                    $translatePartialLoader.addPart('day');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('schedule-ang-detail', {
            parent: 'schedule-ang',
            url: '/schedule-ang/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'conocheApp.schedule.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/schedule/schedule-ang-detail.html',
                    controller: 'ScheduleAngDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('schedule');
                    $translatePartialLoader.addPart('day');
                    $translatePartialLoader.addPart('day');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Schedule', function($stateParams, Schedule) {
                    return Schedule.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'schedule-ang',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('schedule-ang-detail.edit', {
            parent: 'schedule-ang-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schedule/schedule-ang-dialog.html',
                    controller: 'ScheduleAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Schedule', function(Schedule) {
                            return Schedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('schedule-ang.new', {
            parent: 'schedule-ang',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schedule/schedule-ang-dialog.html',
                    controller: 'ScheduleAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                initialDay: null,
                                finalDay: null,
                                initialTime: null,
                                finalTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('schedule-ang', null, { reload: 'schedule-ang' });
                }, function() {
                    $state.go('schedule-ang');
                });
            }]
        })
        .state('schedule-ang.edit', {
            parent: 'schedule-ang',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schedule/schedule-ang-dialog.html',
                    controller: 'ScheduleAngDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Schedule', function(Schedule) {
                            return Schedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('schedule-ang', null, { reload: 'schedule-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('schedule-ang.delete', {
            parent: 'schedule-ang',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schedule/schedule-ang-delete-dialog.html',
                    controller: 'ScheduleAngDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Schedule', function(Schedule) {
                            return Schedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('schedule-ang', null, { reload: 'schedule-ang' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
