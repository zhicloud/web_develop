<%@ page pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>

	<body>
		<form method="post" action="<%=request.getContextPath()%>/easyInterface/client.do?command=authenticate">
			<!-- <input type="hidden" name="command" value="authenticate" /> -->
			<table>
				<tr>
					<td>用户类型：</td>
					<td>
						<select id="user_type" name="user_type">
							<option value="1">超级管理员</option>
							<option value="2">运营商</option>
							<option value="3">代理商</option>
							<option value="4">终端用户</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>user_account:</td>
					<td><input type="text" id="user_account" name="user_account" value="admin" /></td>
				</tr>
				<tr>
					<td>password:</td>
					<td><input type="password" id="password" name="password" value="admin" /></td>
				</tr>
				<tr>
					<td>type:</td>
					<td>
						<select id="terminal_type" name="terminal_type">
							<option value="1">客户端</option>
							<option value="2">云终端</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>cloud_terminal_id:</td>
					<td><input type="text" id="cloud_terminal_id" name="cloud_terminal_id" value="1" /></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" value="submit" /></td>
				</tr>
			</table>
		
		</form>
	
	</body>

</html>