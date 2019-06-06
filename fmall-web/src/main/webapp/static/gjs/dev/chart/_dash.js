function _dash(){
	var _this = this;
	if(!_this.lastMoveToLocation){
		_this.moveForDashed = function(x,y){
			_this.moveTo(x,y);
			_this.lastMoveToLocation.x = x;
			_this.lastMoveToLocation.y = y;
		}
	}
	_this.lastMoveToLocation = {}; 
	_this.dashedLineTo = function(x, y, dashLength){
		var dashLength = dashLength === undefined ? 3 : dashLength,
			startX     = _this.lastMoveToLocation.x,
			startY     = _this.lastMoveToLocation.y,
			deltaX     = x - startX,
			deltaY     = y - startY,
			numDashes  = Math.floor(Math.sqrt(deltaX * deltaX + deltaY * deltaY) / dashLength),
			tx,ty;
		for (var i = 0; i < numDashes; ++i) {
			tx = startX + (deltaX / numDashes) * i;
			ty = startY + (deltaY / numDashes) * i;
			if(i % 2 === 0){
				_this.moveForDashed(tx,ty)
			}else{
				_this.lineTo(tx, ty);
			}
		}
		_this.moveForDashed(x, y);
	}
}