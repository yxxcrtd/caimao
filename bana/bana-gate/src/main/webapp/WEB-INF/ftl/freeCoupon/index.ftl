<!DOCTYPE html>
<html>
<head>
    <title>免息券管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>
<body>
<header class="navbar navbar-inverse bs-docs-nav navbar-fixed-top" role="banner">
    <div class="container">
        <div class="navbar-header">
            <button class="navbar-toggle" type="button" data-toggle="collapse" data-target=".bs-navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="/" class="navbar-brand"></a>
        </div>
    </div>
</header>
<div class="body_container" style="padding-top: 51px;">
    <div class="main_content" style="padding: 0 20px;">
        <div class="container">
            <h4>免息券管理</h4>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">获券渠道 <button class="btn btn-sm btn-primary" onclick="add_coupon_channel()">添加</button></div>
            <div class="panel-body" id="set_div">
                <form class="form-inline" id="default_set_form" style="display: none">
                    <div class="form-group">
                        <label>渠道</label>
                        <select class="form-control input-sm" name="channel_type">
                            <option value="1">充值</option>
                            <option value="2">融资</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>　满</label>
                        <input type="text" class="form-control input-sm" name="money">
                    </div>
                    <div class="form-group">
                        <label>　时赠送</label>
                        <input type="text" class="form-control input-sm" name="scale">
                    </div>
                    <div class="form-group">
                        <label>% 红包　分</label>
                        <input type="text" class="form-control input-sm" name="amount">
                        <label>个</label>
                    </div>
                    <div class="form-group">
                        <label>　状态</label>
                        <select class="form-control input-sm" name="status">
                            <option value="0">开启</option>
                            <option value="1">关闭</option>
                        </select>
                    </div>
                    <button type="button" class="coupon_channel_set_btn btn btn-sm btn-primary">保存</button>
                </form>
                <#list couponChannelList as couponChannel>
                    <form class="form-inline" style="margin-top: 10px;">
                        <input type="hidden" name="id" value="${couponChannel.id}">
                        <div class="form-group">
                            <label>渠道</label>
                            <select class="form-control input-sm" name="channel_type">
                                <option value="1" <#if couponChannel.channelType.getValue() == 1>selected</#if>>充值</option>
                                <option value="2" <#if couponChannel.channelType.getValue() == 2>selected</#if>>融资</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>　满</label>
                            <input type="text" class="form-control input-sm" name="money" value="${couponChannel.money?string('#')}">
                        </div>
                        <div class="form-group">
                            <label>　时赠送</label>
                            <input type="text" class="form-control input-sm" name="scale" value="${couponChannel.scale?string('#')}">
                        </div>
                        <div class="form-group">
                            <label>% 红包　分</label>
                            <input type="text" class="form-control input-sm" name="amount" value="${couponChannel.amount?string('#')}">
                            <label>个</label>
                        </div>
                        <div class="form-group">
                            <label>　状态</label>
                            <select class="form-control input-sm" name="status">
                                <option value="0" <#if couponChannel.status == 0>selected</#if>>开启</option>
                                <option value="1" <#if couponChannel.status == 1>selected</#if>>关闭</option>
                            </select>
                        </div>
                        <button type="button" class="coupon_channel_set_btn btn btn-sm btn-primary">保存</button>
                    </form>
                </#list>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function add_coupon_channel(){
        var html_str = '<form class="form-inline" style="margin-top: 10px;">' + $("#default_set_form").html() + '</form>';
        $("#set_div").append(html_str);
    }

    $(document).on("click", ".coupon_channel_set_btn", function(){
        var form_obj = $(this).parent();
        var save_url = "/freeCoupon/" + (form_obj.find("input[name=id]").length > 0?"updateChannel":"addChannel");
        $.post(save_url, form_obj.serializeArray(), function(d){
            location.reload();
        });
    });

</script>
</body>
</html>