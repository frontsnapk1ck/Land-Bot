package frontsnapk1ck.alloy.command.console;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import frontsnapk1ck.alloy.command.util.AbstractConsoleCommand;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.handler.util.EventHandler;
import frontsnapk1ck.alloy.input.console.ConsoleInputData;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.job.jobs.DelayJob;
import frontsnapk1ck.io.Saver;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import frontsnapk1ck.utility.StringUtil;

public class UpdateCommand extends AbstractConsoleCommand {

    private int guildChanges;
    private int memberChanges;


    @Override
    public void execute(ConsoleInputData data) 
    {
        List<String> args = data.getArgs();

        if (args.size() == 1 )
            updateFiles(data);
        else if (args.get(1).equalsIgnoreCase("socket"))
            updateSocket();
        else if (args.get(1).equalsIgnoreCase("link"))
            updateBanLink(data);
    }

    private void updateBanLink(ConsoleInputData data) 
    {
        JDA jda = data.getJda();
        List<Guild> guilds = jda.getGuilds();

        for (Guild guild : guilds) 
        {
            Server s = AlloyUtil.loadServer(guild);
            if (!s.getBanAppealLink().equalsIgnoreCase("none"))
            {
                s.setBanAppealLink("none");
                guildChanges ++;
            }
        }
        String[][] table = new String[2][2];
        table[0][0] = "Guild Changes";
        table[0][1] = "Member Changes";
        table[1][0] = "" + this.guildChanges;
        table[1][1] = "" + this.memberChanges;
        System.out.println(StringUtil.makeTable(table));
    }

    private void updateSocket() 
    {
        Alloy.LOGGER.getDisInterface().resetSocket();
	}

	private void updateFiles(ConsoleInputData data) 
    {
        Consumer<ConsoleInputData> con = new Consumer<ConsoleInputData>()
        {
            public void accept(ConsoleInputData data) 
            {
                updateFilesImp(data);
            };
        };
        DelayJob<ConsoleInputData> j = new DelayJob<ConsoleInputData>(con , data);
        data.getQueue().queue(j);
    }

    protected void updateFilesImp(ConsoleInputData data) 
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
        if (member.getUser().isBot())
        {
            if (Saver.deleteFile(path))
                memberChanges++;
            return;
        }
        if (!new File(path).exists())
        {
            EventHandler.onMemberJoinEvent(member);
            memberChanges ++;
        }
    }
    
}
