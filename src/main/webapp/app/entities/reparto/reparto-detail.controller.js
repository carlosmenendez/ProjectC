(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('RepartoDetailController', RepartoDetailController);

    RepartoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Reparto', 'Artista', 'Pelicula', 'Roles'];

    function RepartoDetailController($scope, $rootScope, $stateParams, previousState, entity, Reparto, Artista, Pelicula, Roles) {
        var vm = this;

        vm.reparto = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bCiNeApp:repartoUpdate', function(event, result) {
            vm.reparto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
