function step2(){
	var box = document.querySelector("#step2"),
		input = box.querySelectorAll("input"),
		next = box.querySelector("a"),
		stepAction = actions();
	for(var i = 0, l = input.length; i < l; i++){
		stepAction.hiddenNotice(input[i]);
	}
	if(STEP.exchange == 'njs'){
		next.setAttribute("act","submit");
	}
	Event.add(box,"click",function(e){
		var tar = Event.target(e);
		tar.getAttribute("stop") && Event.stop(e);
		tar.getAttribute("act") && stepAction[tar.getAttribute("act")] && stepAction[tar.getAttribute("act")](tar,e);
	})
}