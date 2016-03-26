'use strict';

angular.module('webstoreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('productPrice', {
                parent: 'entity',
                url: '/productPrices',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.productPrice.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/productPrice/productPrices.html',
                        controller: 'ProductPriceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('productPrice');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('productPrice.detail', {
                parent: 'entity',
                url: '/productPrice/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.productPrice.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/productPrice/productPrice-detail.html',
                        controller: 'ProductPriceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('productPrice');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ProductPrice', function($stateParams, ProductPrice) {
                        return ProductPrice.get({id : $stateParams.id});
                    }]
                }
            })
            .state('productPrice.new', {
                parent: 'productPrice',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productPrice/productPrice-dialog.html',
                        controller: 'ProductPriceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    listPrice: null,
                                    discount: null,
                                    salesPrice: null,
                                    active: false,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('productPrice', null, { reload: true });
                    }, function() {
                        $state.go('productPrice');
                    })
                }]
            })
            .state('productPrice.edit', {
                parent: 'productPrice',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productPrice/productPrice-dialog.html',
                        controller: 'ProductPriceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ProductPrice', function(ProductPrice) {
                                return ProductPrice.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('productPrice', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('productPrice.delete', {
                parent: 'productPrice',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productPrice/productPrice-delete-dialog.html',
                        controller: 'ProductPriceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ProductPrice', function(ProductPrice) {
                                return ProductPrice.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('productPrice', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
