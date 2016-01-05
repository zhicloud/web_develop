//文件上传
jQuery(function() {
    var $ = jQuery,
        $list = $('.statusBar'),
        $btn = $('#ctlBtn'),
        state = 'pending',
        progresswid,
        uploader,
        sendmethod = 'PUT',
        serverurl = 'http://'+serverIP+'/disk_image',
        filesize,
        md5value;
    uploader = WebUploader.create({
        // 不压缩image
        resize: false,
        // swf文件路径
        swf:  '<%=request.getContextPath() %>/webupload/Uploader.swf',
        // 文件接收服务端。
        server:serverurl,
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#picker',
        fileNumLimit: 1,
        method:'PUT',
        sendAsBinary: true,
        threads:1,
        chunked:true,
        chunkSize:chunkSize,
	    accept: {
	            title: 'DISK',
	            extensions: 'img,qcow2',
	            mimeTypes: 'iso/*'
	        }
    });
	//添加文件之前先清空原来数据
    uploader.on( 'beforeFileQueued', function( file ) {
    	uploader.reset();
    	$("#chooseinfo").css("display","none");
    	$("#chooseinfo").html("");
    	
        $percent = $('#graphbox');
        $percent.find(".green").css('width', '0%' );
        $percent.find(".green").html('0%');
        $percent.css("display","none");

    });
    // 当有文件添加进来的时候
    uploader.on('fileQueued', function( file ) {
    	$("#isopath").val(file.name);
    	filesize = file.size;
		$("#isopath").css("border","0");
		$("#chooseinfo").css("display","none");
		
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
        var $li = $('.statusBar'),
            $percent = $('#graphbox');
	        progresswid = Math.round(percentage * 100);
	        $percent.css("display","block");
	        $percent.find(".green").css('width', progresswid + '%' );
	        $percent.find(".green").html(progresswid  + '%');
    });
    uploader.on( 'uploadSuccess', function( file,response ) {
    	updateMemoryData('disk');
    	$('#closebtn').click();
    	$("#successconfirm").click();
    });
    
     uploader.on('uploadBeforeSend', function(obj, data, headers) {
    	 headers['host'] = clientIP;
    	 headers['Content-Length'] = obj.file.size;
    	 headers['zc-name'] = $("#isoName").val();
    	 headers['zc-md5'] = obj.file.source.uid;
    	 headers['zc-description'] = $("#isoDes").val();
    	 headers['Content-Type'] = 'application/x-www-form-urlencoded';
		 headers['zc-tag'] = $("#isoTag").val();
		 headers['zc-size'] = filesize;
		 headers['zc-format'] = $("input[name='isotype']:checked").val();
    	 headers['zc-group'] = $("#usergroup").val();
    	 headers['zc-user'] = $("#userbelong").val();
    	 headers['zc-filesize'] = obj.file.size;  
		 //headers['now_slice'] = obj.chunk;
    	 //headers['all_slice'] = obj.chunks;
    	 headers['zc-progress'] = "all_slice="+obj.chunks+",now_slice="+obj.chunk;
     	});
/*     uploader.on( 'error', function( handler ) {
     	
       });*/
    uploader.on( 'uploadError', function( file,reason ) {
    	$("#chooseinfo").css("display","block");
    	$("#chooseinfo").html("上传出错:"+reason);

    });
    uploader.on( 'uploadComplete', function( file ) {
       //$( '#'+file.id ).find('.progress').fadeOut();
    });
    uploader.on( 'all', function( type ) {
        if ( type === 'startUpload' ) {
            state = 'uploading';
        } else if ( type === 'stopUpload' ) {
            state = 'paused';
        } else if ( type === 'uploadFinished' ) {
            state = 'done';
        }

        if ( state === 'uploading' ) {
            $btn.text('正在上传');
            $btn[0].disabled = true;
        }else if(state === 'paused'){
            $btn.text('正在上传');
            $btn[0].disabled = true;
        }else{
        	$btn.text('开始上传');
        	$btn[0].disabled = false;
        }
    });
    $btn.on( 'click', function() {
        if ( state === 'uploading' ) {
            uploader.stop();
        } else {
        	var form = jQuery("#uploadform");
	  		form.parsley('validate');
	  		if(form.parsley('isValid')){  
	  			if(uploader.getFiles().length==0){
	  				$("#isopath").css("border","solid 1px red");
	  				$("#chooseinfo").css("display","block");
	  				return;
	  			}
				uploader.upload(); 
	  		}
        }
    });
    
});