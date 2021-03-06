package app.tasklist;

import java.util.Iterator;
import java.util.List;

import app.tasklist.dao.ITaskRepository;
import app.tasklist.dao.StorageAccessException;
import app.tasklist.model.Task;

public class Service {
	
	private ITaskRepository taskRepo;
	private List<Task> taskList;
	
	public Service(ITaskRepository tr){
		this.taskRepo = tr;
		this.taskList = getTaskList(false);
	}
	
	public List<Task> getTaskList(boolean completedOnly){
		List<Task> list = null;
		try {
			list = taskRepo.getTasks(completedOnly);
		} catch (StorageAccessException e) {
			//e.printStackTrace();
		}
		return list;
	}
	
	public boolean addTask(Task task){
		boolean result = false;
		try {
			int createdId = taskRepo.add(task);
			if(createdId != -1){
				task.setId(createdId);
				taskList.add(task);
				result = true;
			}
		} catch (StorageAccessException e) {
			//e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean markCompleted(int taskId){
		Iterator<Task> iter = taskList.iterator();
		while(iter.hasNext()){
			Task curr = iter.next();
			if(curr.getId() == taskId){
				try {
					boolean success = taskRepo.markAsCompleted(curr);
					if(success){
						iter.remove();
						return true;
					}
				} catch (StorageAccessException e) {
					//e.printStackTrace();
				}
				break;
			}
		}
		return false;
	}
}
