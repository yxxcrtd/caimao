function focus(focusBox,pic,btn,timer){
	var pic = pic.split('/'),
		btn = btn.split('/'),
		focusBox = document.querySelector(focusBox),
		picBox = document.querySelector(pic[0]),
		pics = picBox.querySelectorAll(pic[1]),
		btnBox = document.querySelector(btn[0]),
		timer = timer || 5000,
		idx = 0,
		str = "",btn,cur,timeouter,cool;

	function play(n){
		btn[cur] && (btn[cur].className = "");
		pics[cur] && (pics[cur].className = "");
		cur = n;
		btn[cur] && (btn[cur].className = "on");
		pics[cur] && (pics[cur].className = "on");
	}
	function auto(){
		var n = cur + 1;
		coll = setTimeout(function(){
			timeouter = setTimeout(function(){
				if(n >= pics.length)n = 0;
				play(n);
				auto();
			},timer);
		},30);
	}
	function stop(){
		timeouter && clearTimeout(timeouter);
		timeouter = null;
		coll && clearTimeout(coll);
		coll = null;
	}
	!function(){
		for(var i = 0, l = pics.length; i < l; i++){
			str += "<span idx='"+i+"'></span>";
		}
		if(l < 2)return (pics[0].className = "on");
		btnBox.innerHTML = str;
		btn = btnBox.querySelectorAll("span");
		play(0);
		auto();
		Event.add(btnBox,'click',function(e){
			var tar = Event.target(e);
			if(tar.nodeName.toLowerCase() == 'span'){
				play(tar.getAttribute('idx')*1);
			}
		})
		Event.add(focusBox,"mouseover",stop);
		Event.add(focusBox,"mouseout",auto);
	}()
}