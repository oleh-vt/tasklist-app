package app.tasklist.commands;

import app.tasklist.Service;

public class UpdateCommand implements IServiceCommand {

	private int taskId;
	
	public UpdateCommand(int taskId) {
		this.taskId = taskId;
	}

	@Override
	public Result<? extends Object> execute(Service s) {
		boolean res = s.markCompleted(taskId);
		Result<Boolean> r = new Result<>(res);
		//r.setData(res);
		return r;
	}

}
