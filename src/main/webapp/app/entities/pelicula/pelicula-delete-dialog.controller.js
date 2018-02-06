(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('PeliculaDeleteController',PeliculaDeleteController);

    PeliculaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pelicula'];

    function PeliculaDeleteController($uibModalInstance, entity, Pelicula) {
        var vm = this;

        vm.pelicula = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Pelicula.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
