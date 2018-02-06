(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('CineDeleteController',CineDeleteController);

    CineDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cine'];

    function CineDeleteController($uibModalInstance, entity, Cine) {
        var vm = this;

        vm.cine = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
