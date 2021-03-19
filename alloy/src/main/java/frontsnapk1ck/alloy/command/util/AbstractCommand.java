package frontsnapk1ck.alloy.command.util;

import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.utility.discord.perm.AlloyPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;

public abstract class AbstractCommand {

	/** the trigger of the command */
	private String command;

	/** the description of the command */
	private String description;

	/** the different ways that this command can be used */
	private String[] usage;

	/** the different names that this command may go by */
	private String[] aliases;

	/** the permission that is required to use this command */
	private DisPerm permission;

	/**
	 * sets the field variables to their default values
	 */
	public AbstractCommand() 
	{
		setPermission(AlloyPerm.MESSAGE_WRITE);
		setAliases(new String[]{});
		setUsage(new String[]{});
		setCommand("");
		setDescription("");
	}

	/**
	 * <p>to be implemented by the commands </p>
	 * 
	 * <p>this is the method that will be called upon the input of a command
	 * from any source. as such this will be overridden and modified for each 
	 * command as needed</p>
	 * 
	 * <p>typical implementation of this will include 
	 * <blockquote><pre>
	 *	Guild guild = data.getGuild();
     *	User author = data.getUser();
     *	String[] args = AlloyInputUtil.getArgs(data);
     *	Sendable bot = data.getSendable();
     *	TextChannel channel = data.getChannel();
	 * </pre></blockquote>
	 * because all the information is stored in the {@link AlloyInputData} 
	 * variable and still needs to be extracted out </p>
	 * 
	 * <p>this method is the backbone of all interactions with the 
	 * program, and is quite important. as such it is important to 
	 * make this method efficient and put any intensive jobs or calls 
	 * in a job that is fed to the event queue. this is run on the discord 
	 * thread directly so any delays will impact the performance of the bot 
	 * as a whole</p>
	 * 
	 * @param data the information from discord and other 
	 * 				sources about this command
	 */
	public abstract void execute(AlloyInputData data);

	/**
	 * 
	 * @return the trigger for this command
	 */
	public String getCommand()
	{
		return this.command;
	}

	/**
	 * 
	 * @return the description of this command
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * builds this class up in a quite convoluted way. used the child class 
	 * to load the information about this command, then takes the information
	 * from that child class and returns it though the {@code getUsageActual()} 
	 * method
	 * @see CommandInfoLoader
	 * @see AbstractCommand#getUsageActual()
	 * @return the different ways this command can be used
	 */
	public String[] getUsage()
	{
		return CommandInfoLoader.getData(getClass()).getUsageActual();
	}

	/**
	 * this is what is actually stored in the {@code usage} {@link String}[] <p></p> 
	 * it is much safer to call {@code getUsage()} so you know you are getting the write thing
	 * @see AbstractCommand#getUsage()
	 * @return the actual usage for this command
	 */
	public String[] getUsageActual()
	{
		return this.usage;
	}

	/**
	 * 
	 * @return the different names that this command goes by
	 */
	public String[] getAliases()
	{
		return this.aliases;
	}

	/**
	 * 
	 * @return the permission required to use this command
	 */
	public DisPerm getPermission()
	{
		return this.permission;
	}

	/**
	 * changes the aliases of this command
	 * @param aliases the new set of aliases
	 */
	public void setAliases(String[] aliases) 
	{
		this.aliases = aliases;
	}

	/**
	 * changes the name of this command
	 * @param command the new name of this command
	 */
	public void setCommand(String command) 
	{
		this.command = command;
	}

	/**
	 * changes the description of this command
	 * @param description the new description
	 */
	public void setDescription(String description) 
	{
		this.description = description;
	}

	/**
	 * changes the set of ways this command can be used
	 * @param usage the new set of ways this command can be used
	 */
	public void setUsage(String[] usage) 
	{
		this.usage = usage;
	}

	/**
	 * changes the permission required to use this command
	 * @param permission the new permission required to use this command
	 */
	public void setPermission(DisPerm permission) 
	{
		this.permission = permission;
	}
}
