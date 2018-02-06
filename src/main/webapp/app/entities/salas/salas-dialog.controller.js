(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('SalasDialogController', SalasDialogController);

    SalasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Salas', 'Cine'];

    function SalasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Salas, Cine) {
        var vm = this;

        vm.salas = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cines = Cine.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.salas.id !== null) {
                Salas.update(vm.salas, onSaveSuccess, onSaveError);
            } else {
                Salas.save(vm.salas, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bCiNeApp:salasUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
