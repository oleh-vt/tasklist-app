package app.tasklist.dao;

import java.util.List;

import app.tasklist.model.Task;

public interface ITaskRepository {
	//public List<Task> find();
	//public List<Task> findCompleted();
	public List<Task> getTasks(boolean isCompleted);
	public void add(Task t);
	public void markAsCompleted(int taskId);
}
