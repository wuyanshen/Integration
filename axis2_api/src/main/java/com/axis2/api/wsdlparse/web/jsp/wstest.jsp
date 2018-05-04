<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%
String ctxpath = request.getContextPath();
String locale = request.getParameter("locale");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="../../dbm_gwt/js/ext/resources/css/ext-all.css" />
		<style type="text/css">
		.x-panel-header{ border:1px ssolid #b5c3cf; border-bottom:none; background:url(title_bg.png) left top repeat-x}
		.panel_param label{ padding-left:18px; font-size:12px; font-weight:bold; color:#4d608a; background:url(icon_c.png) left center no-repeat}
		.panel_param .x-form-item-label{ padding-left:18px; font-weight:normal; color:#666; background:url(icon_e.png) left center no-repeat}
		.panel_param .x-panel{ padding-left:17px}
		.param_input .x-panel-bwrap{ border:1px solid #e6e5e5}
		.param_input .x-panel-bwrap .x-panel-bwrap{ border:none}
		.x-form-item{ padding:4px 0px; margin:0px}
		</style>
		<script type="text/javascript" src="../../js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript">jQuery = $;</script>
		<script type="text/javascript" src="../../js/MSG<%if(locale!=null && "en".equals(locale)){%>_en<%}%>.js"></script>
		<script type="text/javascript" src="../../dbm_gwt/js/ext/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="../../dbm_gwt/js/ext/ext-all.js"></script>
		<script type='text/javascript' src='../../js/dwrproxy.js'></script>
		<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>
		<script type="text/javascript" src='<%=request.getContextPath()%>/dwr/engine.js'></script>
		<script type="text/javascript" src='<%=request.getContextPath()%>/dwr/interface/wstestFacade.js'></script>
		<script type="text/javascript" src="../config/Patch.js"></script>
		<script type="text/javascript" src="data.js"></script>
		<script type="text/javascript" src="ParamPanel.js"></script>
		<script type="text/javascript" src="ReturnPanel.js"></script>
		<script type="text/javascript" src="MainPanel.js"></script>
		<script type="text/javascript" src="Main.js"></script>
		<title>WebService Tester Tool</title>
		<script type="text/javascript">
			//加载信息
    		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:MSG.PLEASE_WAIT});
			//将dwr异步改为同步
	    	dwr.engine.setAsync(false);
		</script>
	</head>
	<body>
	</body>
</html>