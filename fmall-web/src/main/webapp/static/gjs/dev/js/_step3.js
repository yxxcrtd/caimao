function step3(){
	var box = document.querySelector("#step3"),
		stepAction = actions();
	Event.add(box,"click",function(e){
		var tar = Event.target(e);
		tar.getAttribute("stop") && Event.stop(e);
		tar.getAttribute("act") && stepAction[tar.getAttribute("act")] && stepAction[tar.getAttribute("act")](tar,e);
	})
}