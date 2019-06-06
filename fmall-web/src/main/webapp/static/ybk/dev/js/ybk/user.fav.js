function fav(){
	var req = request();
	function add(code,ex){
		var favlist = JSON.parse(cookies.get('favlist')),ishad;
		for(var i = 0, l = favlist.length; i < l; i++){
			if(favlist[i][1] == code && favlist[i][0] == ex){
				ishad = 1;
			}
		}
		!ishad && favlist.push([ex,code])
		 && cookies.set({
			name : "favlist",
			value : JSON.stringify(favlist),
			path : "/",
			time : 60 * 60 * 24 * 365 * 10//10年 
		})
	}

	function isfav(code,ex){
		var favlist = JSON.parse(cookies.get('favlist')),ishad;
		for(var i = 0, l = favlist.length; i < l; i++){
			if(favlist[i][1] == code && favlist[i][0] == ex){
				ishad = 1;
			}
		}
		return ishad;
	}

	function del(code,ex){
		var favlist = JSON.parse(cookies.get('favlist')),id = null;
		for(var i = 0, l = favlist.length; i < l; i++){
			if(favlist[i][1] == code && favlist[i][0] == ex){
				id = i;
				break;
			}
		}

		if(id !== null){
			favlist.splice(id,1);
			cookies.set({
				name : "favlist",
				value : JSON.stringify(favlist),
				path : "/",
				time : 60 * 60 * 24 * 365 * 10//10年 
			})
		}
	}

	function list(callback){
		var favlist = JSON.parse(cookies.get('favlist')),
			data = {};

		for(var l = favlist.length; l--; ){
			if(!data[favlist[l][0]]){
				data[favlist[l][0]] = [favlist[l][1]];
			}else{
				data[favlist[l][0]].push(favlist[l][1]);
			}
		}
		req.ajax({
			type : 'GET',
			url : '/api/ybk/get_collection_infos',
			data : {condition : JSON.stringify(data)},
			success : function(data){
				data.success && callback && callback(data.data);
			}
		})
		setTimeout(function(){list(callback)},60000)
	}

	(function(){
		if(!cookies.get('favlist')){
			cookies.set({
				name : "favlist",
				value : "[]",
				path : "/",
				time : 60 * 60 * 24 * 365 * 10//10年 
			})
		}
	})()
	return {
		add : add,
		del : del,
		list : list,
		isfav : isfav
	}
}

