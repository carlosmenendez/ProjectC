(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cartelera', {
            parent: 'entity',
            url: '/cartelera',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.cartelera.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cartelera/carteleras.html',
                    controller: 'CarteleraController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cartelera');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cartelera-detail', {
            parent: 'cartelera',
            url: '/cartelera/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.cartelera.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cartelera/cartelera-detail.html',
                    controller: 'CarteleraDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cartelera');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cartelera', function($stateParams, Cartelera) {
                    return Cartelera.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cartelera',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cartelera-detail.edit', {
            parent: 'cartelera-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cartelera/cartelera-dialog.html',
                    controller: 'CarteleraDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cartelera', function(Cartelera) {
                            return Cartelera.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cartelera.new', {
            parent: 'cartelera',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cartelera/cartelera-dialog.html',
                    controller: 'CarteleraDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dia: null,
                                cine: null,
                                pelicula: null,
                                sala: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cartelera', null, { reload: 'cartelera' });
                }, function() {
                    $state.go('cartelera');
                });
            }]
        })
        .state('cartelera.edit', {
            parent: 'cartelera',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cartelera/cartelera-dialog.html',
                    controller: 'CarteleraDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cartelera', function(Cartelera) {
                            return Cartelera.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cartelera', null, { reload: 'cartelera' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cartelera.delete', {
            parent: 'cartelera',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cartelera/cartelera-delete-dialog.html',
                    controller: 'CarteleraDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cartelera', function(Cartelera) {
                            return Cartelera.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cartelera', null, { reload: 'cartelera' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
