(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ComentariosCineDeleteController',ComentariosCineDeleteController);

    ComentariosCineDeleteController.$inject = ['$uibModalInstance', 'entity', 'ComentariosCine'];

    function ComentariosCineDeleteController($uibModalInstance, entity, ComentariosCine) {
        var vm = this;

        vm.comentariosCine = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ComentariosCine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
