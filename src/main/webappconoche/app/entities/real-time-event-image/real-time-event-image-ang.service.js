(function() {
    'use strict';
    angular
        .module('conocheApp')
        .factory('RealTimeEventImage', RealTimeEventImage);

    RealTimeEventImage.$inject = ['$resource', 'DateUtils'];

    function RealTimeEventImage ($resource, DateUtils) {
        var resourceUrl =  'api/real-time-event-images/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationTime = DateUtils.convertDateTimeFromServer(data.creationTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'eventRealTimeImages' : {
                method: 'GET',
                isArray: true,
                url: 'api/real-time-event-images/event/:idEvent'
            },
            'eventRealTimeImagesInLastHours' : {
                method: 'GET',
                isArray: true,
                url: 'api/real-time-event-images/event/:idEvent/:hours'
            },
        });
    }
})();
