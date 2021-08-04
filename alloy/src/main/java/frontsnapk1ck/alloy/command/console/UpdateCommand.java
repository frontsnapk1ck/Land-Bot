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
import frontsnapk1ck.alloy.utility.job.jobs.AlloyDelayJob;
import frontsnapk1ck.io.Saver;
import frontsnapk1ck.utility.StringUtil;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

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
        AlloyDelayJob<ConsoleInputData> j = new AlloyDelayJob<ConsoleInputData>(con , data);
        data.getQueue().queue(j);
        data.getBot().guildCountUpdate();
        ((Alloy)data.getBot()).update();
    }

    protected void updateFilesImp(ConsoleInputData data) 
    {
        JDA jda = data.getJda();
        List<Guild> guilds = jda.getGuilds();
        final ProgressBar memberPB = new ProgressBarBuilder().setTaskName("Members")
                                                             .setUpdateIntervalMillis(1)
                                                             .build();

        final ProgressBar guildPB = new ProgressBarBuilder().setTaskName("Guilds")
                                                            .setUpdateIntervalMillis(1)
                                                            .setInitialMax(guilds.size())
                                                            .build();
        for (Guild guild : guilds)
        {
            guildPB.setExtraMessage(guild.getId());
            guildPB.step();
            updateGuild(guild,memberPB);
        }
        String[][] table = new String[2][2];
        table[0][0] = "Guild Changes";
        table[0][1] = "Member Changes";
        table[1][0] = "" + this.guildChanges;
        table[1][1] = "" + this.memberChanges;
        
        memberPB.close();
        guildPB.close();
        
        System.out.println(StringUtil.makeTable(table));
    }

    private void updateGuild(Guild guild, final ProgressBar pb) 
    {
        AlloyUtil.loadServer(guild).setLoaded(false);
        String path = AlloyUtil.getGuildPath(guild);
        if (!new File(path).exists())
        {
            EventHandler.onGuildJoinEvent(guild);
            
            guildChanges++;
        }
        List<Member> members = guild.getMembers();
        pb.maxHint(members.size());
        pb.stepTo(0);
        for (Member member : members) 
        {
            pb.setExtraMessage(member.getId());
            updateMember(member);
            pb.step();
        }
        AlloyUtil.loadServer(guild).setLoaded(true);
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
