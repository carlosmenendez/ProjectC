(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ComentariosCineDetailController', ComentariosCineDetailController);

    ComentariosCineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ComentariosCine', 'Cine', 'User'];

    function ComentariosCineDetailController($scope, $rootScope, $stateParams, previousState, entity, ComentariosCine, Cine, User) {
        var vm = this;

        vm.comentariosCine = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bCiNeApp:comentariosCineUpdate', function(event, result) {
            vm.comentariosCine = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
