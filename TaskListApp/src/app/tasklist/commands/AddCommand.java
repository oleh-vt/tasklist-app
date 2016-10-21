package app.tasklist.commands;

import app.tasklist.Service;
import app.tasklist.model.Task;

public class AddCommand implements IServiceCommand {
	
	private Task task;
	
	public AddCommand(Task task) {
		this.task = task;
	}

	@Override
	public Result<? extends Object> execute(Service s) {
		boolean res = s.addTask(task);
		Result<Boolean> r = new Result<>(res);
		//r.setData(res);
		return r;
	}

}
