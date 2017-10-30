<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<LINK href="style/propertiesStyle.css" type=text/css rel=stylesheet>
<% if(isPortal){ %>
<SCRIPT LANGUAGE="JavaScript" src="js/propertiesValidate.js" type="text/javascript"></SCRIPT>
<%} %>
<form action="sso.jsp" method="post" <%=isPortal ? "onSubmit='javascript:return doForm();'" : "" %>>
	<input type="hidden" name="isSubmitRequest" value="true">
	<input type="hidden" name="returnUrl" value="<%=returnUrl %>">
	<input type="hidden" name="secretKey" value="123456">
	<% if(isPortal){ %>
	<input type="hidden" name="isPortal" value="<%=isPortal %>">
	<%} %>
	
<%if(props != null && agent != null){%>
<div class="nomalwindow_td">
   <div class="font_white con_div_header" style="height:15px;padding:5px;text-decoration: none">SSO集成状态</div>
   <div class="con_div_body" id="con_div_body1">
	<div class="title" id="ssoOn-div">
	<div class="first_150">连接状态：</div>
	<div class="second">
		<%=(agent.isSocketAlive() && agent.useSSO()) ? "<font color='green'><b>Agent与IDS已连接</b></font>" : "<font color='red'><b>Agent与IDS未连接</b></font>"%>&nbsp;&nbsp;&nbsp;
	</div>
	</div>
	
	<% if(false == (agent.isSocketAlive() && agent.useSSO())){ %>
		<div class="title" >
		<div class="first_150" ></div>
		<div class="second">
		<b>请检查以下几项内容: </b><BR>
		1) 确认是否打开“使用IDS的SSO流程”的开关；<BR>
		2) 确认IDS身份服务器已经启动; <BR>
		3) 确认SSO集成配置文件配置正确；<BR>
		4) 确认当前协作应用已在IDS身份服务器注册成功；<BR>
		5) 尝试刷新几次当前页面；<a href="sso.jsp?secretKey=<%=rightKey %>">刷新</a><BR>
		6) 尝试重启Agent；<a href="sso.jsp?restartAgent=true">重启</a><BR>
		</div>
		</div>
	<% } %>
	
	<div class="title" ><!-- use in style, do not delete! --></div>
</div>
</div>

<div class="nomalwindow_td">
   <div class="font_white con_div_header" style="height:15px;padding:5px;text-decoration: none">SSO集成基本配置</div>
   <div class="con_div_body" id="con_div_body1">
   
	<!-- 与IDS做SSO集成必须的最基本的几个配置项 -----开始 -->
	<div class="title" id="ssoOn-div">
	<div class="first_150">使用IDS的SSO流程(<font color="red">*</font>)</div>
	<div class="second">
		<select name="ssoOn">
			<option value='true' <%=agent.useSSO() ? "selected" : "" %> >是</option>
			<option value='false' <%=false == agent.useSSO() ? "selected" : "" %>>否</option>
		</select>
	</div>
	<div class="third">默认为是，如果设为否，Agent中的Filter将会停止过滤应用所有的请求，SSO停止。</div>
	</div>
	
	<% if(isPortal){ %>
	<div class="title" id="idsHost-div">
	<div class="first_150">所要连接IDS的IP(<font color="red">*</font>)</div>
	<div class="second">
		<input id="idsHost" name="idsHost" value="<%=StringHelper.filterForHTMLValue(props.getProperty(AgentConfig.IDS_HOST)) %>">
		<span id="idsHost-Span"></span>
	</div>
	<div class="third">所要连接IDS的IP，如果是多个IDS集群，各节点IP使用空格分隔。</div>
	</div>
	
	<div class="title" id="idsPort-div">
	<div class="first_150">所要连接IDS的后台端口(<font color="red">*</font>)</div>
	<div class="second">
		<input id="idsPort" name="idsPort" value="<%=StringHelper.filterForHTMLValue(props.getProperty(AgentConfig.IDS_PORT)) %>">
		<span id="idsPort-Span"></span>
	</div>
	<div class="third">所要连接IDS的后台SSLServer端口，必填.如果不填，默认为2005。</div>
	</div>
	
	<div class="title" id="agentName-div">
		<div class="first_150">协作应用名(<font color="red">*</font>)</div>
		<div class="second">
			<input id="agentName" name="agentName" value="<%=StringHelper.filterForHTMLValue(props.getProperty(AgentConfig.NAME)) %>">
			<span id="agentName-Span"></span>
		</div>
		<div class="third">协作应用名. 必填, 作为IDS上注册的协作应用名；
		要求：2~80个字符、不允许使用中文、全角字符、' " \ / : ; * ? < > | % & # + 空格 下划线这些特殊字符。
		</div>
	</div>
	<%} %>
	
	<div class="title" ><!-- use in style, do not delete! --></div>
</div>
</div>

