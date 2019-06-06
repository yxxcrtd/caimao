define([
    'app/controllers/Helper',
    'dojo/_base/fx',
    'dijit/registry',
    'dojo/dom',
    'dojo/on',
    'app/common/Ajax',
    'dojo/query',
    'app/common/Global',
    'dojo/dom-style',
    'dojo/dom-class',
    'dojo/dom-construct',
    'dojo/mouse',
    'dijit/focus',
    'dojo/cookie',
    'app/views/member/AvatarPanel',
    'app/ux/GenericImageCropper',
    'dojox/form/Uploader',
    'app/ux/GenericMiniMsgBox',
    'dojo/has',
    'dojo/_base/sniff',
    'app/views/common/SideMenu',
    'dojo/_base/config',
    'app/common/Data',
    'dojo/when',
    'app/jquery/AjaxFileUpload',
    'dojo/domReady!'
], function (Helper, fx, registry, dom, on, Ajax, query, Global, domStyle, domClass,
             domConstruct, mouse, focusUtil, cookie, AvatarPanel, ImageCropper, Uploader,
             MiniMsgBox, has, sniff, SideMenu, Config, Data, when, AjaxFileUpload) {

    var config = {};
    var navBarItems = query('.subnav-item', 'subheader'),
        sideBarItems = query('.list-lside-item', 'sidebar'),
        topbarUsername = dom.byId('topusername'),
        logoutBtn = dom.byId('logout'),
        content, uploader, msgBox, fileName;

    function initView() {

//        when(Data.getUserExtra()).then(function(data) {
//            if (data.userPhoto) {
//                content.displayNode.src = Config.baseUrl+ 'avatars/' + data.userPhoto;
//            }
//        });
        //左边导航栏
        var sideMenu = new SideMenu({
            active: '3 7'
        });
        sideMenu.placeAt('sidemenuctn');

        content = new AvatarPanel({
            upload: function () {
                var data = {
                    'file_name': fileName,
                    'h': parseInt(uploader.getInfoActual()['h']),
                    'w': parseInt(uploader.getInfoActual()['w']),
                    'l': parseInt(uploader.getInfoActual()['l']),
                    't': parseInt(uploader.getInfoActual()['t'])
                };
                //data.append('file', content.fileSelectorNode.files[0]);
                //data.append('h', parseInt(uploader.getInfoActual()['h']));
                //data.append('w', parseInt(uploader.getInfoActual()['w']));
                //data.append('l', parseInt(uploader.getInfoActual()['l']));
                //data.append('t', parseInt(uploader.getInfoActual()['t']));
                content.uploadBtn.loading(true);
                Ajax.post(Global.baseUrl + '/user/avatar/set1', data).then(function (response) {
                    content.uploadBtn.loading(false);
                    if (!msgBox) {
                        msgBox = new MiniMsgBox();
                        msgBox.placeAt(document.body);
                    }
                    if (response.success) {
                        msgBox.setValues({
                            type: 'success',
                            text: '上传成功'
                        });
                        msgBox.show();
                        updateTopbarAvatar(response.data);
                        setTimeout(function() {
                        	location.href = Global.baseUrl + '/home/index.htm';
                        }, 2000);
                    } else {
                        msgBox.setValues({
                            type: 'error',
                            text: '上传失败'
                        });
                        msgBox.show();
                    }

                });
            }
        });
        content.placeAt('titlectn', 'after');

        uploader = new ImageCropper({
            keepSquare: true,
            cropSize: 96
        });

        on(uploader, 'change', function () {
            var info = uploader.getInfo();
            var rate = 96 / info.w;
            domStyle.set(content.displayNode, {
                width: info.cw * rate + 'px'
                , height: info.ch * rate + 'px'
                , marginLeft: -info.l * rate + 'px'
                , marginTop: -info.t * rate + 'px'
            });
        });

        $("#avatarfile").AjaxFileUpload({
            action: Global.baseUrl + '/user/avatar/preset',
            onSubmit: function() {
                content.previewBtn.loading(true);
            },
            onComplete: function(filename, response) {
                reloadImg(response.msg);
                content.previewBtn.loading(false);
                fileName = /(.*\..*)\?+.*/.exec(response.msg)[1];
            }
        });
    }

    function reloadImg(imgPath) {
        content.uploadBtn.set('disabled', false);
        var fullPath = Global.baseUrl + '/temp/' + imgPath;
        uploader.setImage(fullPath);
        uploader.placeAt(content.previewNode);
        uploader.domNode.style.display = 'block';
        content.displayNode.src = fullPath;

    }

    function handleFile(file, displayEl) {
        var imageType = /image.*/;

        if (!file.type.match(imageType)) {
            return;
        }

        var reader = new FileReader();
        reader.onload = function (e) {
            uploader.setImage(e.target.result);
            uploader.placeAt(content.previewNode);
            uploader.domNode.style.display = 'block';
            content.displayNode.src = e.target.result;
        };
        reader.readAsDataURL(file);
    }

    function updateTopbarAvatar(value) {
        dom.byId('topbaravatar').src = Config.baseUrl + 'avatars/' + value;
    }

    function addListeners() {
        var me = this;
        on(content.fileSelectorNode, 'change', function (e) {
            //var file = this.files[0];
            //if (file) {
            //	content.uploadBtn.set('disabled', false);
            //	handleFile(this.files[0], content.displayNode);
            //} else {
            //	content.uploadBtn.set('disabled', true);
            //	uploader.domNode.style.display = 'none';
            //	content.displayNode.src = dom.byId('topbaravatar').src;
            //	content.displayNode.style.width = 96 + 'px';
            //	content.displayNode.style.height = 96 + 'px';
            //	content.displayNode.style.marginLeft = 0 + 'px';
            //	content.displayNode.style.marginTop = 0 + 'px';
            //}
            //$.ajaxFileUpload({
            //    url: Global.baseUrl + '/user/avatar/preset', //用于文件上传的服务器端请求地址
            //    secureuri: false, //是否需要安全协议，一般设置为false
            //    fileElementId: 'fileSelector', //文件上传域的ID
            //    dataType: 'json', //返回值类型 一般设置为json
            //    success: function (data, status)  //服务器成功响应处理函数
            //    {
            //        uploader.setImage(data.imgurl);
            //        uploader.placeAt(content.previewNode);
            //        uploader.domNode.style.display = 'block';
            //        content.displayNode.src = data.imgurl;
            //        if (typeof (data.error) != 'undefined') {
            //            if (data.error != '') {
            //                alert(data.error);
            //            } else {
            //                alert(data.msg);
            //            }
            //        }
            //    },
            //    error: function (data, status, e)//服务器响应失败处理函数
            //    {
            //        alert(e);
            //    }
            //});
        });
    }

    return {
        init: function () {
            initView();
            Helper.init(config);
            addListeners();
        }
    }
});