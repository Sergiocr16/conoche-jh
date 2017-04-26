(function() {
    'use strict';

    angular
        .module('conocheApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('event-ang-detail.real-time-event-image-gallery', {
                parent: 'event-ang-detail',
                url: '/real-time-event-image-gallery',
                data: {

                    authorities: ['ROLE_ADMIN','ROLE_OWNER','ROLE_USER']

                },
                views: {
                    'eventContent@event-ang-detail': {
                        templateUrl: 'app/entities/real-time-event-image/real-time-event-image-gallery.html',
                        controller: 'RealTimeEventImageGalleryController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    }
                },
                resolve: {
                    isOwner: ['idEvent', 'Owner', function (idEvent, Owner) {
                        return Owner.isOwner(idEvent);
                    }],

                    page: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return PaginationUtil.parsePage($stateParams.page);
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('realTimeEventImage');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],

                },
                onEnter: ['idEvent', 'WSRealTimeEventImages', function(idEvent, WSRealTimeEventImages) {
                    WSRealTimeEventImages.subscribeNewImages(idEvent);
                    WSRealTimeEventImages.subscribeDeleteImages(idEvent);
                }],
                onExit: ['idEvent', 'WSRealTimeEventImages', function(idEvent, WSRealTimeEventImages) {
                    WSRealTimeEventImages.unsubscribeNewImages(idEvent);
                    WSRealTimeEventImages.unsubscribeDeleteImages(idEvent);
                }]
            })
            .state('real-time-event-image-slideshow', {
                parent: 'entity',
                url: '/real-time-event-image-slideshow/{idEvent}',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_OWNER','ROLE_USER'],
                    pageTitle: 'conocheApp.realTimeEventImage.home.title'
                },
                views: {
                    'navbar@': {
                        template: '',
                        controller: 'NavbarController',
                    },
                    'otherContent@': {
                        templateUrl: 'app/entities/real-time-event-image/real-time-event-image-slideshow.html',
                        controller: 'RealTimeEventImageSlideshowController',
                        controllerAs: 'vm'
                    },
                    'head@': {
                        template: ''
                    },
                    'footer@': {
                        template: ''
                    },
                    'content@': { }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('realTimeEventImage');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                },
                onEnter: ['$stateParams', 'WSRealTimeEventImages', function($stateParams, WSRealTimeEventImages) {
                    WSRealTimeEventImages.subscribeNewImages($stateParams.idEvent);
                    WSRealTimeEventImages.subscribeDeleteImages($stateParams.idEvent);
                }],
                onExit: ['$stateParams', 'WSRealTimeEventImages', function($stateParams, WSRealTimeEventImages) {
                    WSRealTimeEventImages.unsubscribeNewImages($stateParams.idEvent);
                    WSRealTimeEventImages.unsubscribeDeleteImages($stateParams.idEvent);
                }]
            })
            .state('event-ang-detail.real-time-event-image-gallery.savews', {
                parent: 'event-ang-detail.real-time-event-image-gallery',
                url: '/save',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_OWNER','ROLE_USER']
                },
                onEnter: [ '$state', '$uibModal', 'idEvent', function($state, $uibModal, idEvent) {
                    $uibModal.open({
                        templateUrl: 'app/entities/real-time-event-image/real-time-event-image-savews.html',
                        controller: 'RealTimeEventImageSaveWSController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            idEvent: [function() {
                                return idEvent;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('^');
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            //quitar ROLE_USER luego
            .state('event-ang-detail.real-time-event-image-gallery.delete', {
                parent: 'event-ang-detail.real-time-event-image-gallery',
                url: '/{idImage}/delete',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_OWNER','ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', 'isOwner', 'idEvent', function($stateParams, $state, $uibModal, isOwner, idEvent) {
                    if(!isOwner) {
                        $state.go('event-ang-detail.real-time-event-image-gallery', { id: idEvent});
                        return;
                    }
                    $uibModal.open({
                        templateUrl: 'app/entities/real-time-event-image/real-time-event-image-gallery-delete-dialog.html',
                        controller: 'RealTimeEventImageGalleryDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['RealTimeEventImage', function(RealTimeEventImage) {
                                return RealTimeEventImage.get({id : $stateParams.idImage})
                                    .$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('event-ang-detail.real-time-event-image-gallery');
                    }, function() {
                        $state.go('event-ang-detail.real-time-event-image-gallery');
                    });
                }]
            });
    }

})();
