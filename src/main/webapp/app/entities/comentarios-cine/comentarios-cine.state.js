(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('comentarios-cine', {
            parent: 'entity',
            url: '/comentarios-cine',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.comentariosCine.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comentarios-cine/comentarios-cines.html',
                    controller: 'ComentariosCineController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('comentariosCine');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('comentarios-cine-detail', {
            parent: 'comentarios-cine',
            url: '/comentarios-cine/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bCiNeApp.comentariosCine.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comentarios-cine/comentarios-cine-detail.html',
                    controller: 'ComentariosCineDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('comentariosCine');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ComentariosCine', function($stateParams, ComentariosCine) {
                    return ComentariosCine.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'comentarios-cine',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('comentarios-cine-detail.edit', {
            parent: 'comentarios-cine-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comentarios-cine/comentarios-cine-dialog.html',
                    controller: 'ComentariosCineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ComentariosCine', function(ComentariosCine) {
                            return ComentariosCine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('comentarios-cine.new', {
            parent: 'comentarios-cine',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comentarios-cine/comentarios-cine-dialog.html',
                    controller: 'ComentariosCineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                comentario: null,
                                usuario: null,
                                cine: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('comentarios-cine', null, { reload: 'comentarios-cine' });
                }, function() {
                    $state.go('comentarios-cine');
                });
            }]
        })
        .state('comentarios-cine.edit', {
            parent: 'comentarios-cine',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comentarios-cine/comentarios-cine-dialog.html',
                    controller: 'ComentariosCineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ComentariosCine', function(ComentariosCine) {
                            return ComentariosCine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comentarios-cine', null, { reload: 'comentarios-cine' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('comentarios-cine.delete', {
            parent: 'comentarios-cine',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comentarios-cine/comentarios-cine-delete-dialog.html',
                    controller: 'ComentariosCineDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ComentariosCine', function(ComentariosCine) {
                            return ComentariosCine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comentarios-cine', null, { reload: 'comentarios-cine' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
