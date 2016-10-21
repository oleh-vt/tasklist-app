package app.tasklist.commands;

import app.tasklist.Service;

public interface IServiceCommand {
	public Result<? extends Object> execute(Service s);
}
