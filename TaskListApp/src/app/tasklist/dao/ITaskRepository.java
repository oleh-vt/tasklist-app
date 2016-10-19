package app.tasklist.dao;

import java.util.List;

import app.tasklist.model.Task;

public interface ITaskRepository {
	public List<Task> getTasks(boolean completedOnly) throws StorageAccessException;
	public int add(Task t) throws StorageAccessException;
	public int markAsCompleted(int taskId) throws StorageAccessException;
}
