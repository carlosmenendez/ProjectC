(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cine', {
            parent: 'entity',
            url: '/cine',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.cine.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cine/cines.html',
                    controller: 'CineController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cine');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cine-detail', {
            parent: 'cine',
            url: '/cine/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.cine.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cine/cine-detail.html',
                    controller: 'CineDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cine');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cine', function($stateParams, Cine) {
                    return Cine.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cine',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cine-detail.edit', {
            parent: 'cine-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cine/cine-dialog.html',
                    controller: 'CineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cine', function(Cine) {
                            return Cine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cine.new', {
            parent: 'cine',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cine/cine-dialog.html',
                    controller: 'CineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                direccion: null,
                                ciudad: null,
                                telefono: null,
                                numDeSalas: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cine', null, { reload: 'cine' });
                }, function() {
                    $state.go('cine');
                });
            }]
        })
        .state('cine.edit', {
            parent: 'cine',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cine/cine-dialog.html',
                    controller: 'CineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cine', function(Cine) {
                            return Cine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cine', null, { reload: 'cine' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cine.delete', {
            parent: 'cine',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cine/cine-delete-dialog.html',
                    controller: 'CineDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cine', function(Cine) {
                            return Cine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cine', null, { reload: 'cine' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
