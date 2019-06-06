function sideBar(){
	
}
function service(id){
	var box = document.querySelector(id),
		isShow = 0;
	Event.add(box,"click",function(e){
		var tar = Event.target(e);
		if(isShow){
			isShow = 0;
			box.className = "kefuHidden";
		}else{
			isShow = 1;
			box.className = "kefu";
		}

	})
	Event.add(document,"click",function(e){
		var tar = Event.target(e),
			att = attFather(tar,"kefu");
		if(att && att[1] == "kefu"){
			return;
		}
		isShow = 0;
		box.className = "kefuHidden";
	})
}
function userInfo(id,homeUrl){
	var user = document.querySelector(id);
	if(!token){
		user.innerHTML = '<a onclick="window.location.href=\''+homeUrl+'/user/login.html?return='+encodeURIComponent(window.location.href)+'\';" href="javascript:void(0);">登录</a>'
					+'<span>|</span><a onclick="window.location.href=\''+homeUrl+'/user/register.html?return='+encodeURIComponent(window.location.href)+'\';" href="javascript:void(0);">注册</a>';
	}else{
	    h.option = h.api.get_user_info(token,{callback:function(d){
	    	if(d.success){
	    		user.innerHTML = '<a href="'+homeUrl+'/user/info.html">'+(d.data.name || (d.data.mobile.substr(0, 3) + '****' + d.data.mobile.substr(8, 3)))+'</a><span>|</span><a href="https://www.caimao.com/user/logout.html">退出</a>';
	    	}
	    }});
	    h.request(h.option);
	}
}