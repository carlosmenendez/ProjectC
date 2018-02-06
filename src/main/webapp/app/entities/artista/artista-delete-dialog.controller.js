(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ArtistaDeleteController',ArtistaDeleteController);

    ArtistaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Artista'];

    function ArtistaDeleteController($uibModalInstance, entity, Artista) {
        var vm = this;

        vm.artista = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Artista.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
