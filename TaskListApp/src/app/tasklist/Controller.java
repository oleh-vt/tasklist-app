package app.tasklist;

import app.tasklist.commands.IServiceCommand;

public class Controller {

	private Service service;
	private ConsoleView cw;
	
	Controller(Service service, ConsoleView cw) {
		super();
		this.service = service;
		this.cw = cw;
	}
	
	public Object handleCommand(IServiceCommand cmd){
		return cmd.execute(service);
	}
	
	public void start(){
		cw.getUserCommand();
	}

}