<% if(isPortal){ %>
<div class="nomalwindow_td">
   <div class="font_white con_div_header" >自动创建协作应用配置</div>
   <div class="con_div_body" id="con_div_body2">
	<!-- 与IDS集成简化，自动创建协作应用必须的几个配置项 -----开始 -->
	<div class="title" id="agentFirstStart-div">
	<div class="first_150">Agent第一次启动</div>
	<div class="second">
		<select name="agentFirstStart">
			<option value='true' <%=PropertyUtil.getPropertyAsBool(props, AgentConfig.FIRST_START, false) ? "selected" : "" %> >是</option>
			<option value='false' <%=false == PropertyUtil.getPropertyAsBool(props, AgentConfig.FIRST_START, false) ? "selected" : "" %>>否</option>
		</select>
	</div>
	<div class="third">
		如果设置为是，则Agent第一次启动时会通知IDS自动创建协作应用，启动完成以后，自动设置为false；
		默认为否，TRS产品与IDS简化集成，如果需要自动创建协作应用，请选择是。
	</div>
	</div>
	
	<div class="title" id="coAppType-div">
	<div class="first_150">协作应用的类型</div>
	<div class="second">
		<input id="coAppType" name="coAppType" value="<%=StringHelper.filterForHTMLValue(props.getProperty(AgentConfig.COAPP_TYPE)) %>">
		<span id="coAppType-Span"></span>
	</div>
	<div class="third">提供TRS产品名称即可，用于构造应用admin用户名， 例如"portal", "wcm"等；
	TRS产品与IDS简化集成，如果要自动创建协作应用时需要, 不允许包含空格' " \ / : ; * ? < > | % & # + 这些特殊字符。
	</div>
	</div>
	
	<div class="title" id="coAppVersion-div">
	<div class="first_150">协作应用的版本</div>
	<div class="second">
		<input id="coAppVersion" name="coAppVersion" value="<%=StringHelper.filterForHTMLValue(props.getProperty(AgentConfig.COAPP_VERSION)) %>">
		<span id="coAppVersion-Span"></span>
	</div>
	<div class="third">协作应用的版本，用于构造应用显示名称， 例如"V6.5Build1128"等；
	TRS产品与IDS简化集成，如果要自动创建协作应用时需要, 不允许包含空格 ' " \ / : ; * ? < > | % & # + 这些特殊字符。
	</div>
	</div>
	
	<div class="title" id="coAppRootUrl-div">
	<div class="first_150">协作应用的根路径地址</div>
	<div class="second">
		<input id="coAppRootUrl" name="coAppRootUrl" size="50" value="<%=StringHelper.avoidNull(StringHelper.filterForHTMLValue(props.getProperty(AgentConfig.COAPP_ROOTURL))) %>">
		<span id="coAppRootUrl-Span"></span>
	</div>
	<div class="third">TRS产品与IDS简化集成，如果要自动创建协作应用时需要，可以根据应用IP、端口、版本号构造出来；
	例如：http://192.168.0.3:8686/portal
	</div>
	</div>
	
	<div class="title" id="coAppNotifyUrl-div">
	<div class="first_150">IDS通知应用的URL</div>
	<div class="second">
		<input id="coAppNotifyUrl" name="coAppNotifyUrl" size="50" value="<%=StringHelper.avoidNull(StringHelper.filterForHTMLValue(props.getProperty(AgentConfig.COAPP_NOTIFY_URL))) %>">
		<span id="coAppNotifyUrl-Span"></span>
	</div>
	<div class="third">该URL是身份服务器与协作应用进行通信的基础，需要配置成被单点登录Filter可以拦截的URL，比如后缀为jsp、php、asp等动态页面的地址, 后缀不可为htm,html等静态页面地址。
		  TRS产品与IDS简化集成，如果要自动创建协作应用时需要，可以根据应用IP、端口、版本号构造出来。
		  例如：http://192.168.0.3:8686/portal/loginpage.jsp
	</div>
	</div>
	
	<div class="title" id="groupSynchronize-div">
	<div class="first_150">开通组织同步</div>
	<div class="second">
		<select name="groupSynchronize">
			<option value='true' <%=PropertyUtil.getPropertyAsBool(props, AgentConfig.COAPP_GROUP_SYNCHRONIZE, false) ? "selected" : "" %> >是</option>
			<option value='false' <%=false == PropertyUtil.getPropertyAsBool(props, AgentConfig.COAPP_GROUP_SYNCHRONIZE, false) ? "selected" : "" %>>否</option>
		</select>
	</div>
	<div class="third">
		如果设置为是，则会通知IDS创建协作应用时，自动开通协作应用的组织同步方式为Agent/Actor方式同步；
		TRS产品与IDS简化集成，如果要自动创建协作应用时需要；第一次执行完成以后，会自动设置为false。
	</div>
	</div>
	
	<div class="title" id="userSynchronize-div">
	<div class="first_150">开通用户同步</div>
	<div class="second">
		<select name="userSynchronize">
			<option value='true' <%=PropertyUtil.getPropertyAsBool(props, AgentConfig.COAPP_USER_SYNCHRONIZE, false) ? "selected" : "" %> >是</option>
			<option value='false' <%=false == PropertyUtil.getPropertyAsBool(props, AgentConfig.COAPP_USER_SYNCHRONIZE, false) ? "selected" : "" %>>否</option>
		</select>
	</div>
	<div class="third">
		如果设置为是，则会通知IDS创建协作应用时，自动开通协作应用的用户同步方式为Agent/Actor方式同步；
		TRS产品与IDS简化集成，如果要自动创建协作应用时需要；第一次执行完成以后，会自动设置为false。
	</div>
	</div>
	<!-- 与IDS集成简化，自动创建协作应用必须的几个配置项 -----结束 -->
	
	<div class="title" ><!-- use in style, do not delete! --></div>
</div>
</div>
<%
}}
%>

	<div class="btm_div">
		  <span><input type="submit" name="bSubmit" value="应用" ></span>
		  <span><input type="button" name="bClose" value="关闭" onClick="window.close();" ></span>
	</div>
</form>