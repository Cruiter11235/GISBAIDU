package device.file;
/*
 * 待完成：用MVC模式分开DB和Action操作
 * 增删改查看导印统功能的实现
 */
import device.dao.Data;
import device.dao.DeviceDao;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServletAction extends HttpServlet {
	String module="device";
	String sub="file";
	public void showDebug(String msg){
		System.out.println("["+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())+"]["+module+"/"+sub+"/ServletAction]"+msg);
	}
	/*
	 * 处理顺序：先是service，后根据情况doGet或者doPost
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		processAction(request,response);
	}
	/*========================================函数分流 开始========================================*/
	public void processAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		boolean actionOk = false;
		int resultCode=0;
		String resultMsg="ok";
		JSONObject json=new JSONObject(); 
		showDebug("[processAction]收到的action是："+action);
		if (action == null){
			resultMsg="传递过来的action是NULL";
		}else{
			//这几个常规增删改查功能
			if (action.equals("get_device_record")) {
				actionOk=true;
				try {
					getDeviceRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("add_device_record")) {
				actionOk=true;
				try {
					addDeviceRecord(request, response, json);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("modify_device_record")) {
				actionOk=true;
				try {
					modifyDeviceRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("delete_device_record")) {
				actionOk=true;
				try {
					deleteDeviceRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//获取feature信息，需要参数type
			if(action.equals("get_infos")){
				actionOk=true;
				try{
					getfeatureinfo(request,response,json);
				}catch (Exception e){
					e.printStackTrace();
				}
				System.out.println("收到请求points");
			}
			if(action.equals("add_infos")){
				actionOk=true;
				try{
					addfeatureinfo(request,response,json);
				}catch(Exception e){
					e.printStackTrace();
				}
				System.out.println("收到add");
			}
			try {
				responseBack(request,response,json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	/*========================================函数分流 结束========================================*/
	/*========================================公共函数 开始========================================*/
	private Data getPageParameters(HttpServletRequest request,HttpServletResponse response,JSONObject json) throws JSONException{
		Data data=new Data();
		HttpSession session = request.getSession();
		/*----------------------------------------获取所有表单信息 开始----------------------------------------*/
		showDebug("[getPageParameters]----------------------------------------获取所有表单信息 开始----------------------------------------");
		JSONObject param=data.getParam();
		Enumeration requestNames=request.getParameterNames();
		for(Enumeration e=requestNames;e.hasMoreElements();){
			String thisName=e.nextElement().toString();
			String thisValue=request.getParameter(thisName);
			showDebug("[getPageParameters]"+thisName+"="+thisValue);
			param.put(thisName, thisValue);
		}
		showDebug("[getPageParameters]data的Param="+data.getParam().toString());
		showDebug("[getPageParameters]----------------------------------------获取所有表单信息 完毕----------------------------------------");
		/*----------------------------------------获取所有表单信息 完毕----------------------------------------*/
		return data;
	}
	private void responseBack(HttpServletRequest request,HttpServletResponse response,JSONObject json) throws JSONException {
		boolean isAjax=true;if (request.getHeader("x-requested-with") == null || request.getHeader("x-requested-with").equals("com.tencent.mm")){isAjax=false;}	//判断是异步请求还是同步请求，腾讯的特殊
		if(isAjax){
			response.setContentType("application/json; charset=UTF-8");
			try {
				response.getWriter().print(json);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			String action=json.getString("action");
			String errorNo="0";
			String errorMsg="ok";
			String url = module+"/"+sub+"/result.jsp?action="+action+"&result_code="+errorNo+ "&result_msg=" + errorMsg;
			if(json.has("redirect_url")) url=json.getString("redirect_url");
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/*========================================公共函数 结束========================================*/
	/*========================================CRUD业务函数 开始========================================*/
	private void getDeviceRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		DeviceDao dao=new DeviceDao();
		Data data=getPageParameters(request,response,json);
		dao.getDeviceRecord(data,json);
	}
	private void modifyDeviceRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		DeviceDao dao=new DeviceDao();
		Data data=getPageParameters(request,response,json);
		dao.modifyDeviceRecord(data,json);
	}
	private void deleteDeviceRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		DeviceDao dao=new DeviceDao();
		Data data=getPageParameters(request,response,json);
		dao.deleteDeviceRecord(data,json);
	}
	private void addDeviceRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		DeviceDao dao=new DeviceDao();
		Data data=getPageParameters(request,response,json);
		dao.addDeviceRecord(data,json);
	}
	private void addfeatureinfo(HttpServletRequest request,HttpServletResponse response,JSONObject json) throws JSONException,SQLException{
		DeviceDao dao=new DeviceDao();
		Data data=getPageParameters(request,response,json);
		dao.addFeatureRecord(data,json);
	}
	private void getfeatureinfo(HttpServletRequest request,HttpServletResponse response,JSONObject json) throws JSONException,SQLException{
		DeviceDao dao=new DeviceDao();
		//将url的params变成data对象里的json
		Data data=getPageParameters(request,response,json);
		dao.getFeatureRecord(data,json);
	}
	/*========================================CRUD业务函数 结束========================================*/
}
