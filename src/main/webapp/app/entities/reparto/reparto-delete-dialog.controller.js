(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('RepartoDeleteController',RepartoDeleteController);

    RepartoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Reparto'];

    function RepartoDeleteController($uibModalInstance, entity, Reparto) {
        var vm = this;

        vm.reparto = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Reparto.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
