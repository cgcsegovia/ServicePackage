/**
 * Autor: Carlos Guerra
*/
// CONTROLADORES---------------------------------------------------------------------------------------------
app.controller( 'privadoCtrl',  ["usersService","$window", privadoCtrl] );
app.controller( 'carrouselCtrl', ["productsService",carrouselCtrl] );
/**
 * Controlador de página privado.html
 */
function privadoCtrl(usersService,$window){
	var vm = this;
	vm.procesarIrAdministracion = function(){
    	 $window.location.href =servidor+'/admin/index.html';
    }
    return vm;
}
function carrouselCtrl(productsService) {
	vmCarrousel=this;
	vmCarrousel.callBackListOK = function(data, status, headers, config){
	   	if (status==200){
	   		//vmProducts.products = data.products;
	   		for(var i=0;i<data.products.length;i++){
	   			vmCarrousel.addSlide(data.products[i]);	
	   		}
	   		
	   	} else if(status==203){
	   		alert("debe loguearse.");
	   		$window.location.href = servidor + '/index.html';	
	   	} else {	
	   		alert(data);
	   	}
	}
	vmCarrousel.callBackListKO = function(data, status, headers, config){
		if(status==400){
			alert("La petición es incorrecta. "+data);
		} else if(status==500){ 
		 	alert("Error interno del servidor. Inténtelo de nuevo más tarde.");
		} else {
			alert(data);
		}
	}
	
	productsService.getProducts(vmCarrousel);
	vmCarrousel.myInterval = 5000;
	var slides = vmCarrousel.slides = [];
	vmCarrousel.addSlide = function(product) {
		vmCarrousel.slides.push({
			image: servidor + '/images/' + product.id+".jpg",
		    text: product.descripcion
		});
	};
	return vmCarrousel;  
}

