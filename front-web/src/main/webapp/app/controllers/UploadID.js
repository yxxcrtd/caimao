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
    'app/ux/GenericTextBox',
    'app/common/Date',
    'app/views/member/UploadIDPanel',
    'app/views/common/SideMenu',
    'app/ux/GenericImageCropper',
    'dojox/form/Uploader',
    'app/ux/GenericMiniMsgBox',
    'app/ux/GenericPrompt',
    'dojo/has',
    'dojo/_base/sniff',
    'app/ux/GenericTooltip',
    'app/jquery/$',
    'dojo/domReady!'
], function (Helper, fx, registry, dom, on, Ajax, query, Global, domStyle, domClass,
             domConstruct, mouse, focusUtil, cookie, TextBox, DateUtil, UploadIDPanel, SideMenu, ImageCropper, Uploader, MiniMsgBox, Prompt, has, sniff, Tooltip) {

    var config = {};
    var navBarItems = query('.subnav-item', 'subheader'),
        sideBarItems = query('.list-lside-item', 'sidebar'),
        topbarUsername = dom.byId('topusername'),
        logoutBtn = dom.byId('logout'),
        content, uploader, msgBox;


    var randomId = (function () {
        var id = 0;
        return function () {
            return "_AjaxFileUpload" + id++;
        };
    })();

    function createIframe() {
        var id = randomId();

        // The iframe must be appended as a string otherwise IE7 will pop up the response in a new window
        // http://stackoverflow.com/a/6222471/268669
        $("body")
            .append('<iframe src="javascript:false;" name="' + id + '" id="' + id + '" style="display: none;"></iframe>');

        return $('#' + id);
    }

    function createForm(iframe) {
        return $("<form />")
            .attr({
                method: "post",
                enctype: "multipart/form-data",
                target: iframe[0].name
            })
            .hide()
            .appendTo("body");
    }

    function doAjax(url, data, file1, file2, onComplete) {
        var iframe = createIframe();
        var form = createForm(iframe);
        form[0].action = url;
        for (var variable in data) {
            $("<input />")
                .attr("type", "hidden")
                .attr("name", variable)
                .val(data[variable])
                .appendTo(form);
        }
        var id1 = file1.attr('id'),
            clone1 = file1.removeAttr('id').clone().attr('id', id1),
            id2 = file2.attr('id'),
            clone2 = file2.removeAttr('id').clone().attr('id', id2);
        clone1.insertBefore(file1);
        clone2.insertBefore(file2);
        iframe.bind("load", {}, function () {
            var doc = (iframe[0].contentWindow || iframe[0].contentDocument).document,
                response = doc.body.innerText || doc.body.textContent;
            if (response) {
                response = $.parseJSON(response);
            } else {
                response = {};
            }
            onComplete(response);
            form.remove();
            iframe.remove();
        });
        form.append(file1).append(file2);
        form.submit();
    }

    function initView() {

        content = new UploadIDPanel(
            {
                upload: function () {
                    if (content.isValid()) {
                        var files = query('input[type=file]', content.domNode);
                        if(files[0].files && files[0].files[0].size>5242880){ // IE8 not support
                        	setTimeout(function() {
                        		Tooltip.show('图片大小最大为5M', content.uploadBtn.innerNode, 'warning');
                        	}, 200);
                        	
                            return false;
                        }else if(files[1].files && files[1].files[0].size>5242880){
                        	setTimeout(function() {
                        		Tooltip.show('图片大小最大为5M', content.uploadBtn.innerNode, 'warning');
                        	}, 200);
                        	
                            return false;
                        }
                        doAjax(Global.baseUrl + '/user/idcard/upload', {
                            'userRealName': content.realNameFld.get('value'),
                            'idcard': content.IDNoFld.get('value')
                        }, $(files[0]), $(files[1]), function (response) {
                            if (response.success) {
                                content.uploadBtn.loading(false);
                                dom.byId('contentctn').style.display = 'none';
                                dom.byId('contentctnTTT').style.display = 'block';
                                var successPanel = new Prompt({
                                    type: 'success',
                                    msg: '恭喜您，身份证上传成功！',
                                    subMsg: '<span style="color: #666666">后台管理员正在紧张处理，请耐心等待。如有疑问请拨打客服电话 ' +
                                    '<b class="am-ft-orange">' + Global.hotline + '</b></span>',
                                    linkText: '返回会员中心',
                                    link: 'home/index.htm'
                                });
                                successPanel.placeAt('contentctnTTT', 'first');
                                successPanel.show();
                            } else {
                                setTimeout(function() {
                                    Tooltip.show(response.msg, content.uploadBtn.innerNode, 'warning');
                                }, 1000);
                                
                            }

                        });
                        //var data = new FormData();
                        //data.append('positive', content.fileSelectorNode.files[0]);
                        //data.append('opposite', content.fileSelectorOppositeNode.files[0]);
                        //data.append('userRealName',content.realNameFld.get('value'));
                        //data.append('idcard',content.IDNoFld.get('value'));
                        //content.uploadBtn.loading(true);
                        //Ajax.post(Global.baseUrl + '/user/idcard/upload', data).then(function(response) {
                        //	if (response.success) {
                        //		content.uploadBtn.loading(false);
                        //		   dom.byId('contentctn').style.display = 'none';
                        //         var successPanel = new Prompt({
                        //             type: 'success',
                        //             msg: '恭喜您，身份证上传成功！',
                        //             subMsg: '<span style="color: #666666">后台管理员正在紧张处理，请耐心等待。如有疑问请拨打客服电话 ' +
                        //                 '<b class="am-ft-orange">'+Global.hotline+'</b></span>',
                        //             linkText: '返回会员中心',
                        //             link:'home/index.htm'
                        //         });
                        //         successPanel.placeAt('contentctnTTT', 'first');
                        //         successPanel.show();
                        //	} else {
                        //		alert("上传失败");
                        //	}
                        //
                        //});
                    }

                }
            }
        );
        content.placeAt('titlectn', 'after');


        uploader = new ImageCropper({
            keepSquare: true,
            cropSize: 96
        });

//		on(uploader, 'change', function() {
//			var info = uploader.getInfo();
//			var rate =  96/info.w;
//		    domStyle.set(content.displayNode, {
//		        width: info.cw*rate + 'px'
//		        ,height:info.ch*rate + 'px'
//		        ,marginLeft: -info.l*rate + 'px'
//		        ,marginTop: -info.t*rate + 'px'
//		    });
//		});

        // load avatar
//		Ajax.post(Global.baseUrl + '/user/detail', {
//		}).then(function(response) {
//			if (response.success && response.data.photo) {
//				content.displayNode.width = 96;
//				content.displayNode.height = 96;
//				content.displayNode.src = response.data.photo;
//			} else {
//			}
//		});
//		
        Global.focusText();

        // check browser
        //if (has("ie") <= 9) {
        //	var errorHTML = '<div class="contents" style="zoom:0.5;">' +
        //		'<div class="errorpage-icon"><i class="fa">&#xf118;</i></div>' +
        //		'<div class="errorpage-text" style="width: 580px;line-height: 50px;margin-top: 170px;">头像预览功能需要最新浏览器的支持，请使用谷歌、火狐或IE10以上浏览器。</div>' +
        //		'</div>';
        //	domConstruct.place(domConstruct.toDom(errorHTML), content.previewNode);
        //}
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

    function addListeners() {
        var me = this;
//		on(content.fileSelectorNode, 'change', function(e) {
//			var file = this.files[0];
//			if (file) {
//				content.uploadBtn.set('disabled', false);
//				handleFile(this.files[0], content.displayNode);
//			} else {
//				content.uploadBtn.set('disabled', true);
//				uploader.domNode.style.display = 'none';
//				content.displayNode.style.width = 96 + 'px';
//				content.displayNode.style.height = 96 + 'px';
//				content.displayNode.style.marginLeft = 0 + 'px';
//				content.displayNode.style.marginTop = 0 + 'px';
//			}
//		});
//		on(content.fileSelectorOppositeNode, 'change', function(e) {
//			var file = this.files[0];
//			if (file) {
//				content.uploadBtn.set('disabled', false);
//				handleFile(this.files[0], content.displayNode);
//			} else {
//				content.uploadBtn.set('disabled', true);
//				uploader.domNode.style.display = 'none';
//				content.displayNode.style.width = 96 + 'px';
//				content.displayNode.style.height = 96 + 'px';
//				content.displayNode.style.marginLeft = 0 + 'px';
//				content.displayNode.style.marginTop = 0 + 'px';
//			}
//		});
    }

    return {
        init: function () {
            initView();
            Helper.init(config);
            addListeners();
        }
    }
});