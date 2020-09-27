package com.dbs.dao;

import com.dbs.entity.OrderInfo;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class DbsDao {

	@Resource(name = "primaryJdbcTemplate")
	protected JdbcTemplate jdbcTemplate1;
	@Resource(name = "secondaryJdbcTemplate")
	protected JdbcTemplate jdbcTemplate2;

	public List<Map<String, Object>> selectData(int startNumber, Integer limit,
		String selectSql) {
		String sql=selectSql+" limit  "+startNumber+","+limit;
		List<Map<String, Object>> fromTable = jdbcTemplate1.queryForList(sql);
		return fromTable;
	}


	public Map<String, Object> getCoiunt(
		String selectSql) {
		String sql="select count(*)as sum from ("+selectSql+" ) t";
		return jdbcTemplate1.queryForMap(sql);
	}

	public int[] insertData(List<Map<String, Object>> dataMap,String insertSql,List<String> colLsit) {
		return  jdbcTemplate2.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			@Override
			public int getBatchSize() {
				return dataMap.size();
				//这个方法设定更新记录数，通常List里面存放的都是我们要更新的，所以返回list.size();
			}
			@Override
			public void setValues(PreparedStatement ps, int i)throws SQLException {
				Map<String, Object> linkset = dataMap.get(i);
				for (int j = 0; j < colLsit.size(); j++) {
					ps.setObject(j+1, linkset.get(colLsit.get(j)));
				}
			}
		});


}
	public OrderInfo testFileTwo() {
		String sql1 = "select * from mwq where id=1049";
		//String sql2 = "insert  into mwq(blodtest) values(?) ";
		List<Map<String, Object>> familyMemberList = jdbcTemplate2.queryForList(sql1);
		//jdbcTemplate1.update(sql2,new Object[]{familyMemberList.get(0).get("blodtest")});
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setByteName((byte[]) familyMemberList.get(0).get("blodtest"));
		return orderInfo;
	}
	public void testFile(FileInputStream fi) {
		String sql2 = "INSERT INTO `sirmtest`.`mwq`(`blodtest`, `datetest`,test) VALUES ( 0xE982AEE7AEB1E59CB0E59D80EFBC9A77712E6D614073696E6974656B2E636F6D0D0AE982AEE7AEB1E5AF86E7A081EFBC9A7039433177746C470D0A73766EE8B4A6E58FB7E982AEE7AEB1E5898DE7BC80EFBC8CE5AF86E7A081EFBC9A634B73464B4F30440D0A0D0A474954E8B4A6E58FB7EFBC9A77712E6D61EFBC8CE5AF86E7A081EFBC9A79566E51547642760D0A0D0A77286168736F3A6F37497842, '2020-09-25',?)";
		ArrayList<Integer> a=new ArrayList<Integer>(10000);
		for (int i = 0; i <10000 ; i++) {
			a.add(0);
		}
		jdbcTemplate1.batchUpdate(sql2, new BatchPreparedStatementSetter() {
			@Override
			public int getBatchSize() {
				return a.size();
			}
			@Override
			public void setValues(PreparedStatement ps, int i)throws SQLException {
				Integer linkset = a.get(i);
				ps.setObject(1, linkset);
			}
		});
	}

}
