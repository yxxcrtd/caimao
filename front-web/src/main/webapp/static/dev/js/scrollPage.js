// JavaScript Document
function scrollInit(doms){
	var ArrDoms=[].slice.call(doms,0);
	var newData;
	var staticH=true;
	var timest;
	var dir=1;
	var g_top=0;
	var mms=true;
	var tance=scrollInit.tance||10;
	doms.each(function(){
		$(this).height($(window).height());	
	})
	
	newData=[].slice.call(map(doms,function(i,ele){
		return $(ele).offset().top;	
	}),0);
	var c_num=0;
	function map(arr,fn){
		if(Array.prototype.map){
			return arr.map(fn);	
		}	
		return (function(arr,fn){
			var newArr=[];
			for(var i=0; i<arr.length; i++){
				newArr[i]=fn.call(arr,i,arr[i]);	
			}	
			return newArr;
		})(arr,fn);
	}
	function animate(){
		if(staticH) return;
		clearTimeout(timest);
		if(dir<0){
			if(c_num<0){
				staticH=true;
				mms=true;
				c_num=0;
				return;	
			}
			if(g_top+10>=-newData[c_num])	{
				staticH=true;
				mms=true;
				g_top=-newData[c_num];
				$(".func_area").css("top",g_top+"px");
				return;	
			}	
		}else if(dir>0){
			if(c_num>=(newData.length)){
				staticH=true;
				mms=true;
				c_num=newData.length-1;
				return;	
			}
			if(g_top-10<=-newData[c_num])	{
				staticH=true;
				mms=true;
				g_top=-newData[c_num];
				$(".func_area").css("top",g_top+"px");
				return;	
			}	
		}
		g_top-=(tance*dir);
		$(".func_area").css("top",g_top+"px");
		
		timest=setTimeout(animate,1);
		
	}
	
	function wheel(fn){
		if(this.addEventListener){
			this.addEventListener("DOMMouseScroll",fn,false);	
		}
		this.onmousewheel=fn;	
	}
	document.wheel=wheel;
	
	function changePageIndex(){
		c_num+=dir;
		if(c_num<0){
			c_num=0;	
		}
		if(c_num>newData.length-1){
			c_num=newData.length-1	
		}
	}
	return !function(){
		document.wheel(function(ev){
			ev=ev||window.event;
			if(staticH&&mms){
				staticH=false;
				mms=false;
				if(ev.detail){
					dir=ev.detail/Math.abs(ev.detail);	
				}else{
					dir=-ev.wheelDelta/Math.abs(ev.wheelDelta);
				}	
				changePageIndex();
				animate();	
			}
			
		})
	}();
}