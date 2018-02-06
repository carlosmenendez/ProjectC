(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('SalasDeleteController',SalasDeleteController);

    SalasDeleteController.$inject = ['$uibModalInstance', 'entity', 'Salas'];

    function SalasDeleteController($uibModalInstance, entity, Salas) {
        var vm = this;

        vm.salas = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Salas.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
