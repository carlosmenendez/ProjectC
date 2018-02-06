(function() {
    'use strict';

    angular
        .module('bCiNeApp')
        .controller('ComentariosPeliDialogController', ComentariosPeliDialogController);

    ComentariosPeliDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ComentariosPeli', 'Pelicula', 'User'];

    function ComentariosPeliDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ComentariosPeli, Pelicula, User) {
        var vm = this;

        vm.comentariosPeli = entity;
        vm.clear = clear;
        vm.save = save;
        vm.peliculas = Pelicula.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.comentariosPeli.id !== null) {
                ComentariosPeli.update(vm.comentariosPeli, onSaveSuccess, onSaveError);
            } else {
                ComentariosPeli.save(vm.comentariosPeli, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bCiNeApp:comentariosPeliUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
