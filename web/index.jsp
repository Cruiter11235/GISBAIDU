<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<h1>Navigation</h1>
<button onclick="toBaidu()">百度地图页面</button>
<button onclick="toHoutai()">后台管理页面</button>
<script>
    function toBaidu(){
        location.href="http://localhost:8080/DeviceDemo_war_exploded/baidumap.html";
    }
    function toHoutai(){
        window.location.href="http://localhost:8080/DeviceDemo_war_exploded/device_list.jsp";
    }
</script>
</body>
</html>
<script src="./jquery.min.js"></script>
<script src="./index.js"></script>
