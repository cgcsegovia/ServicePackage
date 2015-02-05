/**
 * Autor: Carlos Guerra
*/
//SERVICIOS ----------------------------------------------------------------------------------
app.service('usersService',['$http','$window' , function($http, $window) {
	 var usersService = {};
	 usersService.getUsers = function(vm){
		var url = servidor+"/service";
		$http.post(url,{"service":"getUsers","params":{}})
			.success(function(data, status, headers, config) {
				vm.callBackListOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackListKO(data, status, headers, config);
	        });
	}
	usersService.getUserById = function(vm,id){
		var url = servidor+"/service";
		$http.post(url,{"service":"getUserById","params":{"id":id}})
			.success(function(data, status, headers, config) {
				vm.callBackByIdOK(data, status, headers, config);
			}).error(function(data, status, headers, config) { 
				vm.callBackByIdKO(data, status, headers, config);
				return data.user;
			});
	}
	usersService.putUserPublic = function(nombre, apellidos, correo, login, password){
		var url = servidor+"/service";
		$http.post(url,{"service":"putUserPublic","params":{"name":nombre,"surname":apellidos,"email":correo,"login":login,"password":password}})
			.success(function(data, status, headers, config) {
				alert("El usuario fue creado correctamente.");
			}).error(function(data, status, headers, config) {
				if (status==400){
	        		alert("La entrada es incorrecta. "+data);
	        	}else if(status==409){
	        		alert("El login ya existe.");
	        	}else if(status==500){
	        		alert("Ha ocurrido un error en el servidor. Intentelo de nuevo más tarde.");
	        	} else {
	        		alert(data);
	        	}
			});
	}
	usersService.putUser = function(vm, nombre, apellidos, correo, login, password, rol_id, product_id, hq_id, merchant_id){
		var url = servidor+"/service";
		$http.post(url,{"service":"putUser","params":{"name":nombre,"surname":apellidos,"email":correo,"login":login,"password":password,"rol_id":rol_id, "product_id":product_id, "hq_id":hq_id, "merchant_id":merchant_id}})
			.success(function(data, status, headers, config) {
				vm.callBackInsOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackInsKO(data, status, headers, config);
			});
	}
	usersService.updUser = function(vm, id, nombre, apellidos, correo, login, password, rol_id, product_id, hq_id, merchant_id){
		var url = servidor+"/service";
		$http.post(url,{"service":"updUser","params":{"id":id,"name":nombre,"surname":apellidos,"email":correo,"login":login,"password":password,"rol_id":rol_id, "product_id":product_id, "hq_id":hq_id, "merchant_id":merchant_id}})
			.success(function(data, status, headers, config) {
				vm.callBackUpdOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackUpdKO(data, status, headers, config);
			});
	}
	usersService.updUserSinPwd = function(vm, id, nombre, apellidos, correo, login, rol_id, product_id, hq_id, merchant_id){
		var url = servidor+"/service";
		$http.post(url,{"service":"updUserSinPwd","params":{"id":id,"name":nombre,"surname":apellidos,"email":correo,"login":login,"rol_id":rol_id, "product_id":product_id, "hq_id":hq_id, "merchant_id":merchant_id}})
			.success(function(data, status, headers, config) {
				vm.callBackUpdOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackUpdKO(data, status, headers, config);
			});
	}
	usersService.delUser = function(vm,id){
		var url = "http://localhost:8080/ServicesPackage/service";
		$http.post(url,{"service":"delUser","params":{"id":id}})
			.success(function(data, status, headers, config) {
				vm.callBackDelOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackDelKO(data, status, headers, config);
			});
	}
    return usersService;
}]);
// SERVICIOS ROLES ------------------------------------------------------------------------------------------------
app.service('rolesService',['$http','$window' , function($http, $window) {
	var rolesService = {};
	var url = servidor+"/service";
	rolesService.getRoles = function(vm){
		$http.post(url,{"service":"getRoles","params":{}})
			.success(function(data, status, headers, config) {
				vm.callBackListOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackListKO(data, status, headers, config);
	        });
	}
	rolesService.getRolById = function(vm, id){
		var url = servidor+"/service";
		$http.post(url,{"service":"getRolById","params":{"id":id}})
			.success(function(data, status, headers, config) {
				vm.callBackByIdOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackByIdKO(data, status, headers, config);
			});
	}
	return rolesService;
}]);
//SERVICIOS PRODUCTS ------------------------------------------------------------------------------------------------
app.service('productsService',['$http','$window' , function($http, $window) {
	var productsService = {};
	var url = servidor+"/service";
	productsService.getProducts = function(vm){
		$http.post(url,{"service":"getProducts","params":{}})
			.success(function(data, status, headers, config) {
				vm.callBackListOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackListKO(data, status, headers, config);
			});
	}
	productsService.getProductsByUser = function(vm){
		var url = servidor+"/service";
		$http.post(url,{"service":"getProductsbyUser","params":{}})
			.success(function(data, status, headers, config) {
				vm.callBackListOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackListKO(data, status, headers, config);
			});
	}
	productsService.getProductById = function(vm,id){
		var url = servidor+"/service";
		$http.post(url,{"service":"getProductById","params":{"id":id}})
			.success(function(data, status, headers, config) {
				vm.callBackByIdOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackByIdKO(data, status, headers, config);
			});
	}
	productsService.putProduct = function(vm, descripcion, precio, hq_id, place_id, imagen){
		var url = servidor+"/service";
		$http.post(url,{"service":"putProduct","params":{"descripcion":descripcion, "precio":precio, "hq_id":hq_id, "place_id":place_id, "imagen":imagen}})
			.success(function(data, status, headers, config) {
				vm.callBackInsOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackInsKO(data, status, headers, config);
			});
	}
	productsService.putProductSinImagen = function(vm, descripcion, precio, hq_id, place_id){
		var url = servidor+"/service";
		$http.post(url,{"service":"putProductSinImagen","params":{"descripcion":descripcion, "precio":precio, "hq_id":hq_id, "place_id":place_id}})
			.success(function(data, status, headers, config) {
				vm.callBackInsOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackInsKO(data, status, headers, config);
			});
		}
	productsService.updProduct = function(vm, id, descripcion, precio, hq_id, place_id, imagen){
		var url = servidor+"/service";
		$http.post(url,{"service":"updProduct","params":{"id":id,"descripcion":descripcion,"precio":precio, "hq_id":hq_id,"imagen":imagen, "place_id":place_id}})
			.success(function(data, status, headers, config) {
				vm.callBackUpdOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackUpdKO(data, status, headers, config);
			});
	}
	productsService.updProductSinImagen = function(vm, id, descripcion, precio, hq_id, place_id){
		var url = servidor+"/service";
		$http.post(url,{"service":"updProductSinImagen","params":{"id":id,"descripcion":descripcion,"precio":precio, "hq_id":hq_id, "place_id":place_id}})
			.success(function(data, status, headers, config) {
				vm.callBackUpdOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackUpdKO(data, status, headers, config);
			});
	}
	productsService.delProduct = function(vm,id){
		var url = servidor+"/service";
		$http.post(url,{"service":"delProduct","params":{"id":id}})
			.success(function(data, status, headers, config) {
				vm.callBackDelOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackDelKO(data, status, headers, config);
			});
	}
	return productsService;
}]);


