'use strict';

angular.module('webstoreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('shipment', {
                parent: 'entity',
                url: '/shipments',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.shipment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/shipment/shipments.html',
                        controller: 'ShipmentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('shipment');
                        $translatePartialLoader.addPart('shipmentType');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('shipment.detail', {
                parent: 'entity',
                url: '/shipment/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.shipment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/shipment/shipment-detail.html',
                        controller: 'ShipmentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('shipment');
                        $translatePartialLoader.addPart('shipmentType');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Shipment', function($stateParams, Shipment) {
                        return Shipment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('shipment.new', {
                parent: 'shipment',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/shipment/shipment-dialog.html',
                        controller: 'ShipmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    type: null,
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('shipment', null, { reload: true });
                    }, function() {
                        $state.go('shipment');
                    })
                }]
            })
            .state('shipment.edit', {
                parent: 'shipment',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/shipment/shipment-dialog.html',
                        controller: 'ShipmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Shipment', function(Shipment) {
                                return Shipment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('shipment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('shipment.delete', {
                parent: 'shipment',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/shipment/shipment-delete-dialog.html',
                        controller: 'ShipmentDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Shipment', function(Shipment) {
                                return Shipment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('shipment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
