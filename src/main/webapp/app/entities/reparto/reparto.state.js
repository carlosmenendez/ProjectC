(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reparto', {
            parent: 'entity',
            url: '/reparto',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.reparto.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reparto/repartos.html',
                    controller: 'RepartoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reparto');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('reparto-detail', {
            parent: 'reparto',
            url: '/reparto/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.reparto.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reparto/reparto-detail.html',
                    controller: 'RepartoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reparto');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Reparto', function($stateParams, Reparto) {
                    return Reparto.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'reparto',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('reparto-detail.edit', {
            parent: 'reparto-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reparto/reparto-dialog.html',
                    controller: 'RepartoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Reparto', function(Reparto) {
                            return Reparto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reparto.new', {
            parent: 'reparto',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reparto/reparto-dialog.html',
                    controller: 'RepartoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombreArtista: null,
                                rol: null,
                                descripcion: null,
                                pelicula: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('reparto', null, { reload: 'reparto' });
                }, function() {
                    $state.go('reparto');
                });
            }]
        })
        .state('reparto.edit', {
            parent: 'reparto',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reparto/reparto-dialog.html',
                    controller: 'RepartoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Reparto', function(Reparto) {
                            return Reparto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reparto', null, { reload: 'reparto' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reparto.delete', {
            parent: 'reparto',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reparto/reparto-delete-dialog.html',
                    controller: 'RepartoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Reparto', function(Reparto) {
                            return Reparto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reparto', null, { reload: 'reparto' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
