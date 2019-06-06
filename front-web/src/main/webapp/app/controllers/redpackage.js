var red_package_fnc = {
    ajax:function(opt){
        var xhr = this.createXhrObject();
        xhr.onreadystatechange = function(){
            if(xhr.readyState!=4) return ;
            (xhr.status===200 ?
                opt.success(xhr.responseText,xhr.responseXML):
                opt.error(xhr.responseText,xhr.status));
        };
        xhr.open(opt.type,opt.url,true);
        if(opt.type!=='post'){
            opt.data=null;
            xhr.setRequestHeader("X-Requested-With","XMLHttpRequest");
        }else{
            xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        }
        opt.data = this.parseQuery(opt.data);
        xhr.send(opt.data);
    },
    post:function(url, data, success){
        var popt = {
            url:url,
            type:'post',
            data:data,
            success:success,
            error:function(data){}
        };
        this.ajax(popt);
    },
    get:function(url, success){
        var gopt = {
            url:url,
            type:'get',
            success:success,
            error:function(){}
        };
        this.ajax(gopt);
    },
    createXhrObject:function(){
        var methods = [
            function(){ return new XMLHttpRequest();},
            function(){ return new ActiveXObject('Msxml2.XMLHTTP');},
            function(){ return new ActiveXObject('Microsoft.XMLHTTP');}
        ];
        var len = methods.length;
        for(var i=0;i < len;i++){
            try{
                methods[i]();
            }catch(e){
                continue;
            }
            this.createXhrObject = methods[i];
            return methods[i]();
        }
        throw new Error('Could not create an XHR object.');
    },
    parseQuery:function(json){
        if(typeof json == 'object'){
            var str = '';
            for(var i in json){
                str += "&"+i+"="+encodeURIComponent(json[i]);
            }
            return str.length==0 ? str : str.substring(1);
        }else{
            return json;
        }
    },
    close_rp:function(){
        document.getElementById("rp_box").style.display = "none";
    },
    open_rp:function(){
        this.get("/activity/open_rp", function(response){
            response = eval("("+ response +")");
            if(response.success && response.data){
                document.getElementById("rp_pre_info").style.display = "none";
                document.getElementById("rp_pre_btn").style.display = "none";
                document.getElementById("rp_get_info").style.display = "block";
                document.getElementById("rp_get_btn").style.display = "block";
                document.getElementById("rp_money").innerHTML = response.data + "元";
            }else{
                alert(response.msg);
            }
        });
    },
    show_rp:function(){
        var html = '<div style="position: absolute;z-index: 999;width: 660px;height: 500px;left: 50%;top: 50%;margin-left: -330px;margin-top: -250px;background: url(/app/resources/image/redpackage.png)" id="rp_box">' +
            '<div style="position: absolute;width: 45px;height: 45px;top: 28px;right: 35px;cursor: pointer;" onclick="red_package_fnc.close_rp()"></div>' +
            '<div style="position: absolute;top: 220px;left: 200px;color: #fff;" id="rp_pre_info">' +
            '<p style="line-height: 30px;">亲爱的财猫用户：</p>' +
            '<p style="line-height: 30px;">　　感谢您一如既往的陪伴与支持</p>' +
            '<p style="line-height: 30px;">　　股市虽无情，生活却有情。</p>' +
            '<p style="line-height: 30px;">　　你做的选择，我都会陪你。</p>' +
            '<p style="line-height: 30px;">　　一个现金红包已放入您的账户。</p>' +
            '<p style="text-align: right;line-height: 30px;width: 260px;">财猫</p>' +
            '</div>' +
            '<div style="position: absolute;top: 410px;left: 226px;cursor: pointer;" id="rp_pre_btn">' +
            '<div style="height: 45px;width: 200px;border: none;border-radius: 5px;background: #febc42;color: #d0272f;font-size: 18px;line-height: 45px;text-align: center;" onmouseover="this.style.background=\'#e59c15\';" onmouseout="this.style.background=\'#febc42\'" onclick="red_package_fnc.open_rp()">打开红包</div>' +
        '</div>' +
        '<div style="position: absolute;top: 220px;left: 225px;color: #fff;display: none" id="rp_get_info">' +
        '<p style="text-align: center;width: 200px;">恭喜您获得</p>' +
        '<p style="width: 200px;height: 60px;margin-top: 20px;text-align: center;line-height: 50px;"><span style="color: #ffcc00;font-size: 50px;" id="rp_money">10元</span>　红包</p>' +
        '</div>' +
        '<div style="position: absolute;top: 345px;left: 226px;cursor: pointer;display: none" id="rp_get_btn">' +
        '<div onclick="location.href=\'/home/index.htm\'" style="height: 45px;width: 196px;border: none;border-radius: 5px;background: #febc42;color: #d0272f;font-size: 18px;line-height: 45px;text-align: center;margin-bottom: 15px;margin-left: 2px;" onmouseover="this.style.background=\'#e59c15\';" onmouseout="this.style.background=\'#febc42\'">查看</div>' +
        '<div style="height: 45px;width: 200px;border: none;border-radius: 5px;background:url(/app/resources/image/redpackage_share.jpg);color: #d0272f;font-size: 18px;line-height: 45px;text-align: center;" onclick="red_package_fnc.share()"></div>' +
        '</div>' +
        '</div>';
        var rp_box = document.createElement("div");
        rp_box.innerHTML = html;
        document.body.appendChild(rp_box);
    },
    is_show_rp:function(){
        this.get("/activity/get_show_rp", function(response){
            response = eval("("+ response +")");
            if(response.success && response.data){
                red_package_fnc.show_rp();
            }
        });
    },
    share:function (){
        var share_url   = encodeURIComponent('https://www.caimao.com?i=59'),
            share_title = encodeURIComponent('侠之大者，为国接盘！@财猫金融 免费送我现金红包~ 各位兄台请大侠吃饭来者不拒~'),
            weibo_url = 'http://service.weibo.com/share/share.php?url=' + share_url+'&type=icon&language=zh_cn&appkey=gtKmt&searchPic=true&style=simple&title=' + share_title;
        window.open(weibo_url, '_blank', 'width=500,height=550');
    }
};
red_package_fnc.is_show_rp();