app.service('headquartersService',['$http','$window' , function($http, $window) {
	var headquartersService = {};
	headquartersService.getHeadquarters = function(vm){
		var url = servidor+"/service";	
		$http.post(url,{"service":"getHeadquarters","params":{}})
			.success(function(data, status, headers, config) {
				vm.callBackListOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackListKO(data, status, headers, config);
			});
	}
	headquartersService.getHeadquartersByUser = function(vm){
		var url = servidor+"/service";	
		$http.post(url,{"service":"getHeadquartersbyUser","params":{}})
			.success(function(data, status, headers, config) {
				vm.callBackListOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackListKO(data, status, headers, config);
			});
	}
	headquartersService.getHeadquarterById = function(vm,id){
		var url = servidor+"/service";
			$http.post(url,{"service":"getHeadquarterById","params":{"id":id}})
				.success(function(data, status, headers, config) {
					vm.callBackByIdOK(data, status, headers, config);
				}).error(function(data, status, headers, config) {
					vm.callBackByIdKO(data, status, headers, config);
				});
		}
	headquartersService.putHeadquarter = function(vm, descripcion, merchant_id, place_id){
		var url = servidor+"/service";
			$http.post(url,{"service":"putHeadquarter","params":{"descripcion":descripcion, "merchant_id":merchant_id, "place_id":place_id}})
				.success(function(data, status, headers, config) {
					vm.callBackInsOK(data, status, headers, config);
				}).error(function(data, status, headers, config) {
					vm.callBackInsKO(data, status, headers, config);
				});
			}
	headquartersService.updHeadquarter = function(vm, id, descripcion, merchant_id, place_id){
		var url = servidor+"/service";
		$http.post(url,{"service":"updHeadquarter","params":{"id":id,"descripcion":descripcion,"merchant_id":merchant_id, "place_id":place_id}})
			.success(function(data, status, headers, config) {
				vm.callBackUpdOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackUpdKO(data, status, headers, config);
			});
	}
	headquartersService.updHeadquarterByUser = function(vm, id, descripcion, merchant_id, place_id){
		var url = servidor+"/service";
		$http.post(url,{"service":"updHeadquarterByUser","params":{"id":id,"descripcion":descripcion,"merchant_id":merchant_id, "place_id":place_id}})
			.success(function(data, status, headers, config) {
				data.headquarter.id=id;
				vm.callBackUpdOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackUpdKO(data, status, headers, config);
			});
	}
	
	headquartersService.delHeadquarter = function(vm,id){
		var url = servidor+"/service";
		$http.post(url,{"service":"delHeadquarter","params":{"id":id}})
			.success(function(data, status, headers, config) {
				vm.callBackDelOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackDelKO(data, status, headers, config);
			});
	}
	return headquartersService;
}]);

