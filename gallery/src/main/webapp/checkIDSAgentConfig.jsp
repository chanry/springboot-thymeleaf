<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@page import="com.trs.idm.interact.agent.IAgent"%>
<%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", -1);
	response.setDateHeader("max-age", 0);
	
	IAgentPropertiesValidator propertiesValidator = null;
	IAgent agent = (IAgent)application.getAttribute("IDSAgent");
	if(agent != null) {
		propertiesValidator = agent.getPropertiesValidator();
	}else {
		propertiesValidator = ServiceProviderFactory.getInstance().getPropertiesValidator();
	}
	
	
%>


<%@page import="java.util.List"%>
<%@page import="com.trs.idm.interact.agent.validator.AgentConfigItem"%>
<%@page import="com.trs.idm.util.StringHelper"%>
<%@page import="com.trs.idm.interact.agent.validator.IAgentPropertiesValidator"%>
<%@page import="com.trs.idm.saml.sp.core.ServiceProviderFactory"%><html>
<head>
<title>IDS Agent配置检查页面</title>
</head>

<body>
<table width="99%" border="1" style="word-break: break-all;" >
<tr>
	<td colspan="6">
	&nbsp;<br>
	<%if(propertiesValidator == null) {%>
	&nbsp;&nbsp;<b><font color="red">检查结果： 无法取到验证器的实例对象，请确认应用是否启动成功！</font></b><br><br>
	
	<%
		return;
	} %>
	
	<%
	if(!propertiesValidator.check()) {
	%>
		<b><font color="red">
		&nbsp;&nbsp;检查失败：<%=propertiesValidator.getDetailCheckResult() %><br>
		
		<%
			if(propertiesValidator.hasErrorItem()) { 
		%>
			&nbsp;&nbsp;具体失败原因请参照下方的表格！<br>
		<%
			} 
		%>
		</font></b>
	<%
	}else  {
	%>
		<b><font color="green">
		&nbsp;&nbsp;Agent配置文件中必要的配置项都存在 ，检查通过！
		</font></b>
	<%} %>
	&nbsp;<br>
	</font></b>
	</td>
</tr>

<%if(propertiesValidator.hasErrorItem()) { %>
<tr style="background-color: silver; font-weight: 900;text-align: center;">
	<td width="7%">检查结果</td>
	<td>失败原因</td>
	<td width="12%">配置项名称</td>
	<td width="7%">是否必填</td>
	<td width="20%">配置项的值</td>
	<td width="30%">配置项作用</td>
</tr>

<%
	List errorItems = propertiesValidator.listAllErrorItem();
	for(int i = 0; i < errorItems.size(); i++) {
		AgentConfigItem errorItem = (AgentConfigItem)errorItems.get(i);
%>
<tr>
	<td><font color="red"><b>失败</b></font></td>
	<td><%=errorItem.getErrorMessage() %></td>
	<td><%=errorItem.getName() %></td>
	<td><%=errorItem.isNotNull()?"是":"否" %></td>
	<td><%=StringHelper.isEmpty(errorItem.getValue())?"&nbsp;":errorItem.getValue() %></td>
	<td><%=errorItem.getDesc() %></td>
</tr>
<%
	}
}
%>

</table>
</body>
</html>