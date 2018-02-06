(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('valoracion', {
            parent: 'entity',
            url: '/valoracion',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.valoracion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/valoracion/valoracions.html',
                    controller: 'ValoracionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('valoracion');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('valoracion-detail', {
            parent: 'valoracion',
            url: '/valoracion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.valoracion.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/valoracion/valoracion-detail.html',
                    controller: 'ValoracionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('valoracion');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Valoracion', function($stateParams, Valoracion) {
                    return Valoracion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'valoracion',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('valoracion-detail.edit', {
            parent: 'valoracion-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/valoracion/valoracion-dialog.html',
                    controller: 'ValoracionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Valoracion', function(Valoracion) {
                            return Valoracion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('valoracion.new', {
            parent: 'valoracion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/valoracion/valoracion-dialog.html',
                    controller: 'ValoracionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nota: null,
                                usuarioVotado: null,
                                peliculaVotada: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('valoracion', null, { reload: 'valoracion' });
                }, function() {
                    $state.go('valoracion');
                });
            }]
        })
        .state('valoracion.edit', {
            parent: 'valoracion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/valoracion/valoracion-dialog.html',
                    controller: 'ValoracionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Valoracion', function(Valoracion) {
                            return Valoracion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('valoracion', null, { reload: 'valoracion' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('valoracion.delete', {
            parent: 'valoracion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/valoracion/valoracion-delete-dialog.html',
                    controller: 'ValoracionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Valoracion', function(Valoracion) {
                            return Valoracion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('valoracion', null, { reload: 'valoracion' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
