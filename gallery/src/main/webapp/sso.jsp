<%@ page contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@page import="com.trs.idm.interact.agent.IAgent"%>
<%@page import="java.util.Properties"%>
<%@page import="com.trs.idm.client.filter.GeneralSSOFilter"%>
<%@page import="com.trs.idm.interact.agent.AgentConfig"%>
<%@page import="com.trs.idm.util.PropertyUtil"%>
<%@page import="com.trs.idm.util.StringHelper"%>
<%@page import="com.trs.idm.util.RequestUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.trs.idm.interact.agent.AgentFactory"%>
<%
	String secretKey = RequestUtil.getParameterSafe(request, "secretKey");
    String rightKey = "123456";
	if(StringHelper.isEmpty(secretKey) || !rightKey.equals(secretKey)){
	 	response.sendError(404);
	 	return ;
	 } 

	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", -1);
	response.setDateHeader("max-age", 0);

	String returnUrl = RequestUtil.getParameterAndTrim(request, "returnUrl");
	IAgent agent = (IAgent) application.getAttribute("IDSAgent");
	Properties props = null;
	if (agent != null) {
		props = agent.getProperties();
	} else {
		agent = AgentFactory.getInstance();
		agent = agent.initAndStart();
		props = PropertyUtil.assertAndLoadFromResource(GeneralSSOFilter.class, AgentConfig.CFG_FILE);
	}
	
	// 重启Agent
	boolean restartAgent = RequestUtil.getParameterAsBool(request, "restartAgent");
	if(restartAgent && agent != null){
		agent = agent.restart();
		props = agent.getProperties();
		%>
		<script type="text/javascript">
		<!--
		alert("agent重启成功!");
		document.location.href="sso.jsp?secretKey="+<%=rightKey%>;
		//-->
		</script>
		<%
		return;
	}
	
	// 提交后执行修改
	boolean isSubmitRequest = RequestUtil.getParameterAsBool(request, "isSubmitRequest");
	boolean isPortal = RequestUtil.getParameterAsBool(request, "isPortal");
	if (request.getMethod().equalsIgnoreCase("POST") && isSubmitRequest && agent != null){
		isSubmitRequest = false;
		boolean ssoOn = RequestUtil.getParameterAsBool(request, "ssoOn");
		boolean agentFirstStart = RequestUtil.getParameterAsBool(request, "agentFirstStart");
		String idsHost = RequestUtil.getParameterAndTrim(request, "idsHost");
		String idsPort = RequestUtil.getParameterAndTrim(request, "idsPort");
		String agentName = RequestUtil.getParameterAndTrim(request, "agentName");
		
		String coAppType = RequestUtil.getParameterAndTrim(request, "coAppType");
		String coAppVersion = RequestUtil.getParameterAndTrim(request, "coAppVersion");
		String coAppRootUrl = RequestUtil.getParameterAndTrim(request, "coAppRootUrl");
		String coAppNotifyUrl = RequestUtil.getParameterAndTrim(request, "coAppNotifyUrl");
		boolean groupSynchronize = RequestUtil.getParameterAsBool(request, "groupSynchronize");
		boolean userSynchronize = RequestUtil.getParameterAsBool(request, "userSynchronize");
		Map propertiesMap = new HashMap();
		propertiesMap.put(AgentConfig.SSO_ON, String.valueOf(ssoOn));
		
		// 其它设置，只保留给portal应用使用，不对外开放
		if(isPortal){
			if(StringHelper.isEmpty(idsHost)){
				idsHost = props.getProperty(AgentConfig.IDS_HOST);
			}
			if(StringHelper.isEmpty(idsPort)){
				idsPort = props.getProperty(AgentConfig.IDS_PORT);
			}
		/*	if(StringHelper.isEmpty(coAppType)){
				coAppType = props.getProperty(AgentConfig.COAPP_TYPE);
			}
			if(StringHelper.isEmpty(coAppVersion)){
				coAppVersion = props.getProperty(AgentConfig.COAPP_VERSION);
			}*/
			if(StringHelper.isEmpty(agentName)){
				agentName = props.getProperty(props.getProperty(AgentConfig.NAME));
			}
			if(StringHelper.isEmpty(coAppRootUrl)){
				coAppRootUrl = props.getProperty(AgentConfig.COAPP_ROOTURL);
			}
			if(StringHelper.isEmpty(coAppNotifyUrl)){
				coAppNotifyUrl = props.getProperty(AgentConfig.COAPP_NOTIFY_URL);
			}
			propertiesMap.put(AgentConfig.IDS_HOST, idsHost);
			propertiesMap.put(AgentConfig.IDS_PORT, idsPort);
			propertiesMap.put(AgentConfig.NAME, agentName);
			
			propertiesMap.put(AgentConfig.FIRST_START, String.valueOf(agentFirstStart));
			propertiesMap.put(AgentConfig.COAPP_TYPE, coAppType);
			propertiesMap.put(AgentConfig.COAPP_VERSION, coAppVersion);
			propertiesMap.put(AgentConfig.COAPP_ROOTURL, coAppRootUrl);
			propertiesMap.put(AgentConfig.COAPP_NOTIFY_URL, coAppNotifyUrl);
			propertiesMap.put(AgentConfig.COAPP_GROUP_SYNCHRONIZE, String.valueOf(groupSynchronize));
			propertiesMap.put(AgentConfig.COAPP_USER_SYNCHRONIZE, String.valueOf(userSynchronize));
		}
		
		boolean result = false;
		try{
			result = agent.updateProperties(propertiesMap);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		// 如果只提交ssoOn配置，不要求自动创建，则不需要重启
		if(agent != null && false == agentFirstStart){
			agent.setSSOSwitch(ssoOn);
		}
		
		//如果要求自动创建协作应用,则检查应用名是否已经在IDS注册
		if(agentFirstStart){
			if(agent.exist(agentName)){
				%>
				<script type="text/javascript">
				<!--
					alert("协作应用名[<%=agentName %>]在IDS已占用,无法自动创建!");
					document.location.href="sso.jsp?secretKey="+<%=rightKey%>;
				//-->
				</script>
				<%
				//重启Agent
				agent = agent.restart();
				props = agent.getProperties();
				return;
			}
		}
		
		
		if(result){
			%>
			<script type="text/javascript">
			<!--
				alert("修改成功!");
				document.location.href="sso.jsp?secretKey="+<%=rightKey%>;
			//-->
			</script>
			<%
			//重启Agent
			agent = agent.restart();
			props = agent.getProperties();
		}

		if(false == result){
			%>
			<script type="text/javascript">
			<!--
				alert("修改失败，请重试!");
				document.location.href="sso.jsp?secretKey="+<%=rightKey%>;
			//-->
			</script>
			<%
			
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>TRS IDS Agent Properties Configuration</title>
</head>
<body>

<%@ include file="properties.jsp" %>
</body>
</html>