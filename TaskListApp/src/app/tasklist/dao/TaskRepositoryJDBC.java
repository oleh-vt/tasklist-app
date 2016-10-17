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
	public List<Task> getTasks(boolean isCompleted) {
		List<Task> taskList = new LinkedList<>();
		
		try(Connection conn = ds.getConnection()){
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from tasklist_db.tasks where completed=" + Boolean.toString(isCompleted));
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

//				int id = rs.getInt("ID");
//				String name = rs.getString("NAME");
//				Timestamp deadline = rs.getTimestamp("DEADLINE");
//				String priority = rs.getString("PRIORITY");
//				String completed = rs.getString("COMPLETED");
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
	public void add(Task t) {
		try(Connection conn = ds.getConnection()){
			PreparedStatement stmt = conn.prepareStatement("insert into tasklist_db.tasks (name, deadline, priority) values (?, ?, ?)");
			stmt.setString(1, t.getName());
			stmt.setTimestamp(2, new Timestamp(t.getDeadline().getTime()));
			stmt.setString(3, t.getPriority().name().toLowerCase());
			stmt.executeUpdate();
			stmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

	}

	@Override
	public void markAsCompleted(int taskId) {
		try(Connection conn = ds.getConnection()){
			PreparedStatement stmt = conn.prepareStatement("update tasklist_db.tasks set completed=? where id=?");
			stmt.setBoolean(1, true);
			stmt.setInt(2, taskId);
			stmt.executeUpdate();
			stmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

}
