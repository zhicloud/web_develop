/**
 * 获取批量id
 * @param msg 弹框信息
 * @returns {String} 为空返回nullString字符串
 */
function getIds(msg) {
	var ids = "";
	var datatable = $("#basicDataTable").find(
			"tbody tr input[type=checkbox]:checked");
	$(datatable).each(
			function() {
				if (!(jQuery(this).attr("realHostId") == "" && jQuery(this)
						.attr("status") == 1)) {
					ids += jQuery(this).val() + ","
				}
			});
	// jQuery("input[name='idcheck']:checkbox").each(function(){
	// if(jQuery(this).attr("checked")){
	// ids += jQuery(this).val()+","
	// }
	// })
	if (ids == "") {
		$("#tipscontent").html(msg);
		$("#dia").click();
		return "nullString";
	}
	return ids;
}

//按钮提交操作
function goToBut(msg, functionString) {
	$("#确认关机").html(msg);
	$("#confirm_btn").attr("onclick", functionString);
	$("#con").click();
}