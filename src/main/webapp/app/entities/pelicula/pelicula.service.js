(function() {
    'use strict';
    angular
        .module('bCiNeApp')
        .factory('Pelicula', Pelicula);

    Pelicula.$inject = ['$resource', 'DateUtils'];

    function Pelicula ($resource, DateUtils) {
        var resourceUrl =  'api/peliculas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fechaDeEstreno = DateUtils.convertDateTimeFromServer(data.fechaDeEstreno);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
