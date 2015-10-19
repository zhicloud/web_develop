<%@ page pageEncoding="utf-8"%>
<%@ include file="/views/common/common.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
.errorinfo {
margin-bottom:10px;
padding:5px;
text-align: center;
color: #333;
font-size: 11px;
border-top-left-radius:2px;
border-top-right-radius:2px;
border-bottom-right-radius:2px;
border-bottom-left-radius:2px;
background-image:none;
background-attachment:scroll;
background-repeat:repeat;
background-position-x:0%;
background-position-y:0%;
background-size:auto;
background-origin:padding-box;
background-clip:border-box;
background-color:rgb(255, 252, 204);
}
.lablefont{
font-family: "RobotoBoldCondensed", Arial, Helvetica, sans-serif;
font-size: 14px;
}
</style>
<script type="text/javascript">
var temmbillid = "${systemUser.billid}";
function saveInfo(){
	var oldpassword = jQuery("#oldpassword").val();
	var newpassword = jQuery("#newpassword").val();
	var confirmpassword = jQuery("#confirmpassword").val();
	var tempflag = false;
	if(oldpassword==undefined||oldpassword==""){
		jQuery("#oldpasswordinfo").html("");
		jQuery("#oldpasswordinfo").html("请输入旧密码");
		jQuery("#oldpasswordinfo").show();
		tempflag = true;
	}
	if(oldpassword.length>40){
		jQuery("#oldpasswordinfo").html("");
		jQuery("#oldpasswordinfo").html("密码字符长度超出定义");
		jQuery("#oldpasswordinfo").show();
		tempflag = true;
	}	
	if(newpassword.length>40){
		jQuery("#newpassword").html("");
		jQuery("#newpasswordinfo").html("密码字符长度超出定义");
		tempflag = true;
	}	
	if(newpassword==undefined||newpassword==""){
		jQuery("#newpassword").html("");
		jQuery("#newpasswordinfo").html("请输入新密码");
		tempflag = true;
	}		
	if(confirmpassword==undefined||confirmpassword==""){
		jQuery("#confirmpasswordinfo").html("");
		jQuery("#confirmpasswordinfo").html("请再次确认新密码");
		tempflag = true;
	}	
	if(confirmpassword.length>40){
		jQuery("#confirmpasswordinfo").html("");
		jQuery("#confirmpasswordinfo").html("密码字符长度超出定义");
		tempflag = true;
	}		
	if(newpassword!=confirmpassword){
		jQuery("#confirmpasswordinfo").html("");
		jQuery("#confirmpasswordinfo").html("两次输入的新密码不一致，请重新输入");
		tempflag = true;		
	}
	if(!tempflag){
		jQuery.ajax({
	  	 	type: "POST",
	  	 	async:false,
	   		url: "<%=request.getContextPath() %>/transform/updatepassword",
	  		data: "newpassword="+newpassword+"&oldpassword="+oldpassword+"&"+tempsessioncode+"="+tempsessionvalue,
	  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
	   		success: function(result){
	   		  var obj = eval("("+result+")");
	     	if(obj.status=="success"){
				$("<div class=\"datagrid-mask-msg\"></div>").html("修改成功，请重新登录。。。").appendTo("body").css({
					display:"block",
					left:($(document.body).outerWidth(true) - 190) / 2,
					top:($(window).height() - 45) / 2
				});
	     		parent.window.location.href="<%=request.getContextPath() %>/transform/userlogin";
	     	}else{
	     		jQuery.messager.alert('警告',obj.result,'warning');
	     	}
	   	}
		});	
	}
}
</script>
<div class="centercontent_block tables" id="mainpage">
        <div id="contentwrapper_1" class="contentwrapper tables"> 
            <div class="contenttitle2">
                	<h3>修改密码</h3>
            </div>    	
            <div>
						<form class="stdform" action="" method="post" id="changepasswordform"  >
                            <table>
                            <tr>
                            <td><p>
                                <label class="lablefont">旧密码:</label>
                                <span class="field"><input type="password" name="oldpassword" id="oldpassword" class="smallinput "  /></span>
                            </p></td>
                            <td>&nbsp;&nbsp;<span id="oldpasswordinfo" class="errorinfo" style="display:none;"></span></td>
                            </tr>
                            <tr>
                            <td><p>
                                <label class="lablefont">新密码:</label>
                                <span class="field"><input type="password" name="newpassword" id="newpassword" class="smallinput "  />
                                </span>
                            </p></td>
                            <td>&nbsp;&nbsp;<span id="newpasswordinfo" class="errorinfo" style="display:none;"></span></td>
                            </tr>
                            
                            <tr>
                            <td><p>
                                <label class="lablefont">新密码确认:</label>
                                <span class="field"><input type="password" name="confirmpassword" id="confirmpassword" class="smallinput "  /></span>
                            </p></td>
                            <td>&nbsp;&nbsp;<span id="confirmpasswordinfo" class="errorinfo" style="display:none;"></span></td>
                            </tr>
                            <tr>
                            <td><p>
                            <span class="field">
                                <button class="submit radius2" id="savebtn" style="width:310px;">保存</button>
                            </span>
                            </p></td>
                            <td></td>
                            </tr>
                            </table>
                        </form> 
                </div>                       	
                <br>
                <br>
        </div><!--contentwrapper-->
</div>
