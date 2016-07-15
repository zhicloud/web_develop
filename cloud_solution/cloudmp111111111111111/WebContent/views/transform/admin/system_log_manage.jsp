<%@ page pageEncoding="utf-8"%>
<!-- system_log_manage.jsp -->
<%@ include file="/views/common/common.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<div class="centercontent_block tables" id="mainpage">
        <div id="contentwrapper_1" class="contentwrapper tables"> 
            <div class="contenttitle2">
                	<h3>日志信息</h3>
                </div>
                <table cellpadding="0" cellspacing="0" border="0" class="stdtable" id="dyntable2" style="font-size:12px;">
                    <colgroup>
                        <col class="con1" style="width: 6%" />
                        <col class="con0" />
                        <col class="con1" />
                        <col class="con0" />
                    </colgroup>
                    <thead>
                        <tr> 
                          	<th class="head1 nosort">序号</th>
                            <th class="head0 sort">日志内容</th>
                            <th class="head1 sort">操作时间</th>
                            <th class="head0 sort">操作人</th>
                        </tr>
                    </thead> 
                    <tbody>
                    	<c:forEach items="${logList}" var="log" varStatus="status">
                    		<tr class="gradeX">
                          	<td align="center"><span class="center">
                          	${status.count }
                         	</span></td>
                            <td>${log.content}</td>
                            <td>${log.operate_date}</td>
                            <td>${log.user.usercount}</td>
                        </tr> 
                    	</c:forEach> 
                    </tbody>
                </table>
                <br>
                <br>
        </div><!--contentwrapper-->
</div>