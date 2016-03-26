'use strict';

angular.module('webstoreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('productAttr', {
                parent: 'entity',
                url: '/productAttrs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.productAttr.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/productAttr/productAttrs.html',
                        controller: 'ProductAttrController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('productAttr');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('productAttr.detail', {
                parent: 'entity',
                url: '/productAttr/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.productAttr.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/productAttr/productAttr-detail.html',
                        controller: 'ProductAttrDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('productAttr');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ProductAttr', function($stateParams, ProductAttr) {
                        return ProductAttr.get({id : $stateParams.id});
                    }]
                }
            })
            .state('productAttr.new', {
                parent: 'productAttr',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productAttr/productAttr-dialog.html',
                        controller: 'ProductAttrDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    value: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('productAttr', null, { reload: true });
                    }, function() {
                        $state.go('productAttr');
                    })
                }]
            })
            .state('productAttr.edit', {
                parent: 'productAttr',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productAttr/productAttr-dialog.html',
                        controller: 'ProductAttrDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ProductAttr', function(ProductAttr) {
                                return ProductAttr.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('productAttr', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('productAttr.delete', {
                parent: 'productAttr',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productAttr/productAttr-delete-dialog.html',
                        controller: 'ProductAttrDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ProductAttr', function(ProductAttr) {
                                return ProductAttr.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('productAttr', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
