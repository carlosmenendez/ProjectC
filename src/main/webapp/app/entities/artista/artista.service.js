(function() {
    'use strict';
    angular
        .module('bCiNeApp')
        .factory('Artista', Artista);

    Artista.$inject = ['$resource', 'DateUtils'];

    function Artista ($resource, DateUtils) {
        var resourceUrl =  'api/artistas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fechaNacimiento = DateUtils.convertDateTimeFromServer(data.fechaNacimiento);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
