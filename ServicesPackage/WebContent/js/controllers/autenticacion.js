/**
 * Autor: Carlos Guerra
*/
// CONTROLADORES---------------------------------------------------------------------------------------------
app.controller('autenticacionCtrl',   ['autenticationService','$http','$window','$rootScope', '$scope', autenticacionCtrl] );
/**
 * Controlador de página index.html
 */
function autenticacionCtrl(autenticationService,$http,$window, $rootScope, $scope){
	var vm = this;
    
	vm.getAutentication = function(){
    	autenticationService.autenticacion(vm);
    }
    //BOTONES
    vm.procesarLogin = function(){
    	autenticationService.login(vm, vm.login, vm.password);
    }
    vm.procesarLogout = function(){
    	autenticationService.logout(vm);
    }
    vm.procesarIrRegistro = function(){
    	 $window.location.href =servidor+'/register.html';
    }
    
    // CALLBACKS
    vm.callBackLoginOK = function(data, status, headers, config){
    	if(status==200){
    		$window.location.href =servidor+'/privado.html';
    	}
    }
    vm.callBackLoginKO = function(data, status, headers, config){
    	if (status==400){
    		alert("Error de login/password.");
 		}else if(status==500){
 			alert("Ha ocurrido un error en el servidor. Intentelo de nuevo más tarde.");
 		}
    	$window.location.href = servidor+'/index.html';
    }
    vm.callBackLogoutOK = function(data, status, headers, config){
    	if(status==200){
    		$window.location.href =servidor+'/index.html';
    	}
    }
    vm.callBackLogoutKO = function(data, status, headers, config){
 		if(status==500){
 			alert("Ha ocurrido un error en el servidor. Intentelo de nuevo más tarde.");
 		}
    }
    vm.callBackAutenticacionOK = function(data, status, headers, config){
 		if(status==200){
 			vm.authName = data.name;
 	    	vm.authRol = data.rol;
 	    	vm.authRolId = data.rol_id;
 		} else if(status==204){
 			vm.authName = null;
 	    	vm.authRol = "Anonimo";
 	    	vm.authRolId = 6;//Anonimo 
 		}

 		if ((vm.authRolId<=1)&&($rootScope.vmUsers!=undefined))$rootScope.vmUsers.initData(data.rol_id);
 		if ((vm.authRolId<=2)&&($rootScope.vmMerchants!=undefined))$rootScope.vmMerchants.initData(data.rol_id);
 		if ((vm.authRolId<=3)&&($rootScope.vmHeadquarters!=undefined))$rootScope.vmHeadquarters.initData(data.rol_id);
 		if ((vm.authRolId<=4)&&($rootScope.vmProducts!=undefined))$rootScope.vmProducts.initData(data.rol_id);
    }
    vm.callBackAutenticacionKO = function(data, status, headers, config){
    	vm.authName = null;
    	vm.authRol = null;
    	vm.authRolId = null;
 		if(status==500){
 			alert("Ha ocurrido un error en el servidor. Intentelo de nuevo más tarde.");
 		}
    }
    vm.irAPrivado = function(){
    	$window.location.href =servidor+'/privado.html';
    } 
    this.getAutentication(this);
    $rootScope.vmAutentication = vm;
    return vm;
};

