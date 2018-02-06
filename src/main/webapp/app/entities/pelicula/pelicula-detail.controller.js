(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('PeliculaDetailController', PeliculaDetailController);

    PeliculaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pelicula'];

    function PeliculaDetailController($scope, $rootScope, $stateParams, previousState, entity, Pelicula) {
        var vm = this;

        vm.pelicula = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bCiNeApp:peliculaUpdate', function(event, result) {
            vm.pelicula = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
