<div class="xin1">
    <form id="saveAccountForm" action="/account/edit.action" method="post">
        <input type="hidden" name="accountId" value="${account.accountId}"/>

        <div class="xin1_1">
            <div class="whitea">修改用户</div>
            <div class="close"><img data-dismiss="modal" src="/images/xinjian_03.gif" width="18" height="18"/></div>
        </div>
        <div class="caidan">
            <div class="caidan1">
                <div class="cd1">
                    <table width="100%" border="0">
                        <tr>
                            <td width="25%" align="right" class="hui1">用户名：</td>
                            <td width="75%" align="left" valign="middle">
        	<span>${account.username}<span/>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" class="hui1">用户密码：</td>
                            <td align="left" valign="middle">
                                <a href="javascript:resetPassword();" id="btnReset">重置密码</a>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" class="hui1">选择角色：</td>
                            <td align="left" valign="middle">
                                <select name="role.roleId" class="input">
                                <#list roleList as role>
                                    <option value="${role.roleId}"
                                            <#if account.role??&&role.roleId=account.role.roleId>selected="selected"</#if>>${role.roleName}</option>
                                </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="caidan2">
                <a href="javascript:editAccount();"><img src="/images/bjwj03.jpg" width="62" height="23"/></a>&nbsp;
                <a href="#" data-dismiss="modal"><img src="/images/bjwj_05.jpg" width="62" height="23"/></a>
            </div>
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