app.service('merchantsService',['$http','$window' , function($http, $window) {
	var merchantsService = {};
	merchantsService.getMerchants = function(vm){
		var url = servidor+"/service";
		$http.post(url,{"service":"getMerchants","params":{}})
			.success(function(data, status, headers, config) {
				 vm.callBackListOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				 vm.callBackListKO(data, status, headers, config);
	        });
	}
	merchantsService.getMerchantsByUser = function(vm){
		var url = servidor+"/service";
		$http.post(url,{"service":"getMerchantsByUser","params":{}})
			.success(function(data, status, headers, config) {
				 vm.callBackListOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				 vm.callBackListKO(data, status, headers, config);
	        });
	}
	merchantsService.getMerchantById = function(vm,id){
		var url = servidor+"/service";
			$http.post(url,{"service":"getMerchantById","params":{"id":id}})
				.success(function(data, status, headers, config) {
					vm.callBackByIdOK(data, status, headers, config);
				}).error(function(data, status, headers, config) {
					vm.callBackByIdKO(data, status, headers, config);
				});
		}
	
	merchantsService.getMerchantByIdByUser = function(vm,id){
		var url = servidor+"/service";
			$http.post(url,{"service":"getMerchantByIdByUser","params":{"id":id}})
				.success(function(data, status, headers, config) {
					vm.callBackByIdOK(data, status, headers, config);
				}).error(function(data, status, headers, config) {
					vm.callBackByIdKO(data, status, headers, config);
				});
		}
	merchantsService.putMerchant = function(vm, descripcion){
		var url = servidor+"/service";
			$http.post(url,{"service":"putMerchant","params":{"descripcion":descripcion}})
				.success(function(data, status, headers, config) {
					vm.callBackInsOK(data, status, headers, config);
				}).error(function(data, status, headers, config) {
					vm.callBackInsKO(data, status, headers, config);
				});
			}
	merchantsService.updMerchant = function(vm, id, descripcion){
		var url = servidor+"/service";
		$http.post(url,{"service":"updMerchant","params":{"id":id,"descripcion":descripcion }})
			.success(function(data, status, headers, config) {
				vm.callBackUpdOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackUpdKO(data, status, headers, config);
			});
	}
	merchantsService.updMerchantByUser = function(vm, id, descripcion){
		var url = servidor+"/service";
		$http.post(url,{"service":"updMerchantByUser","params":{"id":id,"descripcion":descripcion }})
			.success(function(data, status, headers, config) {
				data.merchant.id=id;
				vm.callBackUpdOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackUpdKO(data, status, headers, config);
			});
	}
	merchantsService.delMerchant = function(vm,id){
		var url = servidor+"/service";
		$http.post(url,{"service":"delMerchant","params":{"id":id}})
			.success(function(data, status, headers, config) {
				vm.callBackDelOK(data, status, headers, config);
			}).error(function(data, status, headers, config) {
				vm.callBackDelKO(data, status, headers, config);
			});
	}
	return merchantsService;
}]);

