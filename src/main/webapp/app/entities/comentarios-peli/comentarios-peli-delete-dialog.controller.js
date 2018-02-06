(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ComentariosPeliDeleteController',ComentariosPeliDeleteController);

    ComentariosPeliDeleteController.$inject = ['$uibModalInstance', 'entity', 'ComentariosPeli'];

    function ComentariosPeliDeleteController($uibModalInstance, entity, ComentariosPeli) {
        var vm = this;

        vm.comentariosPeli = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ComentariosPeli.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
