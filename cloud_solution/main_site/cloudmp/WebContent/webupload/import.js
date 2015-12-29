//文件上传
jQuery(function() {
    var $ = jQuery,
        $list = $(".statusBar"),
        $btn = $('#ctlBtn'),
        state = 'pending',
        progresswid,
        uploader,

    uploader = WebUploader.create({
        // 不压缩image
        resize: false,
        // swf文件路径
        swf: path + '/webupload/Uploader.swf',

        // 文件接收服务端。
        server: path + '/user/import',

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#picker',

        // 限制文件上传数量
        fileNumLimit: 1,

        // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        resize: false,

        // 只允许选择excel文件。
        accept: {
            title: 'Excel',
            extensions: 'xls,xlsx',
            mimeTypes: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        },

        method: 'POST'
    });
	//添加文件之前先清空原来数据
    uploader.on( 'beforeFileQueued', function( file ) {
    	uploader.reset();
        $("#path").fadeIn();
    	$("#chooseinfo").css("display","none");
    	$("#chooseinfo").html("");
    	$(".graph").css("visibility","visible");
        $percent = $('#graphbox');
        $percent.find(".green").css('width', '0%' );

        $percent.css("display","none");

    });
    // 当有文件添加进来的时候
    uploader.on('fileQueued', function( file ) {
    	$("#attach").val(file.name);
    	filesize = file.size;
		$("#attach").css("border","0");
		$("#chooseinfo").css("display","none");


		
    });

    uploader.on('startUpload', function(file) {
        $("#path").fadeOut();
        $btn.fadeOut();
        //$("#chooseinfo").css("display","block");
        //$("#chooseinfo").html("上传完成后, 系统还需对数据进行处理, 请耐心等待, 不要离开");
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
        var $percent = $('#graphbox');
        var progresswid = Math.round(percentage * 100);
        $percent.css("display","block");
        $percent.find(".green").css('display', 'block' );
        $percent.find(".green").css('width', progresswid + '%' );
        $percent.find(".num").html(progresswid  + '%');

        if (progresswid == 100) {
            $("#chooseinfo").css("display","block");
            $("#chooseinfo").html("上传完成, 数据正在处理中, 请耐心等待, 不要离开");
        }

    });
    uploader.on( 'uploadSuccess', function( file,response ) {
        $("#chooseinfo").css("display","block");
        $("#chooseinfo").html(response.message);

    });
    

    uploader.on( 'uploadError', function( file,reason ) {
        $("#path").fadeIn();
    	$("#chooseinfo").css("display","block");
    	$("#chooseinfo").html("上传出错:"+reason);

    });
    uploader.on( 'uploadComplete', function( file ) {
        //$("#chooseinfo").css("display","block");
        //$("#chooseinfo").html("上传完成,系统正在处理中,请不要离开……");
        //console.info("uploadComplete");

    });

    $btn.on( 'click', function() {
        if ( state === 'uploading' ) {
            uploader.stop();
        } else {
        	var form = jQuery("#uploadform");
	  		form.parsley('validate');
	  		if(form.parsley('isValid')){  
	  			if(uploader.getFiles().length==0){
	  				$("#attach").css("border","solid 1px red");
	  				$("#chooseinfo").css("display","block");
	  				return;
	  			}
				uploader.upload(); 
	  		}
        }
    });
    
});
