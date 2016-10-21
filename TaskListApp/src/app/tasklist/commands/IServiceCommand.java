package app.tasklist.commands;

import app.tasklist.Service;

public interface IServiceCommand {
	public Object execute(Service s);
}
