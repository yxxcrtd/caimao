define([
    'app/controllers/Helper',
	'dojo/_base/fx', 
	'dijit/registry',
	'dojo/dom',
	'dojo/on',
	'app/common/Ajax',
	'dojo/query',
	'app/common/Global',
	'dojo/dom-class',
	'dojo/cookie',
	'app/views/member/CertificationPanel',
	'app/ux/GenericPrompt',
    'app/ux/GenericTooltip',
    'app/common/User',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, 
		query, Global, domClass, cookie, CertificationPanel, Prompt, Tooltip, User) {
	
	var config = {};
	
	function initView() {
        User.isTrust(function() {
            panel1.confirmBtn.set('disabled', true);
            panel1.confirmBtn.set('disabledMsg', '您已完成实名认证，无需重复认证');
        });
        var panel1 = new CertificationPanel();
        panel1.placeAt('contentctn');
        on(panel1.confirmBtn, 'click', function(e) {
        	if (panel1.isValid()) {
                panel1.confirmBtn.loading(true);
        		Ajax.post(Global.baseUrl + '/user/identity',{
        			'real_name':panel1.nameFld.get('value'),
        			'idcard':panel1.IDNoFld.get('value')
        		}).then(function(response){
                    panel1.confirmBtn.loading(false);
        			if(response.success){
                        panel1.domNode.style.display = 'none';
                        var panel2 = new Prompt({
                            type: 'success',
                            msg: '实名认证已成功！',
                            style: {
                                'paddingTop': '100px'
                            },
                            linkText: '返回会员中心',
	                        link:'home/index.htm'
                        });
                        panel2.placeAt('contentctn');
        			}else{
        				if(response.msg=="由于您多次验证无法通过,请选择上传身份证验证"){
                        	Tooltip.show("由于您多次验证无法通过,请选择"+'<a href='+Global.baseUrl+'/user/uploadID.htm>上传身份证验证</a>', panel1.confirmBtn.innerNode, 'warning');
                        }else{
                        	Tooltip.show(response.msg, panel1.confirmBtn.innerNode, 'warning');
                        }
        			}
        		});
        	}
        });
      
       
       
	}
	
	return {
		init: function() {
			initView();
			Helper.init(config);
		}
	}
});