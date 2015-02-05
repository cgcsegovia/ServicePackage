/**
 * Autor: Carlos Guerra
 */
// DIRECTIVAS-------------------------------------------------------------------------------------------------
app.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);
// CONTROLADORES---------------------------------------------------------------------------------------------
// Administración

app.controller( 'modalBorrarCtrl', [ '$scope', '$modalInstance',modalBorrarCtrl] );
app.controller( 'aUsersCtrl', ["rolesService", "usersService", "productsService", "headquartersService", "merchantsService", "$http","$window", "$rootScope", "$scope", '$modal', aUsersCtrl] );
app.controller( 'aProductsCtrl', ["productsService", "headquartersService","fileUploadService", "deleteService", "$http","$window", "$rootScope","$scope", '$modal',aProductsCtrl] );
app.controller( 'aHeadquartesCtrl', ["headquartersService", "merchantsService", "placesService","$http","$window", "$rootScope","$scope", '$modal',aHeadquartesCtrl] );
app.controller( 'aMerchantsCtrl', ["merchantsService","$http","$window", "$rootScope","$scope", '$modal',aMerchantsCtrl] );
app.controller( 'adminCtrl', [ "$rootScope", '$scope',adminCtrl] );
/**
 * Controlador para ventanas modales
 * @param $scope
 * @param $modalInstance
 */
function modalBorrarCtrl($scope, $modalInstance) {
	$scope.ok = function () {
		$modalInstance.close();
	};		  
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};
/**
 * Controlador de página admin/admin.html para Usuarios
 */
