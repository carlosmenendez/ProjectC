(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('CarteleraDeleteController',CarteleraDeleteController);

    CarteleraDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cartelera'];

    function CarteleraDeleteController($uibModalInstance, entity, Cartelera) {
        var vm = this;

        vm.cartelera = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cartelera.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
