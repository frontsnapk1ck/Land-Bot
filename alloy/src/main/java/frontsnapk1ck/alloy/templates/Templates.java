package frontsnapk1ck.alloy.templates;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import com.github.connyscode.ctils.jTrack.Song;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import frontsnapk1ck.alloy.command.util.PunishType;
import frontsnapk1ck.alloy.event.DebugEvent;
import frontsnapk1ck.alloy.gameobjects.Case;
import frontsnapk1ck.alloy.gameobjects.RankUp;
import frontsnapk1ck.alloy.gameobjects.Warning;
import frontsnapk1ck.alloy.gameobjects.player.Building;
import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.alloy.handler.command.EconHandler;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.DisUtil;
import frontsnapk1ck.alloy.utility.discord.EmojiConstants;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.disterface.util.template.Template;
import frontsnapk1ck.io.FileReader;
import frontsnapk1ck.utility.StringUtil;
import frontsnapk1ck.utility.Util;
import frontsnapk1ck.utility.logger.Level;
import frontsnapk1ck.utility.time.TimeUtil;
import frontsnapk1ck.utility.time.TimesIncludes;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class Templates {

	public static final int MAX_ROLE_SHOW = 15;
	public static final int MAX_SERVER_ROLE_SHOW = 30;
	public static final int MAX_ROLE_MEMBERS_SHOW = 30;

	public static List<MessageEmbed> getEmbeds(AlloyTemplate t) 
    {
        List<MessageEmbed> embeds = new ArrayList<MessageEmbed>();
        try
		{
            embeds.add(t.getEmbed());
        }
		catch (Exception ex) 
        {
            String[] arr = t.getText().split("\n");
            String title = arr[0];
            String[] newLines = Util.arrRange(arr, 1);

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(title);

            String out = "";
            for (int i = 0; i < newLines.length; i++) 
            {
                String tmp = out + newLines[i] + "\n";
                if (tmp.length() > MessageEmbed.TEXT_MAX_LENGTH)
                {
                    eb.setDescription(out);
                    embeds.add(eb.build());
                    out = newLines[i];
                }
                else
                    out = tmp;
            }
            eb.setDescription(out);
            embeds.add(eb.build());
        }
        return embeds;
    }

	public static AlloyTemplate noPermission(DisPerm p, User u)
	{
		String s = "The user " + u.getAsMention() + " does not have the permission `" + p.getName() + "`";
		AlloyTemplate t = new AlloyTemplate("No Permissions", s);
		return t;
	}

	public static AlloyTemplate bankTransferMinimum()
	{
		String s = "you must transfer a minimum of $" + EconHandler.MINIUM_BALANCE;
		AlloyTemplate t = new AlloyTemplate("Minimum Bank Transfer", s);
		return t;
	}

	public static AlloyTemplate bankTransferSuccess(Player from, Player to, int amount, String message)
	{
		String s = from.getAsMention() + " has transferred `$" + amount + "` to " + to.getAsMention();
		if (!message.equalsIgnoreCase(""))
			s += "\n" + message;
		AlloyTemplate t = new AlloyTemplate("Bank Transfer Success", s);
		return t;
	}

	public static AlloyTemplate bankTransferFailed()
	{
		AlloyTemplate t = new AlloyTemplate("Bank Transfer Failed", "the transfer has failed");
		return t;
	}

	public static AlloyTemplate bankInsufficientFunds(User author, int amount)
	{
		String s = author.getAsMention() + ", you do not have enough funds to make that payment";
		AlloyTemplate t = new AlloyTemplate("Bank Transfer Failed", s);
		return t;
	}

	public static AlloyTemplate prefixIs( String prefix )
	{
		AlloyTemplate t = new AlloyTemplate("Prefix", "the prefix is currently\n`" + prefix + "`");
		return t;
	}

	public static AlloyTemplate modlogNotFound ()
	{
		AlloyTemplate t = new AlloyTemplate("Mod Log not found", "the mod log could not be found for this server");
		return t;

	}

	public static AlloyTemplate moderationActionEmpty(TextChannel chan, PunishType punishType)
	{
		AlloyTemplate t = new AlloyTemplate("Moderation Action Empty", "Moderation Action Empty\n" + punishType);
		return t;
	}

	public static AlloyTemplate cannotModerateSelf()
	{
		AlloyTemplate t = new AlloyTemplate("Cannot Moderate Self", "you cannot moderate me, i am too powerful");
		return t;
	}

	public static AlloyTemplate moderationActionFailed(PunishType punishType, String message)
	{
		AlloyTemplate t = new AlloyTemplate("Moderation Action Failed",
				"the moderation action `" + punishType + "` has failed with the error\n" + message);
		return t;
	}

	public static AlloyTemplate moderationActionSuccess( TextChannel chan, Member targetUser, String verb )
	{
		AlloyTemplate t = new AlloyTemplate("Moderation Action Success", "the moderation action `" + verb
				+ "` has succeeded, lol.\nused in " + chan.getAsMention() + " against " + targetUser.getAsMention());
		return t;
	}

	public static AlloyTemplate bankCurrentBalance(Player p)
	{
		AlloyTemplate t = new AlloyTemplate("Current Balance", p.getAsMention() + "'s balance is `$" + p.getBal() + "`");
		return t;

	}

	public static AlloyTemplate argumentsNotRecognized( Message msg )
	{
		AlloyTemplate t = new AlloyTemplate("Arguments not Recognized",
				"`" + msg.getContentRaw() + "`\nthat didn't make sense to me, lol");
		return t;
	}

	public static AlloyTemplate daySuccess(Guild guil
	)
	{
		AlloyTemplate t = new AlloyTemplate("Day Success", "the day has advanced in this server");
		return t;

	}

	public static AlloyTemplate moderationLog(TextChannel chan, Guild guild, User author, PunishType punishType, String[] args)
	{
		String out = "";
		for (String string : args)
			out += string + " ";
		AlloyTemplate t = new AlloyTemplate("Moderation Action Used", author.getAsMention() + " used " + punishType
				+ " in the channel " + chan.getAsMention() + "\n`" + out + "`");
		return t;

	}

	public static AlloyTemplate invalidNumberFormat(String[] args)
	{
		String out = "";
		for (String string : args)
			out += string + " ";

		AlloyTemplate t = new AlloyTemplate("Invalid Number format", " this is wrong, lol\n`" + out + "`");
		return t;

	}

	public static AlloyTemplate onlyPositiveNumbers(int amount)
	{
		AlloyTemplate t = new AlloyTemplate("Only Positive Numbers", "i dont think that `" + amount + "` is positive");
		return t;
	}

	public static AlloyTemplate spamRunnableCreated(Long id)
	{
		AlloyTemplate t = new AlloyTemplate("ID to stop this spam",
				"to stop this spam, use the command \n`!spam stop " + id + "`");
		return t;
	}

	public static AlloyTemplate invalidUse(MessageChannel channel)
	{
		AlloyTemplate t = new AlloyTemplate("Invalid Use",
				"you cant do that, thats illegal, almost like my creator spelling things correctly. we all know thats illegal");
		return t;

	}

	public static AlloyTemplate caseNotFound(String caseId)
	{
		AlloyTemplate t = new AlloyTemplate("case not found", "could not find the case `" + caseId + "`");
		return t;

	}

	public static AlloyTemplate caseReasonModified(String reason)
	{
		AlloyTemplate t = new AlloyTemplate("Case Reason Modified", "the reason was modified to\n\n" + reason + "");
		return t;
	}

	public static AlloyTemplate bulkDeleteSuccessful(TextChannel channel, int size)
	{
		AlloyTemplate t = new AlloyTemplate("Purge success", "deleted `" + size + "` messages in " + channel.getAsMention());
		return t;

	}

	public static AlloyTemplate privateMessageFailed(Member m)
	{
		AlloyTemplate t = new AlloyTemplate("Private Message Failed", "i could not PM the member " + m.getAsMention());
		return t;
	}

	public static AlloyTemplate getWarn(Warning w)
	{
		AlloyTemplate t = new AlloyTemplate("Warning",
				"reason: " + w.getReason() + "\nissuer: " + w.getIssuer() + "\nid:" + w.getId());
		return t;
	}

	public static AlloyTemplate warnSuccess(Member m, Warning w, User author)
	{
		AlloyTemplate t = new AlloyTemplate("Warn Success",
				"the member " + m.getAsMention() + "has been warned\n\nwith the reason: `" + w.getReason() + "`");
		t.setFooterName(author.getAsTag());
		t.setFooterURL(author.getAvatarUrl());
		return t;
	}

	public static AlloyTemplate warnings(String table)
	{
		AlloyTemplate t = new AlloyTemplate("Warnings", table);
		return t;
	}

	public static AlloyTemplate warningNotFound(String string)
	{
		AlloyTemplate t = new AlloyTemplate("Warning not found", "the warning with the id `" + string + "` could not be found");
		return t;
	}

	public static AlloyTemplate warningsRemovedSuccess(String string, Member warned)
	{
		AlloyTemplate t = new AlloyTemplate("Warning removed Successfully",
				"the warning has been removed from the user " + warned.getAsMention());
		return t;
	}

	public static AlloyTemplate invalidBuildingName(String name)
	{
		AlloyTemplate t = new AlloyTemplate("Invalid building name", "The name `" + name + "` is invalid");
		return t;

	}

	public static AlloyTemplate argumentsNotSupplied(String[] args, String[] usage)
	{
		String got = "";
		for (String s : args)
			got += s + " ";
		got = got.trim();

		String expected = "\n```txt\n";
		if (usage != null)
		{
			for (String s : usage)
				expected += s + "\n\n";
		}
		expected += "```";
		expected = expected.trim();

		AlloyTemplate t = new AlloyTemplate("Arguments not supplied", "expected:\t " + expected + "\ngot:\t\t```" + (got.equals("") ? "nothing" : got ) + "```");
		return t;
	}

	public static AlloyTemplate buildingsNameOutOfBounds(Building b)
	{
		AlloyTemplate t = new AlloyTemplate("Building Name out of bounds",
				"the name for that building is too long bro \\**hehe*\\*");
		return t;
	}

	public static AlloyTemplate buildingSaveSuccess(List<Building> buildings)
	{
		String names = "";
		String costs = "";
		String gener = "";

		for (Building b : buildings)
		{
			names += "" + b.getName() + "\n";
			costs += "" + b.getCost() + "\n";
			gener += "" + b.getGeneration() + "\n";

		}
		AlloyTemplate t = new AlloyTemplate("Building Save Success", "All available buildings");
		t.addFelid("Name", names, true);
		t.addFelid("Cost", costs, true);
		t.addFelid("Generation", gener, true);

		return t;
	}

	public static AlloyTemplate workOptionAddSuccess(String[] args)
	{
		String out = "";
		for (String string : args)
			out += string + " ";

		AlloyTemplate t = new AlloyTemplate("Work Option Add Success",
				"you've added the following to the work options in this server\n\n" + out);
		return t;
	}

	public static AlloyTemplate numberOutOfBounds(IndexOutOfBoundsException e)

	{
		AlloyTemplate t = new AlloyTemplate("Number out of bounds",
				"lol, that number threw an error, you're lucky i caught it\n\n" + e.getMessage());
		return t;
	}

	public static AlloyTemplate buildingsRemoveSuccess(Building b)
	{
		AlloyTemplate t = new AlloyTemplate("Building Remove Success", "removed the building " + b.getName());
		return t;
	}

	public static AlloyTemplate workRemoveSuccess(String s)
	{
		AlloyTemplate t = new AlloyTemplate("WOrk Remove Success", "removed the Work Option `" + s + "`");
		return t;
	}

	public static AlloyTemplate spamRunnableStopped(Long id)
	{
		AlloyTemplate t = new AlloyTemplate("Spam Stopped", "the spam with the id `" + id + "` has stopped");
		return t;
	}

	public static AlloyTemplate spamRunnableIdNotFound(Long id)
	{
		AlloyTemplate t = new AlloyTemplate("Spam ID not found", "could not find the id `" + id + "`");
		return t;
	}

	public static AlloyTemplate voiceJoinSuccess(VoiceChannel vc)
	{
		AlloyTemplate t = new AlloyTemplate("Voice Join Success", "joined channel " + vc.getName());
		return t;
	}

	public static AlloyTemplate voiceJoinFail(VoiceChannel vc)
	{
		AlloyTemplate t = new AlloyTemplate("Voice Join Fail", "could not join channel " + vc.getName());
		return t;
	}

	public static AlloyTemplate voiceMemberNotInChannel(Member m)
	{
		AlloyTemplate t = new AlloyTemplate("Not In Voice Channel", "you have to be in a voice channel to use that command");
		return t;
	}

	public static AlloyTemplate showXPCooldown(int cooldown)
	{
		AlloyTemplate t = new AlloyTemplate("XP cooldown Time",
				"the xp cooldown time in this guild is currently `" + cooldown + "`");
		return t;
	}

	public static AlloyTemplate showCooldown(int cooldown)
	{
		AlloyTemplate t = new AlloyTemplate("Cooldown Time", "the cooldown time in this guild is currently `" + cooldown + "`");
		return t;

	}

	public static AlloyTemplate invalidNumberFormat(String num)
	{
		AlloyTemplate t = new AlloyTemplate("Invalid Number Format", "`" + num + "` isn't a number now, is it?");
		return t;
	}

	public static AlloyTemplate viewStartingBalance(int startingBalance)
	{
		AlloyTemplate t = new AlloyTemplate("Starting Balance",
				"the starting ballance in this server is `$" + startingBalance + "`");
		return t;
	}

	public static AlloyTemplate workOptions(Guild g)
	{
		String path = AlloyUtil.getGuildPath(g);
		String[] wo = FileReader
				.read(path + AlloyUtil.SUB + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB + AlloyUtil.WORK_OPTIONS_FILE);
		String out = "";
		for (String string : wo)
			out += string + "\n";

		AlloyTemplate t = new AlloyTemplate("Work Options", "the work options are \n\n" + out);
		return t;
	}

	public static AlloyTemplate buildingsList(Guild g)
	{
		List<Building> buildings = AlloyUtil.loadBuildings(g);
		String names = "";
		String costs = "";
		String gener = "";

		for (Building b : buildings)
		{
			names += "" + b.getName() + "\n";
			costs += "" + b.getCost() + "\n";
			gener += "" + b.getGeneration() + "\n";

		}
		AlloyTemplate t = new AlloyTemplate("Building list", "All available buildings");
		t.addFelid("Name", names, true);
		t.addFelid("Cost", costs, true);
		t.addFelid("Generation", gener, true);

		return t;
	}

	public static AlloyTemplate voiceDisconnectSuccess(VoiceChannel vc)
	{
		AlloyTemplate t = new AlloyTemplate("Voice Disconnect Success", "I have left " + vc.getName());
		return t;
	}

	public static AlloyTemplate voiceDisconnectFail()
	{
		AlloyTemplate t = new AlloyTemplate("Voice Disconnect Fail", "I have not left the vc i am in");
		return t;
	}

	public static AlloyTemplate onCooldown(Member m)
	{
		AlloyTemplate t = new AlloyTemplate("Slow down there", "you are on cooldown " + m.getAsMention());
		return t;
	}

	public static AlloyTemplate sayAdmin(String out, Message msg)
	{
		AlloyTemplate t = new AlloyTemplate("Announcement", out);
		t.setFooterName(msg.getAuthor().getAsTag());
		t.setFooterURL(msg.getAuthor().getAvatarUrl());
		return t;

	}

	public static AlloyTemplate help(String out)
	{
		AlloyTemplate t = new AlloyTemplate("HELP", out);
		return t;
	}

	public static AlloyTemplate helpSentAlloyTemplate()
	{
		AlloyTemplate t = new AlloyTemplate("Help has been sent", "albeit in the form of a DM");
		return t;
	}

	public static AlloyTemplate infoSelf()
	{
		AlloyTemplate t = new AlloyTemplate("My name is alloy",
				"I am Alloy and i am a Discord Bot frontsnapk1ck has been working on for a little while now. if you have any question feel free to reach out to him and join the official Alloy Support Server here https://discord.gg/7UNxyXRxBh \n\nthanks!");
		t.setFooterName("frontsnapk1ck");
		t.setFooterURL("https://cdn.discordapp.com/avatars/312743142828933130/7c63b41c5ed601b3314c1dce0d0e0065.png");
		return t;

	}

	public static AlloyTemplate infoUser(Member m)
	{
		String perm = loadPermString(m);
		String nick = (m.getNickname() == null ? "No Nickname" : m.getNickname());
		String username = "```txt\n" + m.getUser().getAsTag() + "\n```";
		String nickname = "```txt\n" + nick + "\n```";
		String isBot = "```txt\n" + (m.getUser().isBot() ? "Yes" : "No") + "\n```";
		String userID = "```txt\n" + m.getId() + "\n```";

		TimesIncludes joinIncludes = new TimesIncludes();
		joinIncludes.addYear();
		joinIncludes.addMonth();
		joinIncludes.addDay();
		joinIncludes.limit(3);
		String joinOn = "```txt\n" + TimeUtil.getTimeAgo(m.getTimeJoined(), joinIncludes) + "\n```";

		TimesIncludes createdIncludes = new TimesIncludes();
		createdIncludes.addYear();
		createdIncludes.addMonth();
		createdIncludes.addDay();
		createdIncludes.limit(2);
		String createOn = "```txt\n" + TimeUtil.getTimeAgo(m.getUser().getTimeCreated(), createdIncludes) + "\n```";

		Field userF = new Field("Username", username, true);
		Field userIDF = new Field("User ID", userID, true);

		Field roles = loadRolesField(m);

		Field nickF = new Field("Nickname", nickname, true);
		Field botF = new Field("Is Bot", isBot, true);

		Field permF = new Field("Global Permissions", perm, false);

		Field joinF = new Field("Joined this server on (MM/DD/YYYY)", joinOn, false);
		Field createF = new Field("Account created on  (MM/DD/YYYY)", createOn, false);

		AlloyTemplate t = new AlloyTemplate("User Info", "");
		t.addFelid(userF);
		t.addFelid(userIDF);

		t.addFelid(roles);

		t.addFelid(nickF);
		t.addFelid(botF);

		t.addFelid(permF);

		t.addFelid(joinF);
		t.addFelid(createF);

		t.setImageURL(getURL(m));

		return t;
	}

	private static String getURL(Member m) 
	{
		String url = m.getUser().getAvatarUrl();
		if (url == null)
			url = m.getUser().getDefaultAvatarUrl();
		return url;
	}

	private static Field loadRolesField(Member m)
	{
		String roleOut = "```txt\n";
		List<Role> roles = m.getRoles();

		int i = 0;
		for (Role r : roles)
		{
			if (i < MAX_ROLE_SHOW)
			{
				roleOut += r.getName() + "\n";
				i++;
			}
		}
		roleOut += "```";

		String name = "Roles [" + i + "] (shows up to " + MAX_ROLE_SHOW + ")";

		Field f = new Field(name, roleOut, false);
		return f;
	}

	private static String loadPermString(Member m)
	{
		String permOut = "```txt\n";
		List<DisPerm> perms = DisPermUtil.parsePerms(m.getPermissions());
		for (DisPerm p : perms)
			permOut += "✅ " + p.getName() + "\n";
		permOut += "```";
		return permOut;
	}

	public static AlloyTemplate infoServer(Guild g)
	{
		String name = "```txt\n" + g.getName() + "\n```";
		String owner = "```txt\n" + g.getOwner().getUser().getAsTag() + "\n```";
		String id = "```txt\n" + g.getId() + "\n```";
		String region = "```txt\n" + g.getRegionRaw() + "\n```";
		String boost = "```txt\n" + g.getBoostTier() + "\n```";
		String boosts = "```txt\n" + g.getBoostCount() + "\n```";

		TimesIncludes createdIncludes = new TimesIncludes();
		createdIncludes.addYear();
		createdIncludes.addMonth();
		createdIncludes.addDay();
		String created = "```txt\n" + TimeUtil.getTimeAgo(g.getTimeCreated(), createdIncludes) + "\n```";

		Field nameF = new Field("Server name", name, true);
		Field ownerF = new Field("Server owner", owner, true);

		Field membersF = loadMembersFelid(g);

		Field idF = new Field("Server ID", id, true);
		Field regionF = new Field("Server Region", region, true);

		Field channels = loadChannelsFelid(g);

		Field emojis = loadEmojis(g);

		Field boostF = new Field("Server boost Level", boost, true);
		Field boostsF = new Field("Server boost amount", boosts, true);

		Field rolesF = loadGuildRoles(g);

		Field createdF = new Field("Server created on (MM/DD/YYYY)", created, false);

		AlloyTemplate t = new AlloyTemplate("Server Info", "");

		t.addFelid(nameF);
		t.addFelid(ownerF);

		t.addFelid(membersF);

		t.addFelid(idF);
		t.addFelid(regionF);

		t.addFelid(channels);

		t.addFelid(emojis);

		t.addFelid(boostF);
		t.addFelid(boostsF);

		t.addFelid(rolesF);

		t.addFelid(createdF);

		t.setImageURL(getURL(g));

		return t;
	}

	private static String getURL(Guild g)
	{
		String url = g.getIconUrl();
		if (url == null)
			url = AlloyUtil.DEFAULT_DISCORD_PHOTO;
		return url;
	}

	private static Field loadGuildRoles(Guild g)
	{
		List<Role> roles = g.getRoles();
		String roleOut = "```txt\n";

		int i = 0;
		for (Role r : roles)
		{
			boolean last = i == MAX_SERVER_ROLE_SHOW - 1 || i == roles.size() - 1;
			boolean stop = i >= MAX_SERVER_ROLE_SHOW || i >= roles.size();
			if (last)
				roleOut += r.getName();
			else if (!stop)
				roleOut += r.getName() + ", ";
			i++;
		}
		roleOut += "\n```";
		String name = "Server roles [" + roles.size() + "] (shows up to " + MAX_SERVER_ROLE_SHOW + ")";
		return new Field(name, roleOut, false);
	}

	private static Field loadEmojis(Guild g)
	{
		List<Emote> emojis = g.getEmotes();
		int animated = 0;
		int normal = 0;
		for (Emote e : emojis)
		{
			if (e.isAnimated())
				animated++;
			else
				normal++;
		}
		String name = "Server emojis [" + emojis.size() + "]";
		String value = "```txt\nNormal: " + normal + "\t|\tAnimated: " + animated + "\n```";
		return new Field(name, value, false);
	}

	private static Field loadChannelsFelid(Guild g)
	{
		List<GuildChannel> channels = g.getChannels();
		List<Category> catagories = g.getCategories();
		int text = 0;
		int voice = 0;
		int announcement = 0;
		for (GuildChannel c : channels)
		{
			if (c instanceof TextChannel)
			{
				text++;
				TextChannel chan = (TextChannel) c;
				if (chan.isNews())
					announcement++;
			} else if (c instanceof VoiceChannel)
				voice++;
		}
		String name = "Server categories and channels [" + channels.size() + "]";
		String value = "```txt\nCategories: " + catagories.size() + "\t|\tText: " + text + "\t|\tVoice: " + voice
				+ "\t|\tAnnouncement: " + announcement + "\n```";

		return new Field(name, value, false);
	}

	private static Field loadMembersFelid(Guild g)
	{
		List<Member> members = g.getMembers();
		int user = 0;
		int bot = 0;
		for (Member member : members)
		{
			if (member.getUser().isBot())
				bot++;
			else
				user++;
		}

		String name = "Server members [" + members.size() + "]";
		String value = "```txt\nMember: " + user + "\t|\tBots: " + bot + "\n```";
		return new Field(name, value, false);
	}

	public static AlloyTemplate roleNotFound(String role)
	{
		AlloyTemplate t = new AlloyTemplate("Role not found", " i couldn't find the role " + role);
		return t;
	}

	public static AlloyTemplate infoRole(Role r)
	{
		Guild g = r.getGuild();

		String name = "```txt\n" + r.getName() + "\n```";
		String id = "```txt\n" + r.getId() + "\n```";
		String perms = loadRolePerms(r);
		String memberC = "```txt\n" + g.getMembersWithRoles(r).size() + "\n```";
		String rolePos = "```txt\n" + g.getRoles().indexOf(r) + "\n```";
		String roleCol = "```txt\n" + getRoleColor(r) + "\n```";

		TimesIncludes createdIncludes = new TimesIncludes();
		createdIncludes.addYear();
		createdIncludes.addMonth();
		createdIncludes.addDay();
		createdIncludes.limit(2);
		String createdOn = "```txt\n" + TimeUtil.getTimeAgo(r.getTimeCreated(), createdIncludes) + "\n```";

		Field nameF = new Field("Role name", name, true);
		Field idF = new Field("Role ID", id, true);

		Field permsF = new Field("Permissions the role has", perms, false);

		Field memberCF = new Field("Member Count", memberC, true);
		Field rolePosF = new Field("Role position", rolePos, true);
		Field colorF = new Field("Role color", roleCol, true);

		Field members = loadRoleMembers(r);

		Field createdF = new Field("Role created on (MM/DD/YYYY)", createdOn, false);

		AlloyTemplate t = new AlloyTemplate("Role Info", "");

		t.addFelid(nameF);
		t.addFelid(idF);

		t.addFelid(permsF);

		t.addFelid(memberCF);
		t.addFelid(rolePosF);
		t.addFelid(colorF);

		t.addFelid(members);

		t.addFelid(createdF);

		return t;
	}

	private static String getRoleColor(Role role)
	{
		Color c = role.getColor();
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		String hex = String.format("#%02x%02x%02x", r, g, b);
		return hex;
	}

	private static Field loadRoleMembers(Role r)
	{
		Guild g = r.getGuild();
		List<Member> members = g.getMembersWithRoles(r);
		String out = "```txt\n";

		int i = 0;
		for (Member member : members)
		{
			boolean stop = i >= MAX_ROLE_MEMBERS_SHOW || i >= members.size();
			boolean last = i == MAX_ROLE_MEMBERS_SHOW - 1 || i == members.size() - 1;

			if (last)
				out += member.getEffectiveName();
			else if (!stop)
				out += member.getEffectiveName() + ", ";
		}
		out += "\n```";
		String name = "Member with Role [" + members.size() + "] (shows up to " + MAX_ROLE_MEMBERS_SHOW + ")";
		return new Field(name, out, false);
	}

	private static String loadRolePerms(Role r)
	{
		List<DisPerm> perms = DisPermUtil.parsePerms(r.getPermissions());
		String permOut = "```txt\n";
		for (DisPerm p : perms)
			permOut += "✅ " + p.getName() + "\n";
		permOut += "```";
		return permOut;
	}

	public static AlloyTemplate invite()
	{
		AlloyTemplate t = new AlloyTemplate("Invite", "Thanks for thinking of me");
		String rickroll = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
		t.setTitle("Click here to invite me", rickroll);
		return t;
	}

	public static AlloyTemplate inviteActual(Member m)
	{
		String inviteLink = "https://discord.com/api/oauth2/authorize?client_id=762825892006854676&permissions=435514430&scope=bot";
		AlloyTemplate t = new AlloyTemplate("This is your actual invite",
				"my creator will never miss an opportunity to rick roll someone\n\nclick [here](" + inviteLink + ")");
		return t;

	}

	public static AlloyTemplate uptime(String relativeTime)
	{
		String date = TimeUtil.date();
		String time = TimeUtil.time();
		AlloyTemplate t = new AlloyTemplate("Uptime",
				"i have been up for `" + relativeTime + "`\nthe time on my host is `" + date + "`\t`" + time + "`");
		return t;
	}

	public static AlloyTemplate noBlacklistedChannels()
	{
		AlloyTemplate t = new AlloyTemplate("No blacklisted Channels", "this guild doesn't have any blacklisted channels ");
		return t;
	}

	public static AlloyTemplate listBlackListedChannels(List<Long> blacklisted)
	{
		String out = "";
		for (Long id : blacklisted)
			out += "<#" + id + ">\n";

		AlloyTemplate t = new AlloyTemplate("Black Listed Channels for XP ", out);
		return t;
	}

	public static AlloyTemplate invalidChannel(String c)
	{
		AlloyTemplate t = new AlloyTemplate("Invalid Channel", c);
		return t;
	}

	public static AlloyTemplate channelIsNotBlacklisted(String c)
	{
		AlloyTemplate t = new AlloyTemplate("Channel is not blacklisted", "the channel " + c + " is not blacklisted");
		return t;
	}

	public static AlloyTemplate blackListRemoveSuccess(String c)
	{
		AlloyTemplate t = new AlloyTemplate("Blacklist Removed Success",
				"removed channel " + c + " from the blacklisted channels");
		return t;
	}

	public static AlloyTemplate blackListAddSuccess(String c)
	{
		AlloyTemplate t = new AlloyTemplate("Blacklist Add Success", "added channel " + c + " to the blacklisted channels");
		return t;
	}

	public static AlloyTemplate leaderboard(List<String> lb)
	{
		String out = "";
		for (String string : lb)
			out += string + "\n";

		AlloyTemplate t = new AlloyTemplate("leaderboard", out);
		return t;
	}

	public static AlloyTemplate userNotFound(String user)
	{
		AlloyTemplate t = new AlloyTemplate("User not found", "i could not find the user " + user);
		return t;
	}

	public static AlloyTemplate rank(Member target, int level, String progress)
	{
		AlloyTemplate t = new AlloyTemplate("Rank", target.getAsMention() + "\nlevel: `" + level + "`\nxp: `" + progress + "`");
		return t;
	}

	public static AlloyTemplate invalidRole(String string)
	{
		AlloyTemplate t = new AlloyTemplate("invalid role", "i did not recognize the role " + string);
		return t;
	}

	public static AlloyTemplate duplicateRankup(int level)
	{
		AlloyTemplate t = new AlloyTemplate("duplicate level detected",
				"you may have tried to make a level get announced twice - level" + level);
		return t;
	}

	public static AlloyTemplate levelNotFound(String string)
	{
		AlloyTemplate t = new AlloyTemplate("Level not found", string);
		return t;
	}

	public static AlloyTemplate rankupAddSuccess(RankUp ru)
	{
		AlloyTemplate t = new AlloyTemplate("rank up add Success", "added a rankup messaged to level " + ru.getLevel());
		return t;
	}

	public static AlloyTemplate rankupRemoveSuccess(RankUp toRm)
	{
		AlloyTemplate t = new AlloyTemplate("rankup remove Success", "removed a rankup messaged from level " + toRm.getLevel());
		return t;
	}

	public static AlloyTemplate viewRankUps(List<RankUp> rus)
	{
		String out = "";
		for (RankUp rankUp : rus)
			out += "level `" + rankUp.getLevel() + "`\n";
		AlloyTemplate t = new AlloyTemplate("Levels with Rankups", out);
		return t;
	}

	public static AlloyTemplate xpSetSuccess(Member target, int xp)
	{
		AlloyTemplate t = new AlloyTemplate("XP set Success", "set " + target.getAsMention() + "'s xp to `" + xp + "`");
		return t;
	}

	public static AlloyTemplate cannotModerateModerators()
	{
		AlloyTemplate t = new AlloyTemplate("Cannot Moderate Moderators",
				"sorry, i cant moderate the moderators. if you need to punish them remove their perms first");
		return t;
	}

	public static AlloyTemplate showBuildings(User author, Map<Building, Integer> owned)
	{
		Set<Building> bs = owned.keySet();
		String[][] data = new String[3][bs.size()];

		int i = 0;
		for (Building building : bs)
		{
			int num = owned.get(building);
			String name = building.getName();
			int gener = building.getGeneration();

			data[0][i] = "" + name + "\n";
			data[1][i] = "" + gener + "\n";
			data[2][i] = "" + num + "\n";

			i++;
		}

		AlloyTemplate t = new AlloyTemplate(author.getAsTag(), "the buildings you own");
		t.addFelid("Name", StringUtil.joinStrings(data[0]), true);
		t.addFelid("Generation", StringUtil.joinStrings(data[1]), true);
		t.addFelid("Quantity", StringUtil.joinStrings(data[2]), true);
		return t;
	}

	public static AlloyTemplate buildingNameNotRecognized(String args)
	{
		AlloyTemplate t = new AlloyTemplate("Building Name Not recognized",
				"i couldn't find a building with the name " + args + " in this guild\nmake sure to check spelling");
		return t;
	}

	public static AlloyTemplate buildingBuySuccess(Building toBuy, User author)
	{
		AlloyTemplate t = new AlloyTemplate("Building Bought", "you have bought the building `" + toBuy.getName() + "` "
				+ author.getAsMention() + " for the price of `$" + toBuy.getCost() + "`");
		return t;
	}

	public static AlloyTemplate workSuccess(String option, int amt)
	{
		AlloyTemplate t = new AlloyTemplate("Work", option + "\nyou made $" + amt);
		return t;
	}

	public static AlloyTemplate spamChannelChanged(TextChannel target)
	{
		AlloyTemplate t = new AlloyTemplate("Spam Channel Changed",
				"the spam channel has been changed to " + target.getAsMention());
		return t;
	}

	public static AlloyTemplate channelIsAlreadyBlacklisted(TextChannel channel)
	{
		AlloyTemplate t = new AlloyTemplate("Channel Already Blacklisted",
				"this channel " + channel.getAsMention() + " is already blacklisted");
		return t;
	}

	public static AlloyTemplate remindMe(String out)
	{
		AlloyTemplate t = new AlloyTemplate("Reminder", "you have a reminder with the message:\n" + out);
		return t;
	}

	public static AlloyTemplate timeNotRecognized(String time)
	{
		AlloyTemplate t = new AlloyTemplate("Time not recognized", "i didn't recognize the time `" + time + "`");
		return t;
	}

	public static AlloyTemplate remindCard(String time, String out)
	{
		AlloyTemplate t = new AlloyTemplate("Reminder", "I will remind you in `" + time + "` with the message:\n" + out);
		return t;
	}

	public static AlloyTemplate remindMeDM(String out, Message msg)
	{
		String link = DisUtil.getLink(msg);
		AlloyTemplate t = new AlloyTemplate("Reminder",
				"you have a reminder with the message:\n" + out + "\n\nlink [here](" + link + ")");
		return t;
	}

	public static AlloyTemplate linkEmbed(String link, String text)
	{
		AlloyTemplate t = new AlloyTemplate ("link" , "[" + text + "](" + link + ")" );
		return t;
	}

	public static AlloyTemplate donate(User author)
	{
		String link = "http://paypal.me/frontsnapk1ckmedia";
		AlloyTemplate t = new AlloyTemplate("Donations"  , "thank you so much for thinking of me " + author.getAsMention() + "\n\nyou can donate to me [here](" + link + ")" );
		return t;
	}

	public static Template debug(DebugEvent e)
	{
		if (e.getLevel() == Level.ERROR)
			return error(e);
		if (e.getLevel() == Level.INFO)
			return info(e);
		if (e.getLevel() == Level.WARN)
			return warn(e);
		if (e.getLevel() == Level.DEBUG)
			return debugInfo(e);
		return new AlloyTemplate("Something Happened" , "You should take a look, ig");
	}

	private static Template debugInfo(DebugEvent e) 
	{
		Template t = new Template(e.getLevel().toString(), e.getMessage());
		return t;
	}

	private static Template warn(DebugEvent e) 
	{
		Template t = new Template(e.getLevel().toString(),  e.getMessage() );
		return t;
	}

	private static Template info(DebugEvent e) 
	{
		Template t = new Template(e.getLevel().toString(),  e.getMessage() );
		return t;
	}

	private static Template error(DebugEvent e) 
	{
		String trace = "";
		StackTraceElement[] stack = e.getError().getStackTrace();

		for (StackTraceElement s : stack) 
			trace += s.toString() + "\n";

		String err = e.getError().getClass().getSimpleName() + "\t:\t" + e.getError().getMessage() ;

		Template t = new Template(e.getLevel().toString(),  err + "\n\nstackTrace:\n" + trace );
		return t;
	}

	public static AlloyTemplate banAppeal(String link)
	{
		AlloyTemplate t = new AlloyTemplate("Ban Appeal" , "[click here to appeal your ban](" + link +  ")");
		return t;
	}

	public static AlloyTemplate invalidURL(String string)
	{
		AlloyTemplate t = new AlloyTemplate("Invalid URL", "the URL `" + string + "` is invalid" );
		return t;
	}

	public static AlloyTemplate channelNotFound(String channelT)
	{
		AlloyTemplate t = new AlloyTemplate ("Channel Not Found" , " i couldn't find the channel " + channelT );
		return t;
	}

	public static AlloyTemplate adminBypassCooldown(boolean b)
	{
		AlloyTemplate t = new AlloyTemplate("Admin Bypass Cooldown" , "the admin bypass cooldown had been change to `" + (b?"on`":"off`"));
		return t;
	}

	public static AlloyTemplate modLogChanged(String channelT)
	{
		return new AlloyTemplate ("Mod Log Changed" , "the mod log has been changed to " + channelT);
	}

	public static AlloyTemplate muteRoleChanged(String role)
	{
		return new AlloyTemplate ("Mute Role Changed" , "the mute role has been changed to " + role);
	}

	public static AlloyTemplate banAppealChanged(String string)
	{
		return new AlloyTemplate ("Ban Appeal Link Changed" , "the Ban Appeal Link has been changed to " + string);
	}

	public static AlloyTemplate logBan(Guild guild, User user)
	{
		AlloyTemplate t = new AlloyTemplate("USER BANNED", "the user " + user.getAsMention() + "has been banned from this server");
		t.setImageURL(user.getAvatarUrl());
		return t;
	}

	public static AlloyTemplate kicked(User u)
	{
		return new AlloyTemplate("Member Kicked", "the user " + u.getAsTag() + " has been kicked from this server");
	}

	public static AlloyTemplate banned(User u)
	{
		return new AlloyTemplate("Member Banned", "the user " + u.getAsTag() + " has been Banned from this server");
	}

	public static AlloyTemplate muted(Member member)
	{
		return new AlloyTemplate("Member Muted", "the user " + member.getUser().getAsTag() + " has been muted");
	}

	public static AlloyTemplate unMuted(Member member)
	{
		return new AlloyTemplate("Member Unmuted", "the user " + member.getUser().getAsTag() + " has been unmuted");
	}

	public static AlloyTemplate showHelpMessage()
	{
		String commands = "https://sites.google.com/view/alloybot/docs/commands";
		String faq = "https://sites.google.com/view/alloybot/docs/frequently-asked-questions";
		String discordInv = "https://discord.com/invite/7UNxyXRxBh";
		String alloyURL = "https://sites.google.com/view/alloybot/home";

		String body = 	":green_circle: [click here](" + commands + ") for a list of commands\n\n" + 
						":red_circle: [click here](" + faq + ") to see our FAQs\n\n" + 
						":white_circle: [click here](" + discordInv + ") to get support in the Alloy Support Server";

		AlloyTemplate t = new AlloyTemplate("Alloy Help", body);
		t.setTitle("Alloy Help", alloyURL);
		return t;
	}

    public static AlloyTemplate musicQueue(BlockingQueue<AudioTrack> queue, AudioTrack nowPlaying)
	{
		String out = "";
		int i = 1;
		for (AudioTrack t : queue) 
		{
			String title = t.getInfo().title;
			String trackInfo = "" + i + ".   [" + title + "]("  + t.getInfo().uri + ")";
			trackInfo += "\nDuration: `";
			trackInfo += TimeUtil.getTimeShortFromRelative(t.getDuration()) + "`";
			out += trackInfo + "\n\n";
			i++;
		}
		if (out.equalsIgnoreCase(""))
			out = "Nothing in the queue";
		
		if (nowPlaying == null)
		{
			AlloyTemplate t = new AlloyTemplate("Music Queue", out);
			return t;
		}

		String title = nowPlaying.getInfo().title;
		String trackInfo = "[" + title + "]("  + nowPlaying.getInfo().uri + ")";
		trackInfo += "\nDuration: `";
		trackInfo += TimeUtil.getTimeShortFromRelative(nowPlaying.getDuration()) + "`";
		out = "Now Playing:\n" + trackInfo + "\n\nQueue:\n" + out;

		AlloyTemplate t = new AlloyTemplate("Music Queue", out);
		return t;
    }

    public static AlloyTemplate notingFoundBy(String[] args)
	{
		String search = StringUtil.joinStrings(args);
		AlloyTemplate t = new AlloyTemplate("Nothing Found", "I could not find anything by the name: `" + search + "`");
        return t;
    }

    public static AlloyTemplate couldNotPlay(FriendlyException exception)
	{
		AlloyTemplate t = new AlloyTemplate("Could Not Play", exception.getMessage());
		return t;
    }

    public static AlloyTemplate addedToMusicQueue(AudioPlaylist playlist)
	{
		AudioTrack firstTrack = playlist.getSelectedTrack();
        
		if (firstTrack == null)
			firstTrack = playlist.getTracks().get(0);
		String message = "Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")";
		AlloyTemplate t = new AlloyTemplate ("Queue" , message);
		return t;
    }

	public static AlloyTemplate addedToMusicQueue(AudioTrack track)
	{
		AlloyTemplate t = new AlloyTemplate("Queue" , "Adding to queue " + track.getInfo().title);
		return t;
	}

    public static AlloyTemplate nowPlaying(AudioTrack nowPlaying)
	{
		if (nowPlaying == null)
			return new AlloyTemplate("Now Playing" , "Nothing is playing right now");
		
		String title = nowPlaying.getInfo().title;

		String pos = TimeUtil.getTimeShortFromRelative(nowPlaying.getPosition());
		String total = TimeUtil.getTimeShortFromRelative(nowPlaying.getDuration());

		String link = nowPlaying.getInfo().uri;
		if (link.contains("youtu"));
			link += "?t=" + nowPlaying.getPosition() / 1000 + "s";
		String trackInfo = "[" + title + "]("  + link + ")\n";
		trackInfo += String.format(
			"Duration: `%s`/`%s`" ,
			pos ,
			total 
		);

		AlloyTemplate t = new AlloyTemplate("Now Playing", trackInfo);
		return t;
    }

    public static AlloyTemplate musicSkipped()
	{
		AlloyTemplate t = new AlloyTemplate("Skipped", "The song has been skipped");
		return t;
    }

	public static AlloyTemplate botStillLoading()
	{
		return new AlloyTemplate("Still Loading", "please wait a moment before using any commands, the bot is still loading");
	}

	public static AlloyTemplate caseList(List<Case> cases)
	{
		String title = "Cases - " + cases.size();
		String out = "";

		for (Case c : cases) 
		{	
			String tmp = "Case `" + c.getNum() + "` " + c.getPunishType().getKeyword();
			tmp += "\n" + c.getReason() + "\nissued by: <@!" + c.getIssuer() + ">";

			out += tmp + "\n\n";
		}
		out = out.trim();
		return new AlloyTemplate(title, out);
	}

	public static AlloyTemplate musicForceSkipped()
	{
		return new AlloyTemplate("Skipped" , "The song has been force skipped");
	}

    public static AlloyTemplate alreadyVotedSkip()
	{
		return new AlloyTemplate("Already Voted" , "You have already voted to skip this song");
    }

    public static AlloyTemplate notInVoiceChannel()
	{
		return new AlloyTemplate("Not In Voice Channel", "To use that command, i must be in a " +  EmojiConstants.VOICE + " Voice Channel" );
    }

    public static AlloyTemplate voteSkip(int voteNum, int majority)
	{
		return new AlloyTemplate("Vote Skipped", "You are the `" + voteNum + "` of the `" + majority + "` needed to skip this song");
    }

    public static AlloyTemplate noMusicPlaying()
	{
		return new AlloyTemplate("No Music Playing", "To use that command, music must be playing");
    }

    public static AlloyTemplate noLyricsFound(String name)
	{
        return new AlloyTemplate("No Lyrics Found", String.format("I couldn't find anything for the song: `%s`", name));
    }

    public static AlloyTemplate songLyrics(Song song)
	{
        String name = song.songFullName();
		String lyrics = song.songLyrics();

		AlloyTemplate t = new AlloyTemplate(name , lyrics);
		return t;
    }

	public static AlloyTemplate addedSongToTop(AudioPlaylist playlist)
	{
		AudioTrack firstTrack = playlist.getSelectedTrack();
        
		if (firstTrack == null)
			firstTrack = playlist.getTracks().get(0);
		String message = "Adding to top of queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")";
		AlloyTemplate t = new AlloyTemplate ("Queue" , message);
		return t;
	}

    public static AlloyTemplate addedSongToTop(AudioTrack track)
	{
        AlloyTemplate t = new AlloyTemplate("Queue" , "Adding to top of queue " + track.getInfo().title);
		return t;
    }

    public static AlloyTemplate playingSongNow(AudioTrack track)
	{
		AlloyTemplate t = new AlloyTemplate("Queue" , "Playing now: " + track.getInfo().title);
		return t;
    }

    public static AlloyTemplate playingSongNow(AudioPlaylist playlist)
	{
		AudioTrack firstTrack = playlist.getSelectedTrack();
        
		if (firstTrack == null)
			firstTrack = playlist.getTracks().get(0);
		String message = "Playing now: " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")";
		AlloyTemplate t = new AlloyTemplate ("Queue" , message);
		return t;
    }

	public static AlloyTemplate songRemove(AudioTrack removed)
	{
		return new AlloyTemplate("Removed Song", "removed song: " + removed.getInfo().title);
	}

	public static AlloyTemplate songNotFound(String song)
	{
		return new AlloyTemplate
		( 
			"Nothing Found", 
			String.format(
				"I couldn't find anything by the name: `%s` on %s YouTube" ,
				song , 
				EmojiConstants.YOUTUBE
			)
		);
	}

	public static AlloyTemplate assignRolesOnBuy(boolean b)
	{
		AlloyTemplate t = new AlloyTemplate("Role Assign on Buy" , "the role assign on buy had been change to `" + (b?"on`":"off`"));
		return t;
	}

	public static AlloyTemplate caseEditFailed()
	{
		return new AlloyTemplate("Case Edit Failed" , "the edit you requested failed");
	}

}