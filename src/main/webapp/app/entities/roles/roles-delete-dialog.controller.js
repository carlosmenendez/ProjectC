(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('RolesDeleteController',RolesDeleteController);

    RolesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Roles'];

    function RolesDeleteController($uibModalInstance, entity, Roles) {
        var vm = this;

        vm.roles = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Roles.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
