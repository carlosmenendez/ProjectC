(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pelicula', {
            parent: 'entity',
            url: '/pelicula',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.pelicula.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pelicula/peliculas.html',
                    controller: 'PeliculaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pelicula');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pelicula-detail', {
            parent: 'pelicula',
            url: '/pelicula/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.pelicula.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pelicula/pelicula-detail.html',
                    controller: 'PeliculaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pelicula');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pelicula', function($stateParams, Pelicula) {
                    return Pelicula.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'pelicula',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('pelicula-detail.edit', {
            parent: 'pelicula-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pelicula/pelicula-dialog.html',
                    controller: 'PeliculaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pelicula', function(Pelicula) {
                            return Pelicula.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pelicula.new', {
            parent: 'pelicula',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pelicula/pelicula-dialog.html',
                    controller: 'PeliculaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                pais: null,
                                duracion: null,
                                fechaDeEstreno: null,
                                genero: null,
                                esEstreno: null,
                                sinopsis: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pelicula', null, { reload: 'pelicula' });
                }, function() {
                    $state.go('pelicula');
                });
            }]
        })
        .state('pelicula.edit', {
            parent: 'pelicula',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pelicula/pelicula-dialog.html',
                    controller: 'PeliculaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pelicula', function(Pelicula) {
                            return Pelicula.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pelicula', null, { reload: 'pelicula' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pelicula.delete', {
            parent: 'pelicula',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pelicula/pelicula-delete-dialog.html',
                    controller: 'PeliculaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pelicula', function(Pelicula) {
                            return Pelicula.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pelicula', null, { reload: 'pelicula' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
