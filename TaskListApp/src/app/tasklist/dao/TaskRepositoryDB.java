package app.tasklist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import app.tasklist.model.Priority;
import app.tasklist.model.Task;

public class TaskRepositoryDB implements ITaskRepository {
	
	private DataSource ds;
	
	public TaskRepositoryDB(DataSource ds) {
		super();
		this.ds = ds;
	}

	@Override
	public List<Task> getTasks(boolean completedOnly) throws StorageAccessException {
		List<Task> taskList = new LinkedList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from tasklist_db.tasks where completed=" + Boolean.toString(completedOnly));
			while(rs.next()){
				Task task = new Task();
				
				task.setId(rs.getInt("ID"));
				task.setName(rs.getString("NAME"));
				
				Timestamp ts = rs.getTimestamp("DEADLINE");
				task.setDeadline(new Date(ts.getTime()));
				
				String priority = rs.getString("PRIORITY");
				task.setPriority(Priority.valueOf(priority.toUpperCase()));
				
				task.setCompleted(rs.getBoolean("COMPLETED"));
				
				taskList.add(task);
			}
			
		}
		catch(SQLException e){
			throw new StorageAccessException(e);
		}
		finally{
			try {rs.close();} catch (Exception e)	{/*ignored*/}
			try {stmt.close();} catch (Exception e)	{/*ignored*/}
			try {conn.close();} catch (Exception e)	{/*ignored*/}
		}
		return taskList;
	}

	@Override
	public int add(Task t) throws StorageAccessException {
		int addedRecordID = -1;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			conn = ds.getConnection();
			stmt = conn.prepareStatement("insert into tasklist_db.tasks (name, deadline, priority) "
															+ "values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, t.getName());
			stmt.setTimestamp(2, new Timestamp(t.getDeadline().getTime()));
			stmt.setString(3, t.getPriority().name().toLowerCase());
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if(rs.next())
				addedRecordID = rs.getInt(1);
		}
		catch(SQLException e){
			throw new StorageAccessException(e);
		}
		finally{
			try {rs.close();} catch (Exception e)	{/*ignored*/}
			try {stmt.close();} catch (Exception e)	{/*ignored*/}
			try {conn.close();} catch (Exception e)	{/*ignored*/}
		}
		return addedRecordID;
	}

	@Override
	public int markAsCompleted(int taskId) throws StorageAccessException {
		int result = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try{
			conn = ds.getConnection();
			stmt = conn.prepareStatement("update tasklist_db.tasks set completed=? where id=?");
			stmt.setBoolean(1, true);
			stmt.setInt(2, taskId);
			result = stmt.executeUpdate();
		}
		catch(SQLException e){
			throw new StorageAccessException(e);
		}
		finally{
			try {stmt.close();} catch (Exception e)	{/*ignored*/}
			try {conn.close();} catch (Exception e)	{/*ignored*/}
		}
		return result;
	}

}