function aUsersCtrl(rolesService, usersService, productsService, headquartersService, merchantsService, $http, $window, $rootScope, $scope, $modal){
	var vmUsers = this;
	
		// Inicialización de variables
		vmUsers.users = [];
		vmUsers.products = [];	
		// CALLBACKS DE SERVICIOS -----------------------------------------------------------------------------------------------------------------------------------------------
	    vmUsers.callBackDelOK = function(data, status, headers, config){
	    	if (status=200){
		    	for(var i=0; i< vmUsers.users.length;i++){
					user = vmUsers.users[i];
		    		if(user.id==data.user.id){
		    			delete vmUsers.users[i];
		    			vmUsers.users[i]=null;
		    			vmUsers.users.length--;
		    			break;
		    		}
				} 
				
		    	// Paginación
		    	vmUsers.paginacion(vmUsers.users,"vmUsers.usersPerPage");
		  	   alert("Usuario eliminado correctamente.");
	     	} else if(status==203){
	    		alert("debe loguearse.");
	    		$window.location.href =servidor+'/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmUsers.callBackDelKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==409){
	    		alert("Error: El login ya existe. "+data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmUsers.callBackListOK = function(data, status, headers, config){
	    	if (status==200){
	    	   if(data.users!=undefined){
	    		   vmUsers.users = data.users;
	   	    	   // Paginación
	        	   vmUsers.paginacion(data.users,"vmUsers.usersPerPage");
	        	   $rootScope.vmAdmin.usersDisabled = false;
	    	   }
	    	   if(data.products!=undefined){
	    		   vmUsers.products = data.products;
	    	   }
	    	   if(data.headquarters!=undefined){
	    		   vmUsers.headquarters = data.headquarters;
	    	   }
	    	   if(data.roles!=undefined){
	    		   vmUsers.roles = data.roles;
	    	   }
	    	   if(data.merchants!=undefined){
	    		   vmUsers.merchants = data.merchants;
	    	   }
	     	} else if(status==203){
	     		$rootScope.vmAdmin.usersDisabled = true;
	     		//alert("debe loguearse.");
	    		//$window.location.href =servidor+'/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmUsers.callBackListKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if(status==500){ 
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmUsers.callBackUpdOK = function(data, status, headers, config){
	    	if(status==200){
		    	for(var i=0; i< vmUsers.users.length;i++){
					user = vmUsers.users[i];
		    		if(user.id==data.user.id){
		    			vmUsers.users[i] = data.user;
		        		for (var j=0;j<vmUsers.roles.length;j++){
		    	    		rol = vmUsers.roles[j];
		    	    		if (rol.id==data.user.rol_id){
		    	    			vmUsers.users[i].rol = vmUsers.roles[j].descripcion; 		
		    	    		}
		    	    	}
		    		}
		    	}
				// Paginación
		    	vmUsers.paginacion(vmUsers.users,"vmUsers.usersPerPage");
				alert("Usuario actualizado correctamente.");
	
		    	vmUsers.changeRol();
	      	} else if(status==203){
	        		alert("debe loguearse.");
	        		$window.location.href =servidor + '/index.html';	
	       	} else {	
	       		alert(data);
	       	}
	    }
	    vmUsers.callBackUpdKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==409){
	    		alert("Error: El login ya existe. "+data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	
	    vmUsers.callBackInsOK = function(data, status, headers, config){
	    	if(status==200){
		    	index=vmUsers.users.length;
		    	vmUsers.users[index]={};
		    	vmUsers.users[index].id = data.user.id;
		    	vmUsers.users[index].name = data.user.name;
		    	vmUsers.users[index].surname = data.user.surname;
		    	vmUsers.users[index].email = data.user.email;
		    	vmUsers.users[index].login = data.user.login;
		    	vmUsers.users[index].password = data.user.password;
		    	vmUsers.users[index].rol_id = data.user.rol_id;
		    	vmUsers.users[index].product_id = data.user.product_id;
		    	vmUsers.users[index].hq_id = data.user.hq_id;
		    	vmUsers.users[index].merchant_id = data.user.merchant_id;
		    	for (var i=0;i<vmUsers.roles.length;i++){
		    		rol = vmUsers.roles[i];
		    		if (rol.id==data.user.rol_id){
		    			vmUsers.users[index].rol = vmUsers.roles[i].descripcion; 		
		    			break;
		    		}
		    	}
		    	vmUsers.changeRol();
		    	// Paginación
		    	vmUsers.paginacion(vmUsers.users,"vmUsers.usersPerPage");
				alert("Usuario insertado correctamente.");
	    	} else if(status==203){
	    		alert("debe loguearse.");
	    		$window.location.href =servidor + '/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmUsers.callBackInsKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==409){
	    		alert("Error: El login ya existe. "+data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmUsers.callBackByIdOK = function(data, status, headers, config){
	    	if (status==200){
		    	if (data.user!=undefined){
		    		vmUsers.id=data.user[0].id;
		    		vmUsers.name=data.user[0].name;
		    		vmUsers.surname=data.user[0].surname;
		    		vmUsers.login=data.user[0].login;
		    		//vmUsers.password=data.user[0].password;
		    		vmUsers.email=data.user[0].email;
		    		vmUsers.rol_id=data.user[0].rol_id;
		    		vmUsers.product_id=data.user[0].product_id; 
		    		vmUsers.hq_id=data.user[0].hq_id; 
		    		vmUsers.merchant_id=data.user[0].merchant_id;
		    		vmUsers.changeRol();
		    	}
		    	if (data.rol!=undefined){
	    			for(var i = 0; i<vmUsers.users.length;i++){
	        			user = vmUsers.users[i];
	    				if(user.id==vmUsers.id){
	    					vmUsers.users[i].rol = data.rol[0].descripcion;
	    				}
	        		}
		    	}
		    	if (data.headquarter!=undefined){
	    			for(var i = 0; i<vmUsers.users.length;i++){
	        			user = vmUsers.users[i];
	    				if(user.id==data.user.id){
	    					vmUsers.users[i].headquarter = data.headquarter[0].descripcion;
	    				}
	        		}
		    	}    	
	    		// Paginación
		    	vmUsers.paginacion(vmUsers.users,"vmUsers.usersPerPage");	    	
		    } else if(status==203){
	    		alert("debe loguearse.");
	    		$window.location.href =servidor + '/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmUsers.callBackByIdKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmUsers.changeRol = function(){
	    	if (vmUsers.rol_id=="1"||vmUsers.rol_id=="5"){
	    		document.getElementById('product').disabled=true;
	    		document.getElementById('headquarter').disabled=true;
	    		document.getElementById('merchant').disabled=true;
	    	} else if (vmUsers.rol_id=="2" ){
	    		document.getElementById('product').disabled=true;
	    		document.getElementById('headquarter').disabled=true;
	    		document.getElementById('merchant').disabled=false;
	    	} else if (vmUsers.rol_id=="3" ){
	    		document.getElementById('product').disabled=true;
	    		document.getElementById('headquarter').disabled=false;
	    		document.getElementById('merchant').disabled=true;
	    	} else if (vmUsers.rol_id=="4" ){
	    		document.getElementById('product').disabled=false;
	    		document.getElementById('headquarter').disabled=true;
	    		document.getElementById('merchant').disabled=true;
	    	}
	    }
	    
	    // BOTONES -----------------------------------------------------------------------------------------------------
	    vmUsers.procesarNuevo = function(){
	    	vmUsers.id="";
			vmUsers.name="";
			vmUsers.surname="";
			vmUsers.login="";
			vmUsers.password="";
			vmUsers.email="";
			vmUsers.rol_id=5;
			vmUsers.product_id=0; 
			vmUsers.hq_id=0; 
			vmUsers.merchant_id=0;
	    }
	    vmUsers.procesarBorrar = function (id) {
	        var modalInstance = $modal.open({
	          templateUrl: 'borrarmodalUser.html',
	          controller: 'modalBorrarCtrl',
	          size: 'sm',
	          resolve: {
	        	  		id:function () {
	              			return $scope.id;
	          			},
	        	  		scope:function () {
	              			return $scope.vm;
	          			}
	          		   }
	        });
	        modalInstance.result.then(function () {
	        	usersService.delUser(vmUsers, id);
	          }, null);
		}
	    vmUsers.procesarGuardar = function(){
    			if(vmUsers.rol_id==undefined){
    				vmUsers.rol_id = 5;
    				vmUsers.product_id = 0;
    				vmUsers.hq_id = 0;
    				vmUsers.merchant_id = 0;
    			} else {
    				switch (vmUsers.rol_id){
    				case "2",2:
    					if(vmUsers.product_id==undefined||vmUsers.product_id==0){
    						alert("Debe seleccionar un comerciante.");
    						return;
    					}
	    				vmUsers.hq_id = 0;
	    				vmUsers.merchant_id = 0;
    					break;
    				case "3",3:
    					if(vmUsers.hq_id==undefined||vmUsers.hq_id==0){
    						alert("Debe seleccionar una delegación.");
    						return;
    					}
	    				vmUsers.product_id = 0;
	    				vmUsers.merchant_id = 0;	    					
    					break;
    				case "4",4:
    					if(vmUsers.merchant_id==undefined||vmUsers.merchant_id==0){
    						alert("Debe seleccionar un producto.");
    						return;
    					}
	    				vmUsers.product_id = 0;
	    				vmUsers.hq_id = 0;	    					
    					break;
    				default:
    					vmUsers.product_id = 0;
    					vmUsers.hq_id = 0;
    					vmUsers.merchant_id = 0;
    				}
    			}

	    	if(vmUsers.id!=undefined)

	    		if(vmUsers.password!=undefined){
	    			usersService.updUser(	vmUsers,
	    								(vmUsers.id==undefined)?"":vmUsers.id,
	    								(vmUsers.name==undefined)?"":vmUsers.name,
										(vmUsers.surname==undefined)?"":vmUsers.surname,
										(vmUsers.email==undefined)?"":vmUsers.email,
										(vmUsers.login==undefined)?"":vmUsers.login,
										(vmUsers.password==undefined)?"":vmUsers.password,
										(vmUsers.rol_id==undefined)?"5":vmUsers.rol_id,
										(vmUsers.product_id==undefined)?"0":vmUsers.product_id,    								
										(vmUsers.hq_id==undefined)?"0":vmUsers.hq_id,
										(vmUsers.merchant_id==undefined)?"0":vmUsers.merchant_id	);
	    		}else{
	    			usersService.updUserSinPwd(	vmUsers,
							(vmUsers.id==undefined)?"":vmUsers.id,
							(vmUsers.name==undefined)?"":vmUsers.name,
							(vmUsers.surname==undefined)?"":vmUsers.surname,
							(vmUsers.email==undefined)?"":vmUsers.email,
							(vmUsers.login==undefined)?"":vmUsers.login,
							(vmUsers.rol_id==undefined)?"5":vmUsers.rol_id,
							(vmUsers.product_id==undefined)?"0":vmUsers.product_id,    								
							(vmUsers.hq_id==undefined)?"0":vmUsers.hq_id,
							(vmUsers.merchant_id==undefined)?"0":vmUsers.merchant_id	);
	    		}
	    	else
	    		usersService.putUser(	vmUsers,	
	    								(vmUsers.name==undefined)?"":vmUsers.name,
	    								(vmUsers.surname==undefined)?"":vmUsers.surname,
	    								(vmUsers.email==undefined)?"":vmUsers.email,
	    								(vmUsers.login==undefined)?"":vmUsers.login,
	    								(vmUsers.password==undefined)?"":vmUsers.password,
	    								(vmUsers.rol_id==undefined)?"5":vmUsers.rol_id,
	    								(vmUsers.product_id==undefined)?"0":vmUsers.product_id,    								
	    								(vmUsers.hq_id==undefined)?"0":vmUsers.hq_id,
	    								(vmUsers.merchant_id==undefined)?"0":vmUsers.merchant_id	);
	    }
	    vmUsers.procesarId = function(id){
	    	usersService.getUserById(vmUsers,id);
	    }    
	  //PAGINACION -------------------------------------------------------------------------------------------------
	    vmUsers.paginacion = function(data,asignation){
	    	data=(data==undefined)?[]:data;
	    	vmUsers.numPerPage = 10;
	    	vmUsers.total = data.length;
	    	vmUsers.currentPage = 1;
	    	eval(asignation+"=data.slice( (vmUsers.currentPage - 1) * vmUsers.numPerPage, ((vmUsers.currentPage - 1) * vmUsers.numPerPage) +vmUsers.numPerPage )");
	    	vmUsers.pageChanged = function() {
	    		eval(asignation+"=data.slice( (vmUsers.currentPage - 1) * vmUsers.numPerPage, ((vmUsers.currentPage - 1) * vmUsers.numPerPage) +vmUsers.numPerPage )");
	    	};
	    	vmUsers.maxSize=20;
	     }
	    // Llamadas a servicios
	    vmUsers.initData = function(rolId){
	    	if (rolId==1){
	    		rolesService.getRoles(vmUsers);
	    		usersService.getUsers(vmUsers);
	    		productsService.getProducts(vmUsers);
	    		headquartersService.getHeadquarters(vmUsers);
	    		merchantsService.getMerchants(vmUsers);
	    	}
		}

	$rootScope.vmUsers = vmUsers; 
    return vmUsers;
}
/**
 * Controlador de página admin/admin.html para Productos
 */
function aProductsCtrl(productsService, headquartersService, fileUploadService, deleteService, $http, $window, $rootScope, $scope, $modal){
	var vmProducts = this;
		vmProducts.products=[];
	 // CALLBACKS DE SERVICIOS -----------------------------------------------------------------------------------------------------------------------------------------------
	    vmProducts.callBackDelOK = function(data, status, headers, config){
			for(var i = 0; i<vmProducts.products.length;i++){
				product=vmProducts.products[i];
				if(product.id==data.product.id){
	    			delete vmProducts.products[i];
	    			vmProducts.products[i]=null;
	    			vmProducts.products.length--;
	    			deleteService.deleteImage(product.id+".jpg");
	    		}
	    	}
	    	// Paginación
	  	   vmProducts.paginacion(vmProducts.products,"vmProducts.productsPerPage");
	  	   alert("Producto se borró correctamente.");
	    }
	    vmProducts.callBackDelKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if(status==500){ 
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmProducts.callBackListOK = function(data, status, headers, config){
	    	if (status==200){
	    		if(data.products!=undefined){
	     		   vmProducts.products = data.products;
	   	    	   // Paginación
	     		   vmProducts.paginacion(data.products,"vmProducts.productsPerPage");
	     		   
		    	}
		    	if(data.headquarters!=undefined){
		    	   vmProducts.headquarters = data.headquarters;
		    	}
		    	$rootScope.vmAdmin.productsDisabled = false;
		    } else if(status==203){
		    	$rootScope.vmAdmin.productsDisabled = true;
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmProducts.callBackListKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if(status==500){ 
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmProducts.callBackUpdOK = function(data, status, headers, config){
	    	if(status=200){
	    		for(var i = 0; i<vmProducts.products.length;i++){
	    			product = vmProducts.products[i];
	    			if(product.id==data.product.id){
		    			vmProducts.products[i] = data.product;
		    			if(vmProducts.products[i].imagen==undefined)
		    			{	vmProducts.products[i].imagen = data.product.id+".jpg"; }
		        		for (var j=0;j<vmProducts.headquarters.length;j++){
		    	    		headquarter = vmProducts.headquarters[j];
		    	    		if (headquarter.id==data.product.hq_id){
		    	    			vmProducts.products[i].headquarter = vmProducts.headquarters[j].descripcion; 		
		    	    		}
		    	    	}
		    		}
	    		}
		    	// Paginación
	 	  	    vmProducts.paginacion(vmProducts.products,"vmProducts.productsPerPage");
	 	  	    if(vmProducts.imagen!=undefined)
	 	  	    {	fileUploadService.uploadFileToUrl(vmProducts, data.product.id, vmProducts.imagen); }	
	    		alert("Producto actualizado correctamente.");
	    	} else if(status==203){
	    		alert("debe loguearse.");
	    		$window.location.href =servidor + '/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmProducts.callBackUpdKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==409){
	    		alert("Error: El login ya existe. "+data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	
	    vmProducts.callBackInsOK = function(data, status, headers, config){
	    	if(status==200){
		    	index=vmProducts.products.length;
		    	vmProducts.products[index]={};
		    	vmProducts.products[index].id = data.product.id;
		    	vmProducts.products[index].descripcion = data.product.descripcion;
		    	vmProducts.products[index].hq_id = data.product.hq_id;
		    	vmProducts.products[index].imagen = data.product.id + ".jpg";
		    	for (var j=0;j<vmProducts.headquarters.length;j++){
	    	    	headquarter = vmProducts.headquarters[j];
	    	    	if (headquarter.id==data.product.hq_id){
	    	    		vmProducts.products[index].headquarter = vmProducts.headquarters[j].descripcion; 		
	    	    	}
	    	    }
		    	// Paginación
	 	  	    vmProducts.paginacion(vmProducts.products,"vmProducts.productsPerPage");
	 	  	    fileUploadService.uploadFileToUrl(vmProducts, data.product.id, vmProducts.imagen);	
				alert("Producto insertado correctamente.");
	    	} else if(status==203){
	    		alert("debe loguearse.");
	    		$window.location.href =servidor + '/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmProducts.callBackInsKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==409){
	    		alert("Error: El login ya existe. "+data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmProducts.callBackByIdOK = function(data, status, headers, config){
	    	if (status==200){
	    		if (data.product!=undefined){
			    	vmProducts.id=data.product[0].id;
			    	vmProducts.descripcion=data.product[0].descripcion;
			    	vmProducts.precio=data.product[0].precio;
			    	vmProducts.imagen=data.product[0].imagen;
			    	vmProducts.hq_id=data.product[0].hq_id;
	    		}
	    		if (data.headquarter!=undefined&&data.headquarter.length>0){
	    			for(var i = 0; i<vmProducts.products.length;i++){
	        			product = vmProducts.products[i];
	    				if(product.id==data.product.id){
	    					vmProducts.products[i].headquarter = data.headquarter;
	    				}
	        		}
	    		}
	    	} else if(status==203){
	    		alert("debe loguearse.");
	    		$window.location.href =servidor + '/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmProducts.callBackByIdKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmProducts.callBackProxIdProductOK = function(data, status, headers, config){
	    	if (status==200){
		    	vmProducts.proximoId = data.product[0].proximoId;
	    	} else if(status==203){
	    		alert("debe loguearse.");
	    		$window.location.href =servidor + '/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmProducts.callBackProxIdProductOK = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	
	    
	    
	    
	    vmProducts.fileUploadOK = function(data, status, headers, config){
	    	if(status==200){
	    		alert("El fichero subio correctamente.");
	    	}
	    }
	    vmProducts.fileUploadKO = function(data, status, headers, config){
	    	if(status==500){
	    		alert("Ocurrió un error. " + data);
	    	} else {
	    		alert(data);
	    	}
	    }
	    
	 // BOTONES -----------------------------------------------------------------------------------------------------
	    vmProducts.procesarNuevo = function(){
	    	vmProducts.id="";
	    	vmProducts.descripcion="";
	    	vmProducts.precio=0;
	    	vmProducts.hq_id=0; 
	    }
	    vmProducts.procesarBorrar = function (id) {
	        var modalInstance = $modal.open({
	          templateUrl: 'borrarmodalProduct.html',
	          controller: 'modalBorrarCtrl',
	          size: 'sm',
	          resolve: {
	        	  		id:function () {
	              			return $scope.id;
	          			},
	        	  		scope:function () {
	              			return $scope.vmProducts;
	          			}
	          		   }
	        });
	        modalInstance.result.then(function () {
	        	productsService.delProduct(vmProducts, id);
	          }, null);
		}
	    vmProducts.procesarGuardar = function(){
	    	if(vmProducts.hq_id==undefined||vmProducts.hq_id==0||vmProducts.hq_id=="0"){
	    		alert("Debe seleccionar una delegación.")
	    		return;
	    	}
	    	if(vmProducts.id!=undefined){
	    		if (vmProducts.imagen==undefined||vmProducts.imagen=="")
	    			productsService.updProductSinImagen(	vmProducts,
							(vmProducts.id==undefined)?"":vmProducts.id,
							(vmProducts.descripcion==undefined)?"":vmProducts.descripcion,
							(vmProducts.precio==undefined)?0:vmProducts.precio,
							(vmProducts.hq_id==undefined)?0:vmProducts.hq_id,
							(vmProducts.place_id==undefined)?0:vmProducts.place_id
							);
	    		else
	    			productsService.updProduct(	vmProducts,
	    								(vmProducts.id==undefined)?"":vmProducts.id,
	    								(vmProducts.descripcion==undefined)?"":vmProducts.descripcion,
	    								(vmProducts.precio==undefined)?0:vmProducts.precio,
										(vmProducts.hq_id==undefined)?0:vmProducts.hq_id,
										(vmProducts.place_id==undefined)?0:vmProducts.place_id,
										(vmProducts.imagen==undefined||vmProducts.imagen=="")?"":vmProducts.id+".jpg");
	    	}else{
	    		if (vmProducts.imagen==undefined||vmProducts.imagen=="")
	    			alert("La imagen es obligatoria");
	    		else
	    			productsService.putProductSinImagen(	vmProducts,	
	    								(vmProducts.descripcion==undefined)?"":vmProducts.descripcion,
	    								(vmProducts.precio==undefined)?0:vmProducts.precio,
	    								(vmProducts.hq_id==undefined||vmProducts.hq_id==0)?0:vmProducts.hq_id,
	    								(vmProducts.place_id==undefined)?0:vmProducts.place_id
	    								);
	    	}
	    }
	    vmProducts.procesarId = function(id){
	    	productsService.getProductById(vmProducts,id);
	    }  
	  //PAGINACION -------------------------------------------------------------------------------------------------
	    vmProducts.paginacion = function(data,asignation){
	    	data=(data==undefined)?[]:data;
	    	vmProducts.numPerPage = 10;
	    	vmProducts.total = data.length;
	    	vmProducts.currentPage = 1;
	    	eval(asignation+"=data.slice( (vmProducts.currentPage - 1) * vmProducts.numPerPage, ((vmProducts.currentPage - 1) * vmProducts.numPerPage) +vmProducts.numPerPage )");
	    	vmProducts.pageChanged = function() {
	    		eval(asignation+"=data.slice( (vmProducts.currentPage - 1) * vmProducts.numPerPage, ((vmProducts.currentPage - 1) * vmProducts.numPerPage) +vmProducts.numPerPage )");
	    	};
	    	vmProducts.maxSize=20;
	     }
	    // Llamadas a servicios
	    vmProducts.initData = function(rolId){
	    	if(rolId==1){
	    		productsService.getProducts(vmProducts);
			    headquartersService.getHeadquarters(vmProducts);
	    	} else {
	    		productsService.getProductsByUser(vmProducts);
	    		headquartersService.getHeadquartersByUser(vmProducts);
	    	}
		}
	$rootScope.vmProducts = vmProducts; 
	return vmProducts;
};
/**
 * Controlador de página admin/admin.html para delegaciones
 */
function aHeadquartesCtrl(headquartersService, merchantsService, placesService,  $http, $window,  $rootScope, $scope, $modal){
	var vmHQs = this;
		vmHQs.headquarters=[];
	    // CALLBACKS DE SERVICIOS -----------------------------------------------------------------------------------------------------------------------------------------------
	    vmHQs.callBackDelOK = function(data, status, headers, config){
			for(var i = 0; i<vmHQs.headquarters.length;i++){
				headquarter=vmHQs.headquarters[i];
				if(headquarter.id==data.headquarter.id){
					delete vmHQs.headquarters[i];
	    			vmHQs.headquarters[i]=null;
	    			vmHQs.headquarters.length--;
	    		}
	    	}
	    	// Paginación
	  	   vmHQs.paginacion(vmHQs.headquarters,"vmHQs.headquartersPerPage");
	  	   alert("Producto se borró correctamente.");
	    }
	    vmHQs.callBackDelKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if(status==500){ 
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmHQs.callBackListOK = function(data, status, headers, config){
	    	if (status==200){
	    		if(data.headquarters!=undefined){
	    			vmHQs.headquarters = data.headquarters;
		    		// Paginación
	    			vmHQs.paginacion(data.headquarters,"vmHQs.headquartersPerPage");
	     			$rootScope.vmAdmin.headquartersDisabled = false;
	     			$rootScope.vmAdmin.productsDisabled = false;
	    		}
		    	if(data.merchants!=undefined){
			    	   vmHQs.merchants = data.merchants;
			    }
	     	} else if(status==203){
	     		$rootScope.vmAdmin.headquartersDisabled = true;
	     		$rootScope.vmAdmin.productsDisabled = true;
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmHQs.callBackListKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if(status==500){ 
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmHQs.callBackUpdOK = function(data, status, headers, config){
	    	if(status=200){
	    		for(var i = 0; i<vmHQs.headquarters.length;i++){
	    			headquarter = vmHQs.headquarters[i];
					if(headquarter.id==data.headquarter.id){
						vmHQs.headquarters[i] = data.headquarter;
		    			alert("Delegación actualizada correctamente.");
					}
					
	    		}
	    		// Paginación
	    		vmHQs.paginacion(vmHQs.headquarters,"vmHQs.headquartersPerPage");
	 	  	
	    	} else if(status==203){
	    		alert("debe loguearse.");
	    		$window.location.href =servidor + '/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmHQs.callBackUpdKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==409){
	    		alert(data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	
	    vmHQs.callBackInsOK = function(data, status, headers, config){
	    	if(status==200){
	    		if(data.headquarter!=undefined){
			    	index=vmHQs.headquarters.length;
			    	vmHQs.headquarters[index]={};
			    	vmHQs.headquarters[index].id = data.headquarter.id;
			    	vmHQs.headquarters[index].descripcion = data.headquarter.descripcion;
			    	vmHQs.headquarters[index].merchant_id = data.headquarter.merchant_id;
				    // Paginación
			    	vmHQs.paginacion(vmHQs.headquarters,"vmHQs.headquartersPerPage");
			    	alert("Delegación insertada correctamente.");
	    		}
	    		if(data.place!=undefined){
	    			vmHQs.place_id=data.place.id;
	    			alert("Ubicación insertada correctamente.");
	    			if (vmHQs.id!=undefined&&vmHQs.id!=""){
	    				headquartersService.updHeadquarter(	vmHQs,
								(vmHQs.id==undefined)?"":vmHQs.id,
								(vmHQs.descripcion==undefined)?"":vmHQs.descripcion,
								(vmHQs.merchant_id==undefined)?0:vmHQs.merchant_id,
								(vmHQs.place_id==undefined)?null:vmHQs.place_id
								);
	    			} else {
	    				headquartersService.putHeadquarter(	vmHQs,
							(vmHQs.descripcion==undefined)?"":vmHQs.descripcion,
							(vmHQs.merchant_id==undefined)?0:vmHQs.merchant_id,
							(vmHQs.place_id==undefined)?null:vmHQs.place_id		
							);
	    			}
	    		}
	    	} else if(status==203){
	    		alert("debe loguearse.");
	    		$window.location.href =servidor + '/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmHQs.callBackInsKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==409){
	    		alert(data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmHQs.callBackByIdOK = function(data, status, headers, config){
	    	if (status==200){
	    		vmHQs.id=data.headquarter[0].id;
	    		vmHQs.descripcion=data.headquarter[0].descripcion;
	    		vmHQs.merchant_id=data.headquarter[0].merchant_id;
	    		vmHQs.place_id=data.headquarter[0].place_id;
	    		vmHQs.direccion=data.headquarter[0].direccion;
	    		vmHQs.initializeMap(vmHQs.direccion);
	    	} else if(status==203){
	    		alert("debe loguearse.");
	    		$window.location.href =servidor + '/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmHQs.callBackByIdKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	// BOTONES -----------------------------------------------------------------------------------------------------
	    vmHQs.procesarNuevo = function(){
	    	vmHQs.id="";
	    	vmHQs.descripcion="";
	    	vmHQs.direccion="";
	    	vmHQs.place_id=0;
	    	vmHQs.merchant_id=0;
	    }
	    vmHQs.procesarBorrar = function (id) {
	        var modalInstance = $modal.open({
	          templateUrl: 'borrarmodalHeadquarter.html',
	          controller: 'modalBorrarCtrl',
	          size: 'sm',
	          resolve: {
	        	  		id:function () {
	              			return $scope.id;
	          			},
	        	  		scope:function () {
	              			return $scope.vmHQs;
	          			}
	          		   }
	        });
	        modalInstance.result.then(function () {
	        	headquartersService.delHeadquarter(vmHQs, id);
	          }, null);
		}
	    vmHQs.procesarGuardar = function(){
	    	if(vmHQs.merchant_id==undefined||vmHQs.merchant_id==0||vmHQs.merchant_id=="0"){
	    		alert("Debe seleccionar un comerciante.")
	    		return;
	    	}
	    	if(vmHQs.id!=undefined&&vmHQs.id!=""){
	    		if ($rootScope.vmAutentication.authRolId>2){
	    			headquartersService.updHeadquarterByUser(vmHQs,
	    								(vmHQs.id==undefined)?"":vmHQs.id,
	    								(vmHQs.descripcion==undefined)?"":vmHQs.descripcion,
										(vmHQs.merchant_id==undefined)?0:vmHQs.merchant_id,
										(vmHQs.place_id==undefined)?null:vmHQs.place_id
										);
	    		} else {
	    			if (vmHQs.direccion!=""){
	    				vmHQs.initializeMap(vmHQs.direccion);
	    				if(vmHQs.place_id!=undefined||vmHQs.place_id!=""){
	    					placesService.putPlace( vmHQs,
									(vmHQs.direccion==undefined)?"":vmHQs.direccion,
									(vmHQs.merchant_id==undefined)?0:vmHQs.merchant_id
							);	    					
	    				}
	    			}  else {	    		
	    				headquartersService.updHeadquarter(	vmHQs,
							(vmHQs.id==undefined)?"":vmHQs.id,
							(vmHQs.descripcion==undefined)?"":vmHQs.descripcion,
							(vmHQs.merchant_id==undefined)?0:vmHQs.merchant_id,
							null
							);
	    			}
	    		}
	    	}else{
	    		if ($rootScope.vmAutentication.authRolId>2){
	    			alert("Para añadir una delegación debe ser al menos rol merchant.")
	    		} else {
	    			if (vmHQs.direccion!=""){
	    				
	    				vmHQs.initializeMap(vmHQs.direccion);
	    				placesService.putPlace( vmHQs,
	    										(vmHQs.direccion==undefined)?"":vmHQs.direccion,
	    										(vmHQs.merchant_id==undefined)?0:vmHQs.merchant_id
	    								);
	    			} else {	 
	    				headquartersService.putHeadquarter(	vmHQs,
										(vmHQs.descripcion==undefined)?"":vmHQs.descripcion,
										(vmHQs.merchant_id==undefined)?0:vmHQs.merchant_id,
										(vmHQs.place_id==undefined)?null:vmHQs.place_id		
										);
	    			}
	    		}
	    	}
	    }
	    vmHQs.procesarId = function(id){
	    	headquartersService.getHeadquarterById(vmHQs,id);
	    }  
	  //PAGINACION -------------------------------------------------------------------------------------------------
	    vmHQs.paginacion = function(data,asignation){
	    	data=(data==undefined)?[]:data;
	    	vmHQs.numPerPage = 10;
	    	vmHQs.total = data.length;
	    	vmHQs.currentPage = 1;
	    	eval(asignation+"=data.slice( (vmHQs.currentPage - 1) * vmHQs.numPerPage, ((vmHQs.currentPage - 1) * vmHQs.numPerPage) +vmHQs.numPerPage )");
	    	vmHQs.pageChanged = function() {
	    		eval(asignation+"=data.slice( (vmHQs.currentPage - 1) * vmHQs.numPerPage, ((vmHQs.currentPage - 1) * vmHQs.numPerPage) +vmHQs.numPerPage )");
	    	};
	    	vmHQs.maxSize=20;
	     }
	    vmHQs.getLocation = function(val) {
	        return $http.post(servidor+'/service', {
	          service: "getLocations",	          
	          params: {
	            direccion: val
	          }
	        }).then(function(response){
	          return response.data.direccion.map(function(item){
	        	  return item;
	          });
	        });
	    }
	    vmHQs.onSelect = function ($item) {
	    	   vmHQs.place_id = $item.id;
	    	   vmHQs.direccion = $item.direccion;
    	};
    	vmHQs.onChange = function () {
	    	   vmHQs.place_id = "";
    	};
	    // Llamadas a servicios
	    vmHQs.initData = function(rolId){
	    	if(rolId==1){
		    	headquartersService.getHeadquarters(vmHQs);
		    	merchantsService.getMerchants(vmHQs);
	    	} else {
	    		headquartersService.getHeadquartersByUser(vmHQs);
	    		merchantsService.getMerchantsByUser(vmHQs);
	    	}
	    }
	    vmHQs.initializeMap = function(address) {
		        // I create a new google maps object to handle the request and we pass the address to it
		    var geoCoder = new google.maps.Geocoder(address)
		        // a new object for the request I called "request" , you can put there other parameters to specify a better search (check google api doc for details) , 
		        // on this example im going to add just the address  
		    var request = {address:address};
		         
		        // I make the request 
		    geoCoder.geocode(request, function(result, status){
		                // as a result i get two parameters , result and status.
		                // results is an  array tha contenis objects with the results founds for the search made it.
		                // to simplify the example i take only the first result "result[0]" but you can use more that one if you want
		   
		                // So , using the first result I need to create a  latlng object to be pass later to the map
		                var latlng = new google.maps.LatLng(result[0].geometry.location.lat(), result[0].geometry.location.lng());  
		   
		        // some initial values to the map   
		        var myOptions = {
		          zoom: 15,
		          center: latlng,
		          mapTypeId: google.maps.MapTypeId.ROADMAP
		        };
		   
		             // the map is created with all the information 
		               var map = new google.maps.Map(document.getElementById("map_canvas"),myOptions);
		   
		             // an extra step is need it to add the mark pointing to the place selected.
		            var marker = new google.maps.Marker({position:latlng,map:map,title:'title'});
		   
		    })
	    }
	$rootScope.vmHeadquarters = vmHQs; 
	return vmHQs;

};
/**
 * Controlador de página admin/admin.html para comerciantes
 */
function aMerchantsCtrl(merchantsService, $http, $window,  $rootScope, $scope, $modal){
	var vmMerchants = this;
		vmMerchants.merchants=[];
	 // CALLBACKS DE SERVICIOS -----------------------------------------------------------------------------------------------------------------------------------------------
	    vmMerchants.callBackDelOK = function(data, status, headers, config){
			for(var i = 0; i<vmMerchants.merchants.length;i++){
				merchant=vmMerchants.merchants[i];
				if(merchant.id==data.merchant.id){
					delete vmMerchants.merchants[i];
					vmMerchants.merchants[i]=null;
					vmMerchants.merchants.length--;
	    		}
	    	}
	    	// Paginación
			vmMerchants.paginacion(vmMerchants.merchants,"vmMerchants.merchantsPerPage");
	  	   alert("Comerciante se borró correctamente.");
	    }
	    vmMerchants.callBackDelKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if(status==500){ 
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmMerchants.callBackListOK = function(data, status, headers, config){
	    	if (status==200){
	    		vmMerchants.merchants = data.merchants;
		    	// Paginación
	    		vmMerchants.paginacion(data.merchants,"vmMerchants.merchantsPerPage");
	    		$rootScope.vmAdmin.headquartersDisabled = false;
	    		$rootScope.vmAdmin.merchantsDisabled = false;
	     	} else if(status==203){
	     		$rootScope.vmAdmin.headquartersDisabled = true;
	     		$rootScope.vmAdmin.merchantsDisabled = true;
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmMerchants.callBackListKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if(status==500){ 
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmMerchants.callBackUpdOK = function(data, status, headers, config){
	    	if(status=200){
	    		for(var i = 0; i<vmMerchants.merchants.length;i++){
	    			merchant = vmMerchants.merchants[i];
					if(merchant.id==data.merchant.id){
						vmMerchants.merchants[i] = data.merchant;
		    			alert("Comerciante actualizado correctamente.");
					}
	    		}
		    	vmMerchants.paginacion(vmMerchants.merchants,"vmMerchants.merchantsPerPage");
	    	} else if(status==203){
	    		alert("debe loguearse.");
	    		$window.location.href =servidor + '/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmMerchants.callBackUpdKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==409){
	    		alert("Error: El login ya existe. "+data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	
	    vmMerchants.callBackInsOK = function(data, status, headers, config){
	    	if(status==200){
		    	index=vmMerchants.merchants.length;
		    	vmMerchants.merchants[index]={};
		    	vmMerchants.merchants[index].id = data.merchant.id;
		    	vmMerchants.merchants[index].descripcion = data.merchant.descripcion;
		    	// Paginación
		    	vmMerchants.paginacion(vmMerchants.merchants,"vmMerchants.merchantsPerPage");
	 	  	   	alert("Comerciante insertado correctamente.");
	    	} else if(status==203){
	    		alert("debe loguearse.");
	    		$window.location.href =servidor + '/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmMerchants.callBackInsKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==409){
	    		alert("Error: El login ya existe. "+data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	    vmMerchants.callBackByIdOK = function(data, status, headers, config){
	    	if (status==200){
	    		vmMerchants.id=data.merchant[0].id;
	    		vmMerchants.descripcion=data.merchant[0].descripcion;
	    	} else if(status==203){
	    		alert("debe loguearse.");
	    		$window.location.href =servidor + '/index.html';	
	    	} else {	
	    		alert(data);
	    	}
	    }
	    vmMerchants.callBackByIdKO = function(data, status, headers, config){
	    	if(status==400){
	    		alert("La petición es incorrecta. "+data);
	    	} else if (status==500){
	    		alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
	    	} else {
	    		alert(data);
	    	}
	    }
	// BOTONES -----------------------------------------------------------------------------------------------------
	    vmMerchants.procesarNuevo = function(){
	    	vmMerchants.id="";
	    	vmMerchants.descripcion="";
	    }
	    vmMerchants.procesarBorrar = function (id) {
	    	if($rootScope.vmAutentication.authRolId>1){
	    		alert("Debe ser superusuario para borrar.");
	    		return;
	    	}	    	
	        var modalInstance = $modal.open({
	          templateUrl: 'borrarmodalMerchant.html',
	          controller: 'modalBorrarCtrl',
	          size: 'sm',
	          resolve: {
	        	  		id:function () {
	              			return $scope.id;
	          			},
	        	  		scope:function () {
	              			return $scope.vmMerchants;
	          			}
	          		   }
	        });
	        modalInstance.result.then(function () {
	        	merchantsService.delMerchant(vmMerchants, id);
	          }, null);
		}
	    vmMerchants.procesarGuardar = function(){
	    	if(vmMerchants.id!=undefined||vmMerchants.id==""){
	    		if ($rootScope.vmAutentication.authRolId>1){
	    			merchantsService.updMerchantByUser(	vmMerchants,
	    					(vmMerchants.id==undefined)?"":vmMerchants.id,
							(vmMerchants.descripcion==undefined)?"":vmMerchants.descripcion
							);
	    		} else {
	    			merchantsService.updMerchant(	vmMerchants,
	    					(vmMerchants.id==undefined)?"":vmMerchants.id,
	    					(vmMerchants.descripcion==undefined)?"":vmMerchants.descripcion
	    					);
	    		}
	    	}else{
	    		if ($rootScope.vmAutentication.authRolId>1){
	    			alert("Solo puede crear nuevos comerciantes el superuser");
	    			return;
	    		}
	   			merchantsService.putMerchant(	vmMerchants,
										(vmMerchants.descripcion==undefined)?"":vmMerchants.descripcion
										);
	    	}
	    }
	    vmMerchants.procesarId = function(id){
	    	if ($rootScope.vmAutentication.authRolId>1){
	    		merchantsService.getMerchantByIdByUser(vmMerchants,id);
		    } else {
		    	merchantsService.getMerchantById(vmMerchants,id);
		    }
	    }  
	  //PAGINACION -------------------------------------------------------------------------------------------------
	    vmMerchants.paginacion = function(data,asignation){
	    	data=(data==undefined)?[]:data;
	    	vmMerchants.numPerPage = 10;
	    	vmMerchants.total = data.length;
	    	vmMerchants.currentPage = 1;
	    	eval(asignation+"=data.slice( (vmMerchants.currentPage - 1) * vmMerchants.numPerPage, ((vmMerchants.currentPage - 1) * vmMerchants.numPerPage) +vmMerchants.numPerPage )");
	    	vmMerchants.pageChanged = function() {
	    		eval(asignation+"=data.slice( (vmMerchants.currentPage - 1) * vmMerchants.numPerPage, ((vmMerchants.currentPage - 1) * vmMerchants.numPerPage) +vmMerchants.numPerPage )");
	    	};
	    	vmMerchants.maxSize=20;
	     }
	    // Llamadas a servicios
	    vmMerchants.initData = function(rolId){
	    	if(rolId==1){
	    		merchantsService.getMerchants(vmMerchants);
	    	} else if(rolId==2){
	    		merchantsService.getMerchantsByUser(vmMerchants);
	    	}  
	    }
	$rootScope.vmMerchants = vmMerchants; 
	return vmMerchants;

};
/**
 * Controlador de página admin/admin.html
 */
function adminCtrl( $rootScope, $scope){
	var vmAdmin = this;
	$scope.oneAtATime = true;
	vmAdmin.usersDisabled = true;
	vmAdmin.productsDisabled = true;
	vmAdmin.headquartersDisabled = true;
	vmAdmin.merchantsDisabled = true;
	$rootScope.vmAdmin = vmAdmin;
	return vmAdmin;
}
 

