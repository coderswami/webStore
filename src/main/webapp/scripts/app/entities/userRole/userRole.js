'use strict';

angular.module('webstoreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userRole', {
                parent: 'entity',
                url: '/userRoles',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.userRole.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userRole/userRoles.html',
                        controller: 'UserRoleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userRole');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userRole.detail', {
                parent: 'entity',
                url: '/userRole/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.userRole.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userRole/userRole-detail.html',
                        controller: 'UserRoleDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userRole');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'UserRole', function($stateParams, UserRole) {
                        return UserRole.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userRole.new', {
                parent: 'userRole',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userRole/userRole-dialog.html',
                        controller: 'UserRoleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    role: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userRole', null, { reload: true });
                    }, function() {
                        $state.go('userRole');
                    })
                }]
            })
            .state('userRole.edit', {
                parent: 'userRole',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userRole/userRole-dialog.html',
                        controller: 'UserRoleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserRole', function(UserRole) {
                                return UserRole.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userRole', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('userRole.delete', {
                parent: 'userRole',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userRole/userRole-delete-dialog.html',
                        controller: 'UserRoleDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['UserRole', function(UserRole) {
                                return UserRole.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userRole', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
