'use strict';

angular.module('webstoreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('orderHeader', {
                parent: 'entity',
                url: '/orderHeaders',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.orderHeader.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/orderHeader/orderHeaders.html',
                        controller: 'OrderHeaderController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('orderHeader');
                        $translatePartialLoader.addPart('orderType');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('orderHeader.detail', {
                parent: 'entity',
                url: '/orderHeader/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.orderHeader.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/orderHeader/orderHeader-detail.html',
                        controller: 'OrderHeaderDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('orderHeader');
                        $translatePartialLoader.addPart('orderType');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'OrderHeader', function($stateParams, OrderHeader) {
                        return OrderHeader.get({id : $stateParams.id});
                    }]
                }
            })
            .state('orderHeader.new', {
                parent: 'orderHeader',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/orderHeader/orderHeader-dialog.html',
                        controller: 'OrderHeaderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    type: null,
                                    status: null,
                                    orderTotal: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('orderHeader', null, { reload: true });
                    }, function() {
                        $state.go('orderHeader');
                    })
                }]
            })
            .state('orderHeader.edit', {
                parent: 'orderHeader',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/orderHeader/orderHeader-dialog.html',
                        controller: 'OrderHeaderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['OrderHeader', function(OrderHeader) {
                                return OrderHeader.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('orderHeader', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('orderHeader.delete', {
                parent: 'orderHeader',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/orderHeader/orderHeader-delete-dialog.html',
                        controller: 'OrderHeaderDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['OrderHeader', function(OrderHeader) {
                                return OrderHeader.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('orderHeader', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
