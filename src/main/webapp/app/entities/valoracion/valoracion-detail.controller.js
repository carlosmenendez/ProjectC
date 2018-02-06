(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ValoracionDetailController', ValoracionDetailController);

    ValoracionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Valoracion', 'User', 'Pelicula'];

    function ValoracionDetailController($scope, $rootScope, $stateParams, previousState, entity, Valoracion, User, Pelicula) {
        var vm = this;

        vm.valoracion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bCiNeApp:valoracionUpdate', function(event, result) {
            vm.valoracion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
