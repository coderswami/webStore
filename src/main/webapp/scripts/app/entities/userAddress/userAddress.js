'use strict';

angular.module('webstoreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userAddress', {
                parent: 'entity',
                url: '/userAddresss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.userAddress.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userAddress/userAddresss.html',
                        controller: 'UserAddressController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userAddress');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userAddress.detail', {
                parent: 'entity',
                url: '/userAddress/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.userAddress.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userAddress/userAddress-detail.html',
                        controller: 'UserAddressDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userAddress');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'UserAddress', function($stateParams, UserAddress) {
                        return UserAddress.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userAddress.new', {
                parent: 'userAddress',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userAddress/userAddress-dialog.html',
                        controller: 'UserAddressDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    streetAddress: null,
                                    landmark: null,
                                    city: null,
                                    pin: null,
                                    phone: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userAddress', null, { reload: true });
                    }, function() {
                        $state.go('userAddress');
                    })
                }]
            })
            .state('userAddress.edit', {
                parent: 'userAddress',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userAddress/userAddress-dialog.html',
                        controller: 'UserAddressDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserAddress', function(UserAddress) {
                                return UserAddress.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userAddress', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('userAddress.delete', {
                parent: 'userAddress',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userAddress/userAddress-delete-dialog.html',
                        controller: 'UserAddressDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['UserAddress', function(UserAddress) {
                                return UserAddress.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userAddress', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
