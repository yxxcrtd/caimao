;$(function(){	
    $('.delet').click(function(){
    	$(this).parent().siblings('input').val('');
    	$(this).parent().css('display','none');
    	$(this).parent().siblings("span").css('display','none');
    });
    $('.delet-2').click(function(){
        $(this).parent().siblings('input').val('');
        $(this).parent().css('display','none');
    });

    $('.eye').click(function(){
    	if($(this).parent().siblings("input").attr('type')=="text"){
    		$(this).parent().siblings("input").attr('type','password')
    	}else{
    		$(this).parent().siblings("input").attr('type','text')
    	}
    });
    
});
