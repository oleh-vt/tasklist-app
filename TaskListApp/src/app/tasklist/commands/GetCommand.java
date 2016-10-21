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
	public Result<? extends Object> execute(Service s) {
		List<Task> list = s.getTaskList(completedOnly);
		Result<List<Task>> r = new Result<>(list);
		//r.setData(t);
		return r;
	}

}
