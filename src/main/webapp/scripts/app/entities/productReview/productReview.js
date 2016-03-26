'use strict';

angular.module('webstoreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('productReview', {
                parent: 'entity',
                url: '/productReviews',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.productReview.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/productReview/productReviews.html',
                        controller: 'ProductReviewController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('productReview');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('productReview.detail', {
                parent: 'entity',
                url: '/productReview/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.productReview.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/productReview/productReview-detail.html',
                        controller: 'ProductReviewDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('productReview');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ProductReview', function($stateParams, ProductReview) {
                        return ProductReview.get({id : $stateParams.id});
                    }]
                }
            })
            .state('productReview.new', {
                parent: 'productReview',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productReview/productReview-dialog.html',
                        controller: 'ProductReviewDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    description: null,
                                    rating: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('productReview', null, { reload: true });
                    }, function() {
                        $state.go('productReview');
                    })
                }]
            })
            .state('productReview.edit', {
                parent: 'productReview',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productReview/productReview-dialog.html',
                        controller: 'ProductReviewDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ProductReview', function(ProductReview) {
                                return ProductReview.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('productReview', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('productReview.delete', {
                parent: 'productReview',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productReview/productReview-delete-dialog.html',
                        controller: 'ProductReviewDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ProductReview', function(ProductReview) {
                                return ProductReview.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('productReview', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
