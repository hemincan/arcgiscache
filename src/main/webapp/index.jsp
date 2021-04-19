<%@page import="java.io.File" pageEncoding="utf-8"%>
<%@page import="util.ServerConfig"%>
<html>
<body>
<h2>图层列表</h2>
<%  
String path1 = request.getContextPath(); 

String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path1+"/";

String path = ServerConfig.getArcgisBasePath();
out.println("服务器文件夹："+path+"");
out.println("<br/>");
File fileDirectory = new File(path);
for (File temp : fileDirectory.listFiles()) {
	if (temp.isDirectory()) {
		 String errorMessage = "";
		  String tilePath  = temp+"/Layers";
	        if(!new File(tilePath).exists()){
	        	tilePath  = temp+"/图层";
	        }
	        if(!new File(tilePath).exists()){
	        	errorMessage = "（不包含'Layers'或者'图层'文件夹）";
	        	
	        }
		String str = temp.toString();
		String layerName = str.split("\\\\")[str.split("\\\\").length-1];
		out.println(layerName+""+errorMessage+""+"（"+basePath+"agstile?layer="+layerName+"&x={x}&y={y}&z={z}）");
		out.println("<br/>");
	}
}

%>   
</body>
</html>
