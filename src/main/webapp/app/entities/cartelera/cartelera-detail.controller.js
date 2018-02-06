(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('CarteleraDetailController', CarteleraDetailController);

    CarteleraDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cartelera', 'Pelicula', 'Cine'];

    function CarteleraDetailController($scope, $rootScope, $stateParams, previousState, entity, Cartelera, Pelicula, Cine) {
        var vm = this;

        vm.cartelera = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bCiNeApp:carteleraUpdate', function(event, result) {
            vm.cartelera = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
