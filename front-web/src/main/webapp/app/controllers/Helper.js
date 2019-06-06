/**
 * this file deals with almost function controllers used 
 */
define([
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/dom-attr',
    'dojo/on',
    'dojo/when',
    'app/common/Ajax',
    'app/common/Global',
    'app/common/Data',
    'app/common/Fx',
    'dijit/registry',
    'dojo/query',
    'dojo/has',
    'dojo/_base/sniff',
    'app/ux/GenericWarningBar',
    'app/ux/GenericSideToolbar',
    'app/views/common/LoginTopbar',
    'app/views/common/LogoutTopbar',
    'dojo/cookie',
    'app/views/common/Error',
    'app/ux/GenericTooltip',
    'dojo/request/notify',
    'dojo/json',
    'dojo/_base/config',
    'dojo/NodeList-traverse'
], function(dom, domConstruct, domClass, domStyle, domAttr, on, when, Ajax, Global, Data, Fx,
		registry, query, has, sniff, WarningBar, SideToolbar, LoginTopbar, LogoutTopbar, cookie,
        ErrorPanel, Tooltip, notify, JSON, Config) {
	var mainMenuItems = query('.subnav-item', 'subheader'),
		sideBarItems = query('.list-lside-item', 'sidebar'),
		callForHelpEl = dom.byId('callforhelp'),
		smallLogo = dom.byId('logotopbar'),
		scrollTop = 0,
		subHeader = dom.byId('subheader'),
        menuPro = dom.byId('menu-product'),
        menuProItems = dom.byId('menu-product-items'),
        headerNav = dom.byId('header-nav');
    var warningBar;
    warningBar = new WarningBar();
    warningBar.placeAt(document.body);
	
	var renderItem = function(items, role) {
		items.filter('[data-access~='+role+']').style('display', 'block');
		items.filter(':not([data-access~='+role+'])').forEach(function(item) {
			domConstruct.destroy(item);
		});
	};

    if (Config.isDebug) {
        notify("done", function(responseOrError){
            if (responseOrError.options && responseOrError.options.handleAs && responseOrError.options.handleAs == 'json') {
                var obj = JSON.parse(responseOrError.text);
                if (!obj.timeout && !obj.success) {
                    warningBar.set('msg', obj.msg);
                    warningBar.show(true);
                }
            }
        });
    }


	return {
		init: function(config) {
            if (menuPro) {
                on(menuPro, 'mouseover', function(e) {
                    menuProItems.style.display = 'block';
                });

                on(menuPro, 'mouseleave', function(e) {
                    menuProItems.style.display = 'none';
                });
            }
			// get the role of login user
			config = config || {};
			window.onerror = function() {
				return false;
			}
			if (!('login' in config) || config.login) {
                when(Data.getUser(), function(user) {
                    if (user) {
                        var loginTopbar = new LoginTopbar({
                            username: Global.sirOrLady(user)
                        }, 'usertopbar');
						if(Config.freePid.indexOf(user.refUserId.toString()) > 0){
							var free_p = dom.byId('free_p');
							if(free_p != null){
								free_p.style.display = 'block';
							}
						}
                    } else {
                        var logoutTopbar = new LogoutTopbar({}, 'usertopbar');
                    }
                });
            }
			
			// render the topbar
			var topbarAvatarEl = dom.byId('topbaravatar'),
				topbarUsernameEl = dom.byId('topusername'),
				topbarMailCountEl = dom.byId('topbarmailcount');
			if (topbarAvatarEl) {
				Ajax.post(Global.baseUrl + '/user/top/bar', {
				}).then(function(response) {
					if (response.success) {
						var data = response.data;
						if (data.userPhoto) {
							topbarAvatarEl.src = data.userPhoto;
						}
						//topbarUsernameEl.innerHTML = data.userName;
						topbarMailCountEl.innerHTML = data.messageCount;
					} else {
					}
				});
			}
			// clear cache from client
            on(document, 'click', function(e) {
                var target = e.target,
                    count = 4;
                for (var i = 0; i < count; i++) {
                    if (target && target.nodeName.toLowerCase() == 'a') {
                        var me = target,
                        // clip the time stamp param
                            href = temp = me.href.replace(/(&?_=.*(?=&)|&?_=.*)/,'').replace('?&', '?').replace(/\?$/,''),
                            tokenStr = '_=' + new Date().valueOf(),
                            symbol = (href.indexOf('?') === -1 ? '?' : '&');
                        if (href && href !== 'javascript:void(0)' && !/_=.*/.test(href) && domAttr.get(me, 'href')[0] !== '#') {
                            me.href = href + symbol + tokenStr;
                        }
                        // recover the url after clicking
                        setTimeout(function() {
                            if (temp) {
                                me.href = temp;
                            }
                        }, 1);
                        break;
                    } else {
                        if (!target.parentNode) {
                            break;
                        }
                        target = target.parentNode;
                    }
                }
            });
            //on(document, 'a:click', function(e) {
				//var me = this,
				//	// clip the time stamp param
				//	href = temp = this.href.replace(/(&?_=.*(?=&)|&?_=.*)/,'').replace('?&', '?').replace(/\?$/,''),
				//	tokenStr = '_=' + new Date().valueOf(),
				//	symbol = (href.indexOf('?') === -1 ? '?' : '&');
				//if (href && href !== 'javascript:void(0)' && !/_=.*/.test(href) && domAttr.get(this, 'href')[0] !== '#') {
				//	this.href = href + symbol + tokenStr;
				//}
				//// recover the url after clicking
				//setTimeout(function() {
				//	if (temp) {
				//		me.href = temp;
				//	}
				//}, 1);
            //});

            on(document, 'click', function(e) {
              Tooltip.hide();
            });

            // show help text
            on(document, '.help:mouseover', function(e) {
                Tooltip.show(domAttr.get(this, 'data-help'), this, 'info');
            });

            on(document, '.help:mouseout', function(e) {
                Tooltip.hide();
            });

            // hide the tooltip
            on(document, 'focusin', function(e) {
                Tooltip.hide();
            });
			
			// focus first input
			Global.focusText();
			
			// press enter key, if the screen has the displayed button to commit, invoke the click event dispatcher
			on(window, 'keypress', function(e) {
				var charOrCode = e.charCode || e.keyCode;
				if (charOrCode === 13) { // enter
					e.preventDefault();
					var btns = query('.dijitButton.enterbutton').reverse();
					btns.some(function(btnEl) {
						var btn = registry.byNode(btnEl);
						if (btn && !btn.get('disabled') && btnEl.offsetHeight !==0 && btn.onClick) {
							btn.onClick.call(btn);
							return true; // broke the loop
						}
						if (!btn) { // dom
							btnEl.click();
							return true;
						}
					});
				}
			});
			
			// sniff browser
			if (has("ie") <= 8 && !cookie('warningBarClosed')) {
				warningBar = new WarningBar({
					msg: '请使用谷歌、火狐或最新版IE浏览器！'
				});
				warningBar.placeAt(document.body);
				warningBar.show();
			}
			
			// call for help anim
			if (callForHelpEl) {
				on(callForHelpEl, 'mouseenter', function() {
					domClass.add(query('i', this)[0], 'phone-calling');
				});
				on(callForHelpEl, 'mouseleave', function() {
					domClass.remove(query('i', this)[0], 'phone-calling');
				});
			}
			
			// active scroll
			on(window, 'scroll', function(e) {
				var top = document.body.scrollTop + document.documentElement.scrollTop,
					pos = 76;
				if (scrollTop < pos && top >= pos) {
					smallLogo && Fx.show(smallLogo, null, 'logo-rotate');
					if (subHeader) {
						domStyle.set(subHeader, {
							position: 'fixed',
							width: '100%',
							zIndex: 1000,
							top: '30px'
						});
						query('#subheader').next().style({
							paddingTop: '45px'
						});
					}
				} else if (scrollTop >= pos && top < pos) {
					smallLogo && Fx.hide(smallLogo, null, 'logo-rotate');
					if (subHeader) {
						domStyle.set(subHeader, {
							position: 'static'
						});
						query('#subheader').next().style({
							paddingTop: '0'
						});
					}
				}
				scrollTop = top;
			});
			
			// place sidetoolbar
			var sideToolbar = new SideToolbar({
				items: [{
                    xtype: 'qq'
                }, {
					xtype: 'totop'
				}]
			});
			sideToolbar.placeAt(document.body);

/*			//获取累计融资
			if(dom.byId('totalAmountNode')){
				Ajax.get(Global.baseUrl + '/financing/getLoanTotalAmount', {
				}).then(function(response) {
					if (response.success && response.data) {
							dom.byId('totalAmountNode').innerHTML=Global.formatAmount(response.data.loanAmount,0,'w');
					}
				});
			}
			//获取会员人数
			if(dom.byId('totalRegisterNode')){
				Ajax.get(Global.baseUrl + '/user/getRegisterUserCount',{}).then(function(response) {
					if (response.success && response.data) {
						dom.byId('totalRegisterNode').innerHTML=Global.formatAmount(response.data.count*100,0,'w');
					}
				});
			}*/


            /** 获取置顶公告 **/
            Ajax.get(Global.baseUrl + '/content/top_notice', {}).then(function (response) {
                if (response.success) {
                    var noticeDiv = dom.byId("topNoticeDivPoint");
                    noticeDiv.style.height = "30px";
                    query('.section', noticeDiv).forEach(function(e) {
                        e.style.display = "block";
                    });
                    var noticeA = dom.byId("topNoticeTitlePoint");
                    noticeA.innerHTML = response.data.title;
                    noticeA.href = "/notice.htm?id="+response.data.id;
                }
            });
		}
	};
});