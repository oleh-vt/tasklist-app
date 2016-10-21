package app.tasklist.commands;

import java.util.List;

import app.tasklist.Service;
import app.tasklist.model.Task;

public class GetCommand implements IServiceCommand {

	private boolean completedOnly;
	
	public GetCommand(boolean completedOnly) {
		this.completedOnly = completedOnly;
	}

	@Override
	public Object execute(Service s) {
		return s.getTaskList(completedOnly);
	}

}
