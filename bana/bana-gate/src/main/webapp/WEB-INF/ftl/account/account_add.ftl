<#include "../include/common.ftl"/>
<div class="xin1">
    <form id="saveAccountForm" action="/account/add.action" method="post">
        <div class="xin1_1">
            <div class="whitea">添加用户</div>
            <div class="close"><img data-dismiss="modal" src="/images/xinjian_03.gif" width="18" height="18"/></div>
        </div>
        <div class="caidan">
            <div class="caidan1">
                <div class="cd1">
                    <table width="100%" border="0">
                        <tr>
                            <td width="25%" align="right" class="hui1">用户名：</td>
                            <td width="75%" align="left" valign="middle">
                                <input name="username" type="text" class="input" id="textfield"
                                       validate="validate[required]"/>
                                <span class='tip'></span>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" class="hui1">用户密码：</td>
                            <td align="left" valign="middle">
                                <input name="password" type="password" class="input" id="textfield2"
                                       validate="validate[required,length[4,20]]"/>
                                <span class='tip'></span>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" class="hui1">确认密码：</td>
                            <td align="left" valign="middle">
                                <input name="retryPassword" type="password" class="input" id="textfield3"
                                       validate="validate[required,confirm[textfield2]]"/>
                                <span class='tip'></span>
                            </td>
                        </tr>

                    </table>
                </div>
            </div>
            <div class="caidan2">
                <a href="javascript:addAccount();"><img src="/images/bjwj03.jpg" width="62" height="23"/></a>&nbsp;
                <a href="#" data-dismiss="modal"><img src="/images/bjwj_05.jpg" width="62" height="23"/></a>
            </div>
        </div>
    </form>
</div>
<style type="text/css">
    .close {
        opacity: 1;
    }

    .tip {
        padding-left: 10px;
        color: red;
    }
</style>