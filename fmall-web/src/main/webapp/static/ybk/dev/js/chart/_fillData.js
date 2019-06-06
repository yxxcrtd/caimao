function _fillData(box){
	var _this = this,
		stockChart = _this,outRule,
		tmp = {
				timeShare: '<%if(outRule){%><em><%=data.name%></em><%}%>'
					+'<em><%=totime(obj.drawData.datetime,"m-d h:i:s")%></em>'
					+'<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[0]%>">最新：<%=(obj.drawData.pri*1).toFixed(2)%></em>'
					+'<% if(obj.drawData.avg !== null && obj.drawData.type*1 && 0){%>'
						+'<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[1]%>">均价：<%=(obj.drawData.avg*1).toFixed(2)%></em>'
					+'<% } %>'
					+'<%if(outRule){%><em style="color:<%=(obj.pri*1 > cfg.data.DATA2.pre*1 ? cfg.schemes[cfg.user.schemes].rise : cfg.schemes[cfg.user.schemes].fall)%>">量：<%=obj.drawData.vol%></em><%}%>',
				

				k: '<%if(outRule){%><em><%=data.name%></em><%}%>'
					+ '<%if(data.ind[i]){'
						+'var j = 0; for(k in data.ind[i]){%>'
							+ '<% if(data.ind[i][k]){%>'
								+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[j++]%>"><%=k%>:<%=cutFixed(data.ind[i][k],2)%></em>' 
							+ '<%}%>'
						+ '<%}%>'
					+ '<%}%>',

				vol: '<em style="color:<%=(obj.cur * 1 > obj.open ? cfg.schemes[cfg.user.schemes].rise :'
					+ 'cfg.schemes[cfg.user.schemes].fall)%>">总手：<%=obj.vol%></em>'
					+ '<%if(data.ind[i]){'
						+'var j = 0; for(k in data.ind[i]){%>'
							+ '<% if(data.ind[i][k]){%>'
								+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[j++]%>"><%=k%>:<%=cutFixed(data.ind[i][k],2)%></em>' 
							+ '<%}%>'
						+ '<%}%>'
					+ '<%}%>',
				

				MACD: '<em>MACD(12,26,9)</em>' 
					+ '<%if(outRule){%>'
					+ '<em style="color:<%=(cfg.schemes[cfg.user.schemes][obj.macd > 0 ? "MACDrise" : "MACDfall"])%>">MACD：<%=obj.macd%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[0]%>">DIFF：<%=obj.dif%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[1]%>">DEA：<%=obj.dea%></em>'
					+ '<%}%>',
				

				KDJ: '<em>KDJ(9,3,3)</em>' 
					+ '<%if(outRule){%>'
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[0]%>">K：<%=cutFixed(obj.k,2)%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[1]%>">D：<%=cutFixed(obj.d,2)%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[2]%>">J：<%=cutFixed(obj.j,2)%></em>'
					+ '<%}%>',

				RSI: '<em>RSI(6,12,24)</em>' 
					+ '<%if(outRule){%>'
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[0]%>">RSI6：<%=cutFixed(obj.rsi1,2)%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[1]%>">RSI12：<%=cutFixed(obj.rsi2,2)%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[2]%>">RSI24：<%=cutFixed(obj.rsi3,2)%></em>'
					+ '<%}%>',
				
				kbox : '<ul>'
					+ '<li>时间：<i><%=totime(obj.date,"y-m-d")%></i></li>'
					+ '<li>开盘：<i class="<%if(obj.open*1 > pre){%>red<%}else if(obj.open*1 <pre){%>green<%}%>"><%=obj.open%></i></li>' 
					+ '<li>最高：<i class="<%if(obj.high*1 > pre){%>red<%}else if(obj.high*1 <pre){%>green<%}%>"><%=obj.high%></i></li>' 
					+ '<li>最低：<i class="<%if(obj.low*1 > pre){%>red<%}else if(obj.low*1 <pre){%>green<%}%>"><%=obj.low%></i></li>' 
					+ '<li>收盘：<i class="<%if(obj.cur*1 > pre){%>red<%}else if(obj.cur*1 < pre){%>green<%}%>"><%=obj.cur%></i></li>' 
					+ '<li>涨幅：<i class="<%if(obj.rate > 0){%>red<%}else if(obj.rate < 0){%>green<%}%>"><%=cutFixed(obj.rate/100,3)%>%</i></li>'
					+ '<li>振幅：<i class="blue"><%=cutFixed((obj.high - obj.low)*100/pre,3)%>%</i></li>'
					+ '</ul>',

				MTM: '<em>MTM(12,6)</em>' 
					+ '<%if(outRule){%>'
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[0]%>">MTM：<%=cutFixed(obj.mtm,2)%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[1]%>">MTMMA：<%=cutFixed(obj.mtmma,2)%></em>' 
					+ '<%}%>'
			};
	_this.fillCurData = function(idx){
		var data,i;
		outRule = box.size.w > stockChart.options.user.outRuleWidth;

		if(stockChart.options.user.showInfo){
			for(var k in stockChart.fillBoxs){
				data = stockChart.fillData[k];
				i    = idx === null ? data.data.length - 1 : idx;
				i = data.data.length - i - 1;
				if(data.data[i]){
					stockChart.fillBoxs[k].innerHTML = rentmpl(
						tmp[k],
						{
							data : data,
							code : _this.options.user.stockID,
							i : i,
							obj : data.data[i],
							cfg : stockChart.options,
							outRule : outRule
						}
					);
					if(k === 'k'){
						stockChart['kBox'] && (stockChart.kBox.innerHTML = rentmpl(
							tmp['kbox'],
							{
								data : data,
								pre : data.data[i+1] ? data.data[i+1].cur*1 : data.data[i].open*1,
								code : _this.options.user.stockID,
								i : i,
								obj : data.data[i],
								cfg : stockChart.options,
								outRule : outRule
							}
						));
					}
				}
			}

		}
	}
}
