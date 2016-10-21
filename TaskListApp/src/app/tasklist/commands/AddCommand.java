package app.tasklist.commands;

import app.tasklist.Service;
import app.tasklist.model.Task;

public class AddCommand implements IServiceCommand {
	
	private Task task;
	
	public AddCommand(Task task) {
		this.task = task;
	}

	@Override
	public Object execute(Service s) {
		return s.addTask(task);
	}

}
