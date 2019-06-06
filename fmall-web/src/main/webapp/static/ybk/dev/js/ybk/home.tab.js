function tabLoopMove(bigbox,tabbox,def){
	var bigbox = document.querySelector(bigbox),
		tabbox = document.querySelector(tabbox),
		size = tabbox.parentNode.offsetWidth,
		li = tabbox.querySelectorAll('li'),
		litab = tabbox.querySelectorAll('a'),
		def = def*1 || 1,
		step = [0],
		current = 0,
		tab = {};
	function init(){
		def -= 1;
		tab.tab = litab[def];
		tab.tab.className = 'on';
		Event.add(bigbox,'click',function(e){
			var tar = Event.target(e);
			Event.stop(e);
			tab[tar.getAttribute('act')] && tab[tar.getAttribute('act')](tar);
		})
		for(var cw = 0,tw = 0,i = 0, l = li.length; i < l; i++){
			if(cw + li[i].offsetWidth < size){
				cw += li[i].offsetWidth;
			}else{
				step.push(tw*-1);
				cw = li[i].offsetWidth;
			}
			tw += li[i].offsetWidth;
		}
	}
	tab.next = function(){
		if(current == step.length-1)return;
		current += 1;
		console.log(current)
		tabbox.style.left = step[current] + 'px';
	}
	tab.pre = function(){
		if(current == 0)return;
		current -= 1;
		tabbox.style.left = step[current] + 'px';
	}
	tab.switch = function(tar){
		tab.tab.className = '';
		tab.tab = tar;
		tab.tab.className = 'on';
	}
	init();
}