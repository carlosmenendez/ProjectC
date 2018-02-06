(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('roles', {
            parent: 'entity',
            url: '/roles',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.roles.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/roles/roles.html',
                    controller: 'RolesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('roles');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('roles-detail', {
            parent: 'roles',
            url: '/roles/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.roles.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/roles/roles-detail.html',
                    controller: 'RolesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('roles');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Roles', function($stateParams, Roles) {
                    return Roles.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'roles',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('roles-detail.edit', {
            parent: 'roles-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/roles/roles-dialog.html',
                    controller: 'RolesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Roles', function(Roles) {
                            return Roles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('roles.new', {
            parent: 'roles',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/roles/roles-dialog.html',
                    controller: 'RolesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombreRol: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('roles', null, { reload: 'roles' });
                }, function() {
                    $state.go('roles');
                });
            }]
        })
        .state('roles.edit', {
            parent: 'roles',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/roles/roles-dialog.html',
                    controller: 'RolesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Roles', function(Roles) {
                            return Roles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('roles', null, { reload: 'roles' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('roles.delete', {
            parent: 'roles',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/roles/roles-delete-dialog.html',
                    controller: 'RolesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Roles', function(Roles) {
                            return Roles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('roles', null, { reload: 'roles' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
