package app.tasklist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import app.tasklist.dao.ITaskRepository;
import app.tasklist.model.Priority;
import app.tasklist.model.Task;

public class Service {
	
	private ITaskRepository taskRepo;
	private String datePattern = "yyyy-MM-dd HH:mm";
	private List<Task> taskList;
	
	public Service(ITaskRepository tr, String datePattern){
		this.taskRepo = tr;
		this.datePattern = datePattern;
		this.taskList = taskRepo.getTasks(false);
	}
	
	public List<Task> getTaskList(boolean completedOnly){
		if(!completedOnly)
			return this.taskList;
		else
			return taskRepo.getTasks(completedOnly);
	}
	
	public boolean addTask(Map<String, String> params) throws Exception{
		
		String taskName = params.get("name");
		String ddline = params.get("deadline");
		int prrty = Integer.parseInt(params.get("priority"));
		
		if(taskName == null || taskName.trim().isEmpty())
			throw new Exception("Name is incorrect");
		
		SimpleDateFormat df = new SimpleDateFormat(datePattern);
		Date deadline = null;
		deadline = df.parse(ddline);

		Priority[] priorities = Priority.values();
		if(prrty > priorities.length || prrty < 1)
			throw new Exception("Priority is incorrect");
		
		Task task = new Task(taskName, deadline, priorities[prrty-1]);
		
		int createdId = taskRepo.add(task);
		if(createdId != -1){
			task.setId(createdId);
			taskList.add(task);
			return true;
		}
		return false;
	}
	
	public boolean markCompleted(int taskId){
		boolean completed = taskRepo.markAsCompleted(taskId);
		if(completed){
			Iterator<Task> iter = taskList.iterator();
			while(iter.hasNext()){
				Task curr = iter.next();
				if(curr.getId() == taskId){
					iter.remove();
					break;
				}
			}
			return true;
		}
		return false;
	}
}
