package app.tasklist.commands;

import app.tasklist.Service;

public class UpdateCommand implements IServiceCommand {

	private int taskId;
	
	public UpdateCommand(int taskId) {
		this.taskId = taskId;
	}

	@Override
	public Object execute(Service s) {
		return s.markCompleted(taskId);
	}

}
