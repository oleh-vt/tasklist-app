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

public class TaskRepositoryJDBC implements ITaskRepository {
	
	private DataSource ds;
	
	public TaskRepositoryJDBC(DataSource ds) {
		super();
		this.ds = ds;
	}

	@Override
	public List<Task> getTasks(boolean completedOnly) {
		List<Task> taskList = new LinkedList<>();
		
		try(Connection conn = ds.getConnection()){
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from tasklist_db.tasks where completed=" + Boolean.toString(completedOnly));
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
			rs.close();
			stmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return taskList;
	}

	@Override
	public int add(Task t) {
		int addedRecordID = -1;
		try(Connection conn = ds.getConnection()){
			PreparedStatement stmt = conn.prepareStatement("insert into tasklist_db.tasks (name, deadline, priority) "
															+ "values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, t.getName());
			stmt.setTimestamp(2, new Timestamp(t.getDeadline().getTime()));
			stmt.setString(3, t.getPriority().name().toLowerCase());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next())
				addedRecordID = rs.getInt(1);
			rs.close();
			stmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return addedRecordID;
	}

	@Override
	public boolean markAsCompleted(int taskId) {
		boolean success = false;
		try(Connection conn = ds.getConnection()){
			PreparedStatement stmt = conn.prepareStatement("update tasklist_db.tasks set completed=? where id=?");
			stmt.setBoolean(1, true);
			stmt.setInt(2, taskId);
			int r = stmt.executeUpdate();
			if(r > 0)
				success = true;
			stmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return success;
	}

}
