package alloy.command.console;

import java.io.File;
import java.util.List;

import alloy.command.util.AbstractConsoleCommand;
import alloy.handler.EventHandler;
import alloy.input.console.ConsoleInputData;
import alloy.utility.discord.AlloyUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import utility.StringUtil;

public class UpdateCommand extends AbstractConsoleCommand {

    private int guildChanges;
    private int memberChanges;


    @Override
    public void execute(ConsoleInputData data) 
    {
        JDA jda = data.getJda();
        List<Guild> guilds = jda.getGuilds();
        for (Guild guild : guilds)
            updateGuild(guild);
        String[][] table = new String[2][2];
        table[0][0] = "Guild Changes";
        table[0][1] = "Member Changes";
        table[1][0] = "" + this.guildChanges;
        table[1][1] = "" + this.memberChanges;
        System.out.println(StringUtil.makeTable(table));
    }

    private void updateGuild(Guild guild) 
    {
        String path = AlloyUtil.getGuildPath(guild);
        if (!new File(path).exists())
        {
            EventHandler.onGuildJoinEvent(guild);
            guildChanges++;
        }
        List<Member> members = guild.getMembers();
        for (Member member : members) 
            updateMember(member);
    }

    private void updateMember(Member member) 
    {
        String path = AlloyUtil.getMemberPath(member);
        if (!new File(path).exists())
        {
            EventHandler.onMemberJoinEvent(member);
            memberChanges ++;
        }
    }
    
}
