(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('CineDialogController', CineDialogController);

    CineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cine'];

    function CineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cine) {
        var vm = this;

        vm.cine = entity;
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
            if (vm.cine.id !== null) {
                Cine.update(vm.cine, onSaveSuccess, onSaveError);
            } else {
                Cine.save(vm.cine, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bCiNeApp:cineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
