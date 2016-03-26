'use strict';

angular.module('webstoreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('catalog', {
                parent: 'entity',
                url: '/catalogs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.catalog.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/catalog/catalogs.html',
                        controller: 'CatalogController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('catalog');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('catalog.detail', {
                parent: 'entity',
                url: '/catalog/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.catalog.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/catalog/catalog-detail.html',
                        controller: 'CatalogDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('catalog');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Catalog', function($stateParams, Catalog) {
                        return Catalog.get({id : $stateParams.id});
                    }]
                }
            })
            .state('catalog.new', {
                parent: 'catalog',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/catalog/catalog-dialog.html',
                        controller: 'CatalogDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    active: false,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('catalog', null, { reload: true });
                    }, function() {
                        $state.go('catalog');
                    })
                }]
            })
            .state('catalog.edit', {
                parent: 'catalog',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/catalog/catalog-dialog.html',
                        controller: 'CatalogDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Catalog', function(Catalog) {
                                return Catalog.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('catalog', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('catalog.delete', {
                parent: 'catalog',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/catalog/catalog-delete-dialog.html',
                        controller: 'CatalogDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Catalog', function(Catalog) {
                                return Catalog.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('catalog', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
