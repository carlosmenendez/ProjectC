(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('SalasDetailController', SalasDetailController);

    SalasDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Salas', 'Cine'];

    function SalasDetailController($scope, $rootScope, $stateParams, previousState, entity, Salas, Cine) {
        var vm = this;

        vm.salas = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bCiNeApp:salasUpdate', function(event, result) {
            vm.salas = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
