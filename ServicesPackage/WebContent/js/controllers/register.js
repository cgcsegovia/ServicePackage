/**
* Autor: Carlos Guerra
*/
// CONTROLADORES---------------------------------------------------------------------------------------------
app.controller( 'registroCtrl', ["usersService","$http","$window",registerCtrl] );
/**
 * Controlador de página register.html
 */
function registerCtrl(usersService,$http,$window){
	var vm = this;
	vm.procesarGuardar = function(){
   	 	usersService.putUserPublic(vm.name,vm.surname,vm.email,vm.login,vm.password);
   	 	$window.location.href = servidor + '/index.html';
	}
	return vm;
}
