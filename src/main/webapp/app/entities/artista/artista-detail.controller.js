(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ArtistaDetailController', ArtistaDetailController);

    ArtistaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Artista'];

    function ArtistaDetailController($scope, $rootScope, $stateParams, previousState, entity, Artista) {
        var vm = this;

        vm.artista = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bCiNeApp:artistaUpdate', function(event, result) {
            vm.artista = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
