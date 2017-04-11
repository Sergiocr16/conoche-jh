(function() {
    'use strict';

    angular
        .module('conocheApp')
        .controller('RatingLocalAngDialogController', RatingLocalAngDialogController);

    RatingLocalAngDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RatingLocal', 'User', 'Local','Principal'];

    function RatingLocalAngDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RatingLocal, User, Local,Principal) {
        var vm = this;

        vm.ratingLocal = entity;

        Principal.identity().then(function(user){
        console.log(user.login);
        console.log($stateParams.id)
        RatingLocal.getByUserAndLocal({localId: $stateParams.id,userLogin:user.login}).$promise.then(function(data){
        vm.ratingLocal = data;
        $('#field_description').focus();
        vm.paintStars(data.rating-1)
        })
        })

        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.locals = Local.query();

        populateStars();
        function populateStars(){
        vm.stars = [];
        for(var i = 0;i<5;i++){
            vm.stars.push({paint:0})
        }
        }

        function defineLabelColor(index){
        switch((index+1)){
            case 1:
            vm.labelColor = "label-danger";
            break;
            case 2:
            vm.labelColor = "label-warning";
            break;
            case 3:
            vm.labelColor = "label-primary";
            break;
            case 4:
            vm.labelColor = "label-almost-good";
            break;
            case 5:
            vm.labelColor = "label-success";
            break;
        }
        }
        vm.paintStars = function(index){
        vm.calification = (index+1);
        for(var i = 0;i<5;i++){
                    vm.stars[i].paint = 0;
         }
         for(var i = index;i >=0; i--){
          vm.stars[i].paint = 1;
         }
         vm.qualityText = "conocheApp.ratingLocal.califications."+(index+1);
         defineLabelColor(index);
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

       vm.paintStars(2);
        function save () {
        Principal.identity().then(function(user){
            vm.isSaving = true;
           vm.ratingLocal.rating = vm.calification;
           vm.ratingLocal.localId = $stateParams.id;
           vm.ratingLocal.userLogin = user.login;
           vm.ratingLocal.userDetailsId = 1;
           vm.ratingLocal.creationDate = moment(new Date()).format();
            if (vm.ratingLocal.id !== null) {
                RatingLocal.update(vm.ratingLocal, onSaveSuccess, onSaveError);
            } else {
                RatingLocal.save(vm.ratingLocal, onSaveSuccess, onSaveError);
            }

        })

        }

        function onSaveSuccess (result) {
            $scope.$emit('conocheApp:ratingLocalUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
