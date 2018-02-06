(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('artista', {
            parent: 'entity',
            url: '/artista',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.artista.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/artista/artistas.html',
                    controller: 'ArtistaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('artista');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('artista-detail', {
            parent: 'artista',
            url: '/artista/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.artista.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/artista/artista-detail.html',
                    controller: 'ArtistaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('artista');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Artista', function($stateParams, Artista) {
                    return Artista.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'artista',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('artista-detail.edit', {
            parent: 'artista-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artista/artista-dialog.html',
                    controller: 'ArtistaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Artista', function(Artista) {
                            return Artista.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('artista.new', {
            parent: 'artista',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artista/artista-dialog.html',
                    controller: 'ArtistaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombreCompleto: null,
                                fechaNacimiento: null,
                                esDirector: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('artista', null, { reload: 'artista' });
                }, function() {
                    $state.go('artista');
                });
            }]
        })
        .state('artista.edit', {
            parent: 'artista',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artista/artista-dialog.html',
                    controller: 'ArtistaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Artista', function(Artista) {
                            return Artista.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('artista', null, { reload: 'artista' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('artista.delete', {
            parent: 'artista',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artista/artista-delete-dialog.html',
                    controller: 'ArtistaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Artista', function(Artista) {
                            return Artista.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('artista', null, { reload: 'artista' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
