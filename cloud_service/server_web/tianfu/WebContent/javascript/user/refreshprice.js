function refreshPrice2(region,core,memory,dataDisk,bandWidth){
		var core_price = 0;
		var memory_price = 0;
		var datadisk_price = 0;
		var bandwidth_price = 0;
		if(region == 4){
			if(core == 1){
				core_price = 48;
			}else if(core == 2){
				core_price = 128;
			}else if(core == 4){
				core_price = 240;
			}else if(core == 8){
				core_price = 512;
			}else if(core == 12){
				core_price = 800;
			}else if(core == 16){
				core_price = 1280;
			}
			
			if(memory == 1){
				memory_price = 48;
			}else if(memory == 2){
				memory_price = 120;
			}else if(memory == 4){
				memory_price = 208;
			}else if(memory == 6){
				memory_price = 304;
			}else if(memory == 8){
				memory_price = 432;
			}else if(memory == 12){
				memory_price = 560;
			}else if(memory == 16){
				memory_price = 688;
			}else if(memory == 32){
				memory_price = 1788.8;
			}else if(memory == 64){
				memory_price = 3577.6;
			}
			
			datadisk_price = dataDisk * 0.8 * 1.6;
			
			if(bandWidth <= 6){
				if(bandWidth == 0){
					bandwidth_price = 0;
				}else if(bandWidth == 1){
					bandwidth_price = 60;
				}else if(bandWidth == 2){
					bandwidth_price = 120;
				}else if(bandWidth == 3){
					bandwidth_price = 180;
				}else if(bandWidth == 4){
					bandwidth_price = 240;
				}else if(bandWidth == 5){
					bandwidth_price = 300;
				}else if(bandWidth == 6){
					bandwidth_price = 400;
				}
			}else{
				bandwidth_price = 300 + (bandWidth-5) * 100;
			}
			return core_price + memory_price + datadisk_price + bandwidth_price;
		}else {
			if(core == 1){
				core_price = 30;
			}else if(core == 2){
				core_price = 80;
			}else if(core == 4){
				core_price = 150;
			}else if(core == 8){
				core_price = 320;
			}else if(core == 12){
				core_price = 500;
			}else if(core == 16){
				core_price = 800;
			}
			
			if(memory == 1){
				memory_price = 30;
			}else if(memory == 2){
				memory_price = 75;
			}else if(memory == 4){
				memory_price = 130;
			}else if(memory == 6){
				memory_price = 190;
			}else if(memory == 8){
				memory_price = 270;
			}else if(memory == 12){
				memory_price = 350;
			}else if(memory == 16){
				memory_price = 430;
			}else if(memory == 32){
				memory_price = 1118;
			}else if(memory == 64){
				memory_price = 2236;
			}
			
			datadisk_price = dataDisk * 0.8;
			
			if(bandWidth <= 6){
				if(bandWidth == 0){
					bandwidth_price = 0;
				}else if(bandWidth == 1){
					bandwidth_price = 30;
				}else if(bandWidth == 2){
					bandwidth_price = 60;
				}else if(bandWidth == 3){
					bandwidth_price = 90;
				}else if(bandWidth == 4){
					bandwidth_price = 120;
				}else if(bandWidth == 5){
					bandwidth_price = 150;
				}else if(bandWidth == 6){
					bandwidth_price = 250;
				}
			}else{
				bandwidth_price = 150 + (bandWidth-5) * 100;
			}
			return core_price + memory_price + datadisk_price + bandwidth_price;
		}
	}
function refreshPrice(region,core,memory,dataDisk,bandWidth){
	var price = "0";
	 ajax.remoteCall("bean://packageOptionService:getCurrentPrice", 
 			[ region,"3",core,memory,dataDisk,bandWidth ],
 			function(reply) { 
		 		if(reply.result.status == 'success'){
		 			price = reply.result.message;
		 		}
 			}
 		);
	 return price;
}