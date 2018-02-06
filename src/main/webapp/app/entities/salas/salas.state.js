(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('salas', {
            parent: 'entity',
            url: '/salas',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.salas.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/salas/salas.html',
                    controller: 'SalasController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('salas');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('salas-detail', {
            parent: 'salas',
            url: '/salas/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.salas.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/salas/salas-detail.html',
                    controller: 'SalasDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('salas');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Salas', function($stateParams, Salas) {
                    return Salas.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'salas',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('salas-detail.edit', {
            parent: 'salas-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/salas/salas-dialog.html',
                    controller: 'SalasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Salas', function(Salas) {
                            return Salas.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('salas.new', {
            parent: 'salas',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/salas/salas-dialog.html',
                    controller: 'SalasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                denominacion: null,
                                aforo: null,
                                cine: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('salas', null, { reload: 'salas' });
                }, function() {
                    $state.go('salas');
                });
            }]
        })
        .state('salas.edit', {
            parent: 'salas',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/salas/salas-dialog.html',
                    controller: 'SalasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Salas', function(Salas) {
                            return Salas.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('salas', null, { reload: 'salas' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('salas.delete', {
            parent: 'salas',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/salas/salas-delete-dialog.html',
                    controller: 'SalasDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Salas', function(Salas) {
                            return Salas.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('salas', null, { reload: 'salas' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
