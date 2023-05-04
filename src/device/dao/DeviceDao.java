package device.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import device.dao.Db;

public class DeviceDao {
	public void showDebug(String msg){
		System.out.println("["+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())+"][device/dao/Db]"+msg);
	}
	/*添加记录*/
	public void addDeviceRecord(Data data, JSONObject json) throws JSONException, SQLException {
		//构造sql语句，根据传递过来的条件参数
		String feature_type=data.getParam().has("feature_type")?data.getParam().getString("feature_type"):null;
		String feature_id=data.getParam().has("feature_id")?data.getParam().getString("feature_id"):null;
		String feature_name=data.getParam().has("feature_name")?data.getParam().getString("feature_name"):null;
		String longitude=data.getParam().has("longitude")?data.getParam().getString("longitude"):null;
		String latitude=data.getParam().has("latitude")?data.getParam().getString("latitude"):null;
		String address=data.getParam().has("address")?data.getParam().getString("address"):null;

		if(feature_id!=null&&feature_type!=null&&feature_name!=null&&longitude!=null&&latitude!=null&&address!=null){
			String sql = String.format(
					"insert into gis_feature(" +
							"feature_type," +
							"feature_id," +
							"feature_name," +
							"longitude," +
							"latitude," +
							"address) values('%s','%s','%s',%s,%s,'%s')",feature_type,feature_id,feature_name,longitude,latitude,address);
			data.getParam().put("sql",sql);
			System.out.println(sql);
			updateRecord(data,json);
		}
	}
	public void addFeatureRecord(Data data, JSONObject json) throws JSONException, SQLException{
		String feature_type=data.getParam().has("feature_type")?data.getParam().getString("feature_type"):null;
		String feature_content=data.getParam().has("feature_content")?data.getParam().getString("feature_content"):null;
		String sql = String.format(
				"insert into gis_feature(" +
						"feature_type," +
						"feature_content) values('%s','%s')",feature_type,feature_content

		);
		data.getParam().put("sql",sql);
		System.out.println(sql);
		updateRecord(data,json);
	}
	/*删除记录*/
	public void deleteDeviceRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		if(id!=null){
			String sql="delete from gis_feature where id="+data.getParam().getString("id");
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}
	/*修改记录*/
	public void modifyDeviceRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		String feature_type=data.getParam().has("feature_type")?data.getParam().getString("feature_type"):null;
		String feature_id=data.getParam().has("feature_id")?data.getParam().getString("feature_id"):null;
		String feature_name=data.getParam().has("feature_name")?data.getParam().getString("feature_name"):null;
		String longitude=data.getParam().has("longitude")?data.getParam().getString("longitude"):null;
		String latitude=data.getParam().has("latitude")?data.getParam().getString("latitude"):null;
		String address=data.getParam().has("address")?data.getParam().getString("address"):null;
		if(id!=null){
			String sql = String.format(
					"update gis_feature set " +
							"feature_type='%s'," +
							"feature_id='%s'," +
							"feature_name='%s'," +
							"longitude=%s," +
							"latitude=%s," +
							"address='%s' where id=%s",feature_type,feature_id,feature_name,longitude,latitude,address,id);
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}
	/*查询记录*/
	public void getDeviceRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的查询条件参数
		String sql=createGetRecordSql(data);			//构造sql语句，根据传递过来的查询条件参数
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	public void getFeatureRecord(Data data,JSONObject json) throws JSONException,SQLException{
		String sql=createGetFeatureSql(data);
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	/*
	 * 这是一个样板的函数，可以拷贝做修改用
	 */
	private void updateRecord(Data data,JSONObject json) throws JSONException, SQLException{
		/*--------------------获取变量 开始--------------------*/
		JSONObject param=data.getParam();
		int resultCode=0;
		String resultMsg="ok";
		/*--------------------获取变量 完毕--------------------*/
		/*--------------------数据操作 开始--------------------*/
		Db updateDb = new Db("test");
		String sql=data.getParam().getString("sql");
		showDebug("[updateRecord]"+sql);
		updateDb.executeUpdate(sql);
		updateDb.close();
		/*--------------------数据操作 结束--------------------*/
		/*--------------------返回数据 开始--------------------*/
		json.put("result_msg",resultMsg);															//如果发生错误就设置成"error"等
		json.put("result_code",resultCode);														//返回0表示正常，不等于0就表示有错误产生，错误代码
		/*--------------------返回数据 结束--------------------*/
	}
	private void queryRecord(Data data,JSONObject json) throws JSONException, SQLException{
		/*--------------------获取变量 开始--------------------*/
		String resultMsg = "ok";
		int resultCode = 0;
		List jsonList = new ArrayList();
		/*--------------------获取变量 完毕--------------------*/
		/*--------------------数据操作 开始--------------------*/
		Db queryDb = new Db("test");
		String sql=data.getParam().getString("sql");
		showDebug("[queryRecord]构造的SQL语句是：" + sql);
		try {
			ResultSet rs = queryDb.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int fieldCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 0; i < fieldCount; i++) {
					map.put(rsmd.getColumnName(i + 1), rs.getString(rsmd.getColumnName(i + 1)));
				}
				jsonList.add(map);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			showDebug("[queryRecord]查询数据库出现错误：" + sql);
			resultCode = 10;
			resultMsg = "查询数据库出现错误！" + e.getMessage();
		}
		queryDb.close();
		/*--------------------数据操作 结束--------------------*/
		/*--------------------返回数据 开始--------------------*/
		json.put("aaData",jsonList);
		json.put("result_msg",resultMsg);															//如果发生错误就设置成"error"等
		json.put("result_code",resultCode);														//返回0表示正常，不等于0就表示有错误产生，错误代码
		/*--------------------返回数据 结束--------------------*/
	}

	private String createGetRecordSql(Data data) throws JSONException {
		String sql="select * from gis_feature";
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		if(id!=null)
			sql=sql+" where id="+id;
		return sql;
	}
	private String createGetFeatureSql(Data data) throws JSONException{
		String feature_type=data.getParam().has("feature_type")?data.getParam().getString("feature_type"):null;
		String sql = String.format("select * from gis_feature where feature_type='%s'",feature_type);
		return sql;
	}
}
