(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ComentariosPeliDetailController', ComentariosPeliDetailController);

    ComentariosPeliDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ComentariosPeli', 'Pelicula', 'User'];

    function ComentariosPeliDetailController($scope, $rootScope, $stateParams, previousState, entity, ComentariosPeli, Pelicula, User) {
        var vm = this;

        vm.comentariosPeli = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bCiNeApp:comentariosPeliUpdate', function(event, result) {
            vm.comentariosPeli = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
