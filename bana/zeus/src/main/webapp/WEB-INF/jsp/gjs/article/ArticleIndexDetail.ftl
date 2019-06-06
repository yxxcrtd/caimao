<!doctype html>
<html>
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width,height=device-height,initial-scale=1, user-scalable=false">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-touch-fullscreen" content="yes">
        <meta name="format-detection" content="telephone=no"/>
        <title>${entity.title!}</title>
        <link rel="stylesheet" href="${htmlUrl!}/static/gjs/prod/style/app.css" />
        <script src="${htmlUrl!}/static/gjs/prod/js/lib.js"></script>
    </head>

    <body>
        <div id="cont">
            <#--<div class="fixedBox"><img src="http://jin.web1caimao.com/static/gjs/img/loading.gif" /></div>-->
            <h1>${entity.title!}</h1>
            <h2>
                <img src="${htmlUrl!}/upload/article/<#list entity.user?split(',') as u><#if (2 == u_index)>${u}</#if></#list>"><strong><#list entity.user?split(',') as u><#if (1 == u_index)>${u}</#if></#list></strong><span>|</span><i>${entity.pub!}</i></h2>
            <div class="content"><i>${entity.content!}</i></div>
        </div>
    </body>
</html>