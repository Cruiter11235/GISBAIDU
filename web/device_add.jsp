<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>添加设备</title>
</head>

<body>
	<input type="hidden" id="page_id" name="page_id" value="device_add"/>
	feature_type ：<input id="feature_type" name="feature_type"><br>
	feature_id ：<input id="feature_id" name="feature_id"><br>
	feature_name ：<input id="feature_name" name="feature_name"><br>
	longitude ：<input id="longitude" name="longitude"><br>
	latitude  ：<input id="latitude" name="latitude"><br>
	address   ：<input id="address" name="address"><br>
	<button id="add_button" name="add_button">确定添加</button>
</body>
</html>
<script src="jquery.min.js"></script>
<script src="device.js"></script>