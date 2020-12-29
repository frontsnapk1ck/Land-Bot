package alloy.command.util;

import alloy.input.discord.AlloyInputData;
import alloy.utility.discord.perm.AlloyPerm;
import alloy.utility.discord.perm.DisPerm;

public abstract class AbstractCommand {

	private String command;
	private String description;
	private String[] usage;
	private String[] aliases;
	private DisPerm permission;

	public AbstractCommand() 
	{
		this.permission = AlloyPerm.MESSAGE_WRITE;
	}

	public abstract void execute(AlloyInputData data);

	public String getCommand()
	{
		return this.command;
	}

	public String getDescription()
	{
		return this.description;
	}

	public String[] getUsage()
	{
		return this.usage;
	}

	public String[] getAliases()
	{
		return this.aliases;
	}

	public DisPerm getPermission()
	{
		return this.permission;
	}

	public void setAliases(String[] aliases) 
	{
		this.aliases = aliases;
	}

	public void setCommand(String command) 
	{
		this.command = command;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public void setUsage(String[] usage) 
	{
		this.usage = usage;
	}

	public void setPermission(DisPerm permission) 
	{
		this.permission = permission;
	}
}
