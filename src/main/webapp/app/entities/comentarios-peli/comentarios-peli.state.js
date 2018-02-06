(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('comentarios-peli', {
            parent: 'entity',
            url: '/comentarios-peli',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.comentariosPeli.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comentarios-peli/comentarios-pelis.html',
                    controller: 'ComentariosPeliController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('comentariosPeli');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('comentarios-peli-detail', {
            parent: 'comentarios-peli',
            url: '/comentarios-peli/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.comentariosPeli.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comentarios-peli/comentarios-peli-detail.html',
                    controller: 'ComentariosPeliDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('comentariosPeli');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ComentariosPeli', function($stateParams, ComentariosPeli) {
                    return ComentariosPeli.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'comentarios-peli',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('comentarios-peli-detail.edit', {
            parent: 'comentarios-peli-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comentarios-peli/comentarios-peli-dialog.html',
                    controller: 'ComentariosPeliDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ComentariosPeli', function(ComentariosPeli) {
                            return ComentariosPeli.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('comentarios-peli.new', {
            parent: 'comentarios-peli',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comentarios-peli/comentarios-peli-dialog.html',
                    controller: 'ComentariosPeliDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                comentario: null,
                                usuario: null,
                                peli: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('comentarios-peli', null, { reload: 'comentarios-peli' });
                }, function() {
                    $state.go('comentarios-peli');
                });
            }]
        })
        .state('comentarios-peli.edit', {
            parent: 'comentarios-peli',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comentarios-peli/comentarios-peli-dialog.html',
                    controller: 'ComentariosPeliDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ComentariosPeli', function(ComentariosPeli) {
                            return ComentariosPeli.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comentarios-peli', null, { reload: 'comentarios-peli' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('comentarios-peli.delete', {
            parent: 'comentarios-peli',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comentarios-peli/comentarios-peli-delete-dialog.html',
                    controller: 'ComentariosPeliDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ComentariosPeli', function(ComentariosPeli) {
                            return ComentariosPeli.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comentarios-peli', null, { reload: 'comentarios-peli' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
