package alloy.handler;

import alloy.utility.discord.AlloyUtil;
import io.FileReader;

public class FunChatHandler {

	public static String[] getDeadChats() 
	{
		String[] out = FileReader.read(AlloyUtil.CHATS_FILE);
		return out;
	}

	public static String[] getRankChats() 
	{
		String[] out = FileReader.read(AlloyUtil.RANKS_FILE);
		return out;
	}
    
}
