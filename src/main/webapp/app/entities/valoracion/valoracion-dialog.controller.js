(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ValoracionDialogController', ValoracionDialogController);

    ValoracionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Valoracion', 'User', 'Pelicula'];

    function ValoracionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Valoracion, User, Pelicula) {
        var vm = this;

        vm.valoracion = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.peliculas = Pelicula.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.valoracion.id !== null) {
                Valoracion.update(vm.valoracion, onSaveSuccess, onSaveError);
            } else {
                Valoracion.save(vm.valoracion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bCiNeApp:valoracionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