app.service('placesService', ['$http', function ($http) {
	this.putPlace = function(vm, direccion, merchant_id){
		var url = servidor+"/service";
		 $http.post(url,{"service":"putPlace","params":{"direccion":direccion, "merchant_id":merchant_id}})
	 		.success(function(data, status, headers, config) {
	 			vm.callBackInsOK(data, status, headers, config);
	 		}).error(function(data, status, headers, config) {
	 			vm.callBackInsKO(data, status, headers, config);
	 		});
	}
}]);

app.service('fileUploadService', ['$http', function ($http) {
    this.uploadFileToUrl = function(vm, productId, file){
    	var uploadUrl = servidor+'/upload';
        var fd = new FormData();
        fd.append('file', file);
        fd.append('id', productId);
        $http.post(uploadUrl, fd, {
            headers: {'Content-Type': undefined},
        	data: fd,
        	transformRequest: angular.identity()
         })
        .success(function(data, status, headers, config) {
        	vm.fileUploadOK(data, status, headers, config);
        })
        .error(function(data, status, headers, config) {
        	vm.fileUploadKO(data, status, headers, config);
        });
    }
}]);

app.service('deleteService', ['$http', function ($http) {
    this.deleteImage = function(imagen){
    	var url = servidor+"/delete";
		$http.post(url,{"imagen":imagen})
			.success(function(data, status, headers, config) {
				 if(status==200){
		        		alert("Fichero imagen: "+imagen+" borrado.");
				 }
			}).error(function(data, status, headers, config) {
	        	if (status==400){
	        		alert("La entrada es incorrecta.");
	        	}else if(status==401){
	        		alert("No tiene permisos para borrar.");
	        	}else if(status==500){
	        		alert("Ha ocurrido un error en el servidor. Intentelo de nuevo más tarde.");
	        	}
	        });
    }
}]);
app.service('autenticationService', ['$http', function ($http) {
	 this.login = function(vm, login, password){
		 var url = servidor+"/autenticacion";
		 $http.post(url,{"service":"getLogin","params":{"login":login,"password":password}})
	 		.success(function(data, status, headers, config) {
	 			vm.callBackLoginOK(data, status, headers, config);
	 		}).error(function(data, status, headers, config) {
	 			vm.callBackLoginKO(data, status, headers, config);
	 		});
	 }
	 this.logout= function(vm){
		 var url = servidor+"/autenticacion";
		 $http.post(url,{"service":"getLogout","params":{}})
	 		.success(function(data, status, headers, config) {
	 			vm.callBackLogoutOK(data, status, headers, config);
	 		}).error(function(data, status, headers, config) {
	 			vm.callBackLogoutKO(data, status, headers, config);
	 		});
	 }
	 this.autenticacion= function(vm){
		 var url = servidor+"/autenticacion";
		 $http.post(url,{"service":"getAutentication","params":{}})
	 		.success(function(data, status, headers, config) {
	 			vm.callBackAutenticacionOK(data, status, headers, config);
	 		}).error(function(data, status, headers, config) {
	 			vm.callBackAutenticacionKO(data, status, headers, config);
	 		});
	 }
}]);


 