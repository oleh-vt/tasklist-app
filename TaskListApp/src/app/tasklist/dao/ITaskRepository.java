package app.tasklist.dao;

import java.util.List;

import app.tasklist.model.Task;

public interface ITaskRepository {
	public List<Task> getTasks(boolean completedOnly);
	public int add(Task t);
	public boolean markAsCompleted(int taskId);
}
