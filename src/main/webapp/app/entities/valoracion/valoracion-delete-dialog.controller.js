(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ValoracionDeleteController',ValoracionDeleteController);

    ValoracionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Valoracion'];

    function ValoracionDeleteController($uibModalInstance, entity, Valoracion) {
        var vm = this;

        vm.valoracion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Valoracion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
