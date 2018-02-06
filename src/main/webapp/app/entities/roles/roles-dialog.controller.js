(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('RolesDialogController', RolesDialogController);

    RolesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Roles'];

    function RolesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Roles) {
        var vm = this;

        vm.roles = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.roles.id !== null) {
                Roles.update(vm.roles, onSaveSuccess, onSaveError);
            } else {
                Roles.save(vm.roles, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bCiNeApp:rolesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
