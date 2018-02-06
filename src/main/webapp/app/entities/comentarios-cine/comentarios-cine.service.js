(function() {
    'use strict';
    angular
        .module('bCiNeApp')
        .factory('ComentariosCine', ComentariosCine);

    ComentariosCine.$inject = ['$resource'];

    function ComentariosCine ($resource) {
        var resourceUrl =  'api/comentarios-cines/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
