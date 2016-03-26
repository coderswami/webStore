'use strict';

angular.module('webstoreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userLogin', {
                parent: 'entity',
                url: '/userLogins',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.userLogin.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userLogin/userLogins.html',
                        controller: 'UserLoginController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userLogin');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userLogin.detail', {
                parent: 'entity',
                url: '/userLogin/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.userLogin.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userLogin/userLogin-detail.html',
                        controller: 'UserLoginDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userLogin');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'UserLogin', function($stateParams, UserLogin) {
                        return UserLogin.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userLogin.new', {
                parent: 'userLogin',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userLogin/userLogin-dialog.html',
                        controller: 'UserLoginDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    username: null,
                                    password: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userLogin', null, { reload: true });
                    }, function() {
                        $state.go('userLogin');
                    })
                }]
            })
            .state('userLogin.edit', {
                parent: 'userLogin',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userLogin/userLogin-dialog.html',
                        controller: 'UserLoginDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserLogin', function(UserLogin) {
                                return UserLogin.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userLogin', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('userLogin.delete', {
                parent: 'userLogin',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userLogin/userLogin-delete-dialog.html',
                        controller: 'UserLoginDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['UserLogin', function(UserLogin) {
                                return UserLogin.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userLogin', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
