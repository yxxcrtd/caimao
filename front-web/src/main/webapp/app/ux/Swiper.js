define([
        'dojo/query',
        'dojo/on',
    	'dojo/dom-style',
    	'dojo/dom-class',
    	'dojo/_base/fx'
        ],function(query,on,domStyle,domClass,fx){
	var swiperConfig = {};
	swiperConfig.s_id = 0;
	function swiper(item , i){
		if(swiperConfig.s_id == i){
			return;
		}
		clearTimeout(swiperConfig.timeOut);
		
		domClass.remove(query(".hd li",item)[swiperConfig.s_id],"on");
		domClass.add(query(".hd li",item)[i],"on");
	
		var showItem = query(".bd li",item)[i];
		domStyle.set(showItem,"display","list-item");
		fx.fadeIn({
			node:showItem,
			onEnd:function(){
			}
		}).play();
		
		var hideItem = query(".bd li",item)[swiperConfig.s_id];
		fx.fadeOut({
			node:hideItem,
			onEnd:function(){
				 domStyle.set(hideItem,"display","none");
			}
		}).play();
		
		//当有轮播时间时根据时间自动轮播
		if(swiperConfig.roundTime != null){
			swiperConfig.timeOut = setTimeout(swiperConfig.nextSlide,swiperConfig.roundTime);
		}
		
		swiperConfig.s_id = i;
		
	};
	//下一页轮播
	swiperConfig.nextSlide = function(){
		if((swiperConfig.s_id+1) < swiperConfig.max_id){
			swiper(swiperConfig.slide,swiperConfig.s_id+1)
		}else{
			swiper(swiperConfig.slide,0)
		}
	};
	//上一页轮播
	swiperConfig.preSlide = function(){

		if(swiperConfig.s_id > 0){
			swiper(swiperConfig.slide,swiperConfig.s_id-1)
		}else{
			swiper(swiperConfig.slide,swiperConfig.max_id)
		}
	};
	
	var _swiper = function(config){
		swiperConfig.roundTime = config.time;
		//初始化轮播图
		query(config.node).forEach(function(item){
			swiperConfig.slide = item;
			var hg = domStyle.get(item,"height")+"px";
			var wt = domStyle.get(item,"width")+"px";
			
			var innerHtml = "";
			domStyle.set(query(".bd ul",item)[0],{
				"position":"relative",
				"height":hg,
				"width":wt
			});
			var itembds = query(".bd li",item);
			swiperConfig.max_id = itembds.length;
			itembds.forEach(function(itembd,i){
				domStyle.set(itembd,{
					"position":"absolute",
					"left":"0",
					"top":"0"
				});
				
				innerHtml += "<li ";
				if(i==0){
					innerHtml += "class='on'";
				}else{
					 domStyle.set(itembd,{
						 "display":"none",
						 "opacity":"0"
					});
				}
				innerHtml +=">"+(i+1)+"</li>";
			});
			query(".hd ul",item)[0].innerHTML = innerHtml;
			query(".hd li",item).forEach(function(itemhd,i){
				on(itemhd,"click",function(){
					swiper(item, i);
				});
			})
			
			
		});
		//当有轮播时间时根据时间自动轮播
		if(swiperConfig.roundTime != null){
			swiperConfig.timeOut = setTimeout(swiperConfig.nextSlide,swiperConfig.roundTime);
		}
		return swiperConfig;
		
	}
	
	return _swiper;
})