'use strict';

angular.module('webstoreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tracking', {
                parent: 'entity',
                url: '/trackings',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.tracking.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tracking/trackings.html',
                        controller: 'TrackingController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tracking');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('tracking.detail', {
                parent: 'entity',
                url: '/tracking/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.tracking.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tracking/tracking-detail.html',
                        controller: 'TrackingDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tracking');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Tracking', function($stateParams, Tracking) {
                        return Tracking.get({id : $stateParams.id});
                    }]
                }
            })
            .state('tracking.new', {
                parent: 'tracking',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tracking/tracking-dialog.html',
                        controller: 'TrackingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    status: null,
                                    details: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('tracking', null, { reload: true });
                    }, function() {
                        $state.go('tracking');
                    })
                }]
            })
            .state('tracking.edit', {
                parent: 'tracking',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tracking/tracking-dialog.html',
                        controller: 'TrackingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Tracking', function(Tracking) {
                                return Tracking.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tracking', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('tracking.delete', {
                parent: 'tracking',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tracking/tracking-delete-dialog.html',
                        controller: 'TrackingDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Tracking', function(Tracking) {
                                return Tracking.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tracking', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
