function switchbtn(box,tag,idx){
	var btnbox = document.querySelector(box),
		btns = btnbox.querySelectorAll(tag),
		current = btns[(idx || 1) - 1];
	current.className = 'on';
	Event.add(btnbox,'click',function(e){clickSwitch(e)});
	function clickSwitch(e){
		var tar = Event.target(e);
		if(tar != current && tar.nodeName.toLowerCase() == tag.toLowerCase()){
			current.className = '';
			current = tar;
			current.className = 'on';
		}
	}
}