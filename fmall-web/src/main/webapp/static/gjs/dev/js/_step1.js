function step1(){
	var box = document.querySelector("#step1"),
		input = box.querySelectorAll("input"),
		bank = box.querySelector("#bank"),
		bankNo = box.querySelector("#bankNo"),
		ul = box.querySelector("ul"),
		stepAction = actions(),
		data = {},timer;
	for(var i = 0, l = input.length; i < l; i++){
		stepAction.hiddenNotice(input[i]);
	}
	Event.add(box,"change",function(e){
		var tar = Event.target(e);
		if(tar.name == "exchange"){
			data.exchange = tar.value;
			stepAction.reDrawStep(data.exchange,bank,bankNo,ul);
		}
	})
	Event.add(bank,'focus',function(){
		timer && clearTimeout(timer);
		if(STEP.exchange == "sjs")return;
		ul.style.display = "block";
	})
	Event.add(bank,'blur',function(){
		timer = setTimeout(function(){
			ul.style.display = "none";
		},200);
	})
	Event.add(box,"click",function(e){
		var tar = Event.target(e);
		tar.getAttribute("stop") && Event.stop(e);
		tar.getAttribute("act") && stepAction[tar.getAttribute("act")] && stepAction[tar.getAttribute("act")](tar,e);
	})
	Event.add(document,"click",function(){
		ul.style.display = "none";
	})
}