// @koala-prepend "./lib/jquery-1.11.1.min.js"
// @koala-prepend "./lib/jquery.uploadify.min.js"
// @koala-prepend "./lib/jquery.imgareaselect.min.js"
// @koala-prepend "./common/utils.js"
// @koala-prepend "./common/Data.js"
// @koala-prepend "./lib/formValidate.js"
// @koala-prepend "./v2/actionTip.js"
// @koala-prepend "./ux/alert.js"

var cutData = {};
var ias;
var imgshow = function(img, selection){
	var owidth = $("#iselect").attr("owidth"),oheight= $("#iselect").attr("oheight"),imgwidth=$("#iselect").width()||100,imgheight=$("#iselect").height()||100;
	var osize =imgwidth/owidth;
	var selectHeight=selectWidth=selection.width/osize;
	var size159=selectWidth/159,size90=selectWidth/90,size30=selectWidth/30;
	var width159=owidth/size159,height159=oheight/size159;
	var width90=owidth/size90,height90=oheight/size90;
	var width30=owidth/size30,height30=oheight/size30;
	$("#jsImg159").css({width:width159,height:height159,"margin-top":selection.y1/osize/size159*-1,"margin-left":selection.x1/osize/size159*-1});
	$("#jsImg90").css({width:width90,height:height90,"margin-top":selection.y1/osize/size90*-1,"margin-left":selection.x1/osize/size90*-1});
	$("#jsImg30").css({width:width30,height:height30,"margin-top":selection.y1/osize/size30*-1,"margin-left":selection.x1/osize/size30*-1});
};
$(function () {
	$("#selectBtn").uploadify({
		'buttonText' : "",
		'fileObjName' : 'file',
		'queueSizeLimit' : 1,
		'buttonClass' : 'hide',
		'fileSizeLimit' : '500KB',
		'fileTypeExts' : '*.gif; *.jpg; *.png;*.jpeg',
		'swf':'/static/prod/js/lib/uploadify.swf',
		'uploader': '/user/avatar/preset',
		'uploadLimit' : 0,
		'auto': true,
		'multi': false,
		'onUploadSuccess' : function(file, data, response) {
			var d = $.parseJSON(data);
			cutData["file_name"]=d.data.img.substring(d.data.img.lastIndexOf("/")+1,d.data.img.lastIndexOf("?"));
			cutData['w'] = d.data.width;
			cutData['h'] = d.data.height;
			cutData['l'] = 0;
			cutData['t'] = 0;
			$("#iselect").attr("owidth",d.data.width);
			$("#iselect").attr("oheight",d.data.height);
			$(".sele_img img").attr("src",d.data.img);
			$("#iselect").css({width:d.data.width>300?300:d.data.width,height:d.data.width>300?(300/d.data.width*d.data.height):d.data.width}).parent().removeClass("hide");
			ias = $('img#iselect').imgAreaSelect({
				handles: true,
				instance: true,
				aspectRatio:"1:1",
				x1:0,
				y1:0,
				x2:100,
				y2:100,
				minHeight:30,
				minWidth:30,
				persistent:true,
				onSelectEnd: function(img, selection){
					var imgwidth=$("#iselect").width(),owidth=$("#iselect").attr("owidth");
					var osize =imgwidth/owidth;
					cutData.l=~~(selection.x1/osize);cutData.t=~~(selection.y1/osize);cutData.w=~~(selection.width/osize);cutData.h=~~(selection.height/osize);
				},
				onSelectChange:imgshow
			});
			imgshow($("#iselect"),{x1:0,y1:0,x2:100,y2:100,width:100,height:100});
			$("#imgSave").removeClass('btn_gray').addClass('btn_blue');
		 },
		'onUploadError' : function(file, errorCode, errorMsg, errorString) {
			alert("上传失败请确认图片格式是否正确");
		}
    });
	$("#imgSave").click(function(){
		if($(this).hasClass('btn_blue')){
			$.post("/user/avatar/set1",cutData,function(response){
				if(response.success){
					ias.cancelSelection();
					$("#iselect").addClass("hide");
					$("#imgSave").addClass('btn_gray').removeClass('btn_blue');
				}else{
					alert(response.msg);
				}
			});
		}
	});

	var formInfo = $('#infoFrom');
	var btnInfo = $('#infoBtn');

	formValidate.init(formInfo);

	$(btnInfo).click(function(){
		if (formValidate.validateForm(formInfo) == false) {
			return false;
		}
		var nick_name=$("#jsNick").val();
		var come_from=$("#jsFrom").val();
		var address=$("#jsAddress").val();
		var occupation=$("#jsOccu").val();
		var education=$("#jsEdu").val();
		var inve_experience=$("#jsInve").val();
		$.post("/user/enrich",{nick_name:nick_name,come_from:come_from,address:address,occupation:occupation,education:education,inve_experience:inve_experience},function(response){
			if(response.success){
				Alert.show("修改成功", "确认", window.location.reload());
			} else {
				formValidate.showFormTips(btnInfo, response.msg);
			}
		});
	});
});