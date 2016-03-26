'use strict';

angular.module('webstoreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('state', {
                parent: 'entity',
                url: '/states',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.state.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/state/states.html',
                        controller: 'StateController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('state');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('state.detail', {
                parent: 'entity',
                url: '/state/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'webstoreApp.state.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/state/state-detail.html',
                        controller: 'StateDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('state');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'State', function($stateParams, State) {
                        return State.get({id : $stateParams.id});
                    }]
                }
            })
            .state('state.new', {
                parent: 'state',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/state/state-dialog.html',
                        controller: 'StateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    code: null,
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('state', null, { reload: true });
                    }, function() {
                        $state.go('state');
                    })
                }]
            })
            .state('state.edit', {
                parent: 'state',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/state/state-dialog.html',
                        controller: 'StateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['State', function(State) {
                                return State.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('state', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('state.delete', {
                parent: 'state',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/state/state-delete-dialog.html',
                        controller: 'StateDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['State', function(State) {
                                return State.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('state', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
