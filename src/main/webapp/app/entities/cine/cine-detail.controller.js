(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('CineDetailController', CineDetailController);

    CineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cine'];

    function CineDetailController($scope, $rootScope, $stateParams, previousState, entity, Cine) {
        var vm = this;

        vm.cine = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bCiNeApp:cineUpdate', function(event, result) {
            vm.cine = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
