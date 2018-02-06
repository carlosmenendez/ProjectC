(function() {
    'use strict';
    angular
        .module('bCiNeApp')
        .factory('Cartelera', Cartelera);

    Cartelera.$inject = ['$resource', 'DateUtils'];

    function Cartelera ($resource, DateUtils) {
        var resourceUrl =  'api/carteleras/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dia = DateUtils.convertDateTimeFromServer(data.dia);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
