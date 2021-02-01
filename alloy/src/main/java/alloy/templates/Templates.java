package alloy.templates;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Set;

import alloy.command.util.PunishType;
import alloy.event.DebugEvent;
import alloy.gameobjects.RankUp;
import alloy.gameobjects.Warning;
import alloy.gameobjects.player.Building;
import alloy.gameobjects.player.Player;
import alloy.handler.BankHandler;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import alloy.utility.job.jobs.SpamRunnable;
import io.FileReader;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import utility.StringUtil;
import utility.logger.Level;
import utility.time.TimeUtil;
import utility.time.TimesIncludes;

public class Templates {

	public static final int MAX_ROLE_SHOW = 15;
	public static final int MAX_SERVER_ROLE_SHOW = 30;
	public static final int MAX_ROLE_MEMBERS_SHOW = 30;

	public static Template noPermission(DisPerm p, User u) {
		String s = "The user " + u.getAsMention() + " does not have the permission `" + p.getName() + "`";
		Template t = new Template("No Permissions", s);
		return t;
	}

	public static Template bankTransferMinimum() {
		String s = "you must transfer a minimum of $" + BankHandler.MINIUM_BALANCE;
		Template t = new Template("Minimum Bank Transfer", s);
		return t;
	}

	public static Template bankTransferSuccess(Player from, Player to, int amount, String message) {
		String s = from.getAsMention() + " has transferred `$" + amount + "` to " + to.getAsMention();
		if (!message.equalsIgnoreCase(""))
			s += "\n" + message;
		Template t = new Template("Bank Transfer Success", s);
		return t;
	}

	public static Template bankTransferFailed() {
		Template t = new Template("Bank Transfer Failed", "the transfer has failed");
		return t;
	}

	public static Template bankInsufficientFunds(User author, int amount) {
		String s = author.getAsMention() + ", you do not have enough funds to make that payment";
		Template t = new Template("Bank Transfer Failed", s);
		return t;
	}

	public static Template prefixIs(String prefix) {
		Template t = new Template("Prefix", "the prefix is currently\n`" + prefix + "`");
		return t;
	}

	public static Template modlogNotFound() {
		Template t = new Template("Mod Log not found", "the mod log could not be found for this server");
		return t;

	}

	public static Template moderationActionEmpty(TextChannel chan, PunishType punishType) {
		Template t = new Template("Moderation Action Empty", "Moderation Action Empty\n" + punishType);
		return t;
	}

	public static Template cannotModerateSelf() {
		Template t = new Template("Cannot Moderate Self", "you cannot moderate me, i am too powerful");
		return t;
	}

	public static Template moderationActionFailed(PunishType punishType) {
		Template t = new Template("Moderation Action Failed",
				"the moderation action `" + punishType + "` has failed, lol");
		return t;
	}

	public static Template moderationActionSuccess(TextChannel chan, Member targetUser, String verb) {
		Template t = new Template("Moderation Action Success", "the moderation action `" + verb
				+ "` has succeeded, lol.\nused in " + chan.getAsMention() + " against " + targetUser.getAsMention());
		return t;
	}

	public static Template bankCurrentBalance(Player p) {
		Template t = new Template("Current Balance", p.getAsMention() + "'s balance is `$" + p.getBal() + "`");
		return t;

	}

	public static Template argumentsNotRecognized(Message msg) {
		Template t = new Template("Arguments not Recognized",
				"`" + msg.getContentRaw() + "`\nthat didn't make sense to me, lol");
		return t;
	}

	public static Template daySuccess(Guild guild) {
		Template t = new Template("Day Success", "the day has advanced in this server");
		return t;

	}

	public static Template moderationLog(TextChannel chan, Guild guild, User author, PunishType punishType,
			String[] args) {
		String out = "";
		for (String string : args)
			out += string + " ";
		Template t = new Template("Moderation Action Used", author.getAsMention() + " used " + punishType
				+ " in the channel " + chan.getAsMention() + "\n`" + out + "`");
		return t;

	}

	public static Template invalidNumberFormat(String[] args) {
		String out = "";
		for (String string : args)
			out += string + " ";

		Template t = new Template("Invalid Number format", " this is wrong, lol\n`" + out + "`");
		return t;

	}

	public static Template onlyPositiveNumbers(int amount) {
		Template t = new Template("Only Positive Numbers", "i dont think that `" + amount + "` is positive");
		return t;
	}

	public static Template spamRunnableCreated(SpamRunnable r) {
		Template t = new Template("ID to stop this spam",
				"to stop this spam, use the command \n`!spam stop " + r.getID() + "`");
		return t;
	}

	public static Template invalidUse(MessageChannel channel) {
		Template t = new Template("Invalid Use",
				"you cant do that, thats illegal, almost like my creator spelling things correctly. we all know thats illegal");
		return t;

	}

	public static Template caseNotFound(String caseId) {
		Template t = new Template("case not found", "could not find the case `" + caseId + "`");
		return t;

	}

	public static Template caseReasonModified(String reason) {
		Template t = new Template("Case Reason Modified", "the reason was modified to\n\n" + reason + "");
		return t;
	}

	public static Template bulkDeleteSuccessful(TextChannel channel, int size) {
		Template t = new Template("Purge success", "deleted `" + size + "` messages in " + channel.getAsMention());
		return t;

	}

	public static Template privateMessageFailed(Member m) {
		Template t = new Template("Private Message Failed", "i could not PM the member " + m.getAsMention());
		return t;
	}

	public static Template getWarn(Warning w) {
		Template t = new Template("Warning",
				"reason: " + w.getReason() + "\nissuer: " + w.getIssuer() + "\nid:" + w.getId());
		return t;
	}

	public static Template warnSuccess(Member m, Warning w, User author) {
		Template t = new Template("Warn Success",
				"the member " + m.getAsMention() + "has been warned\n\nwith the reason: `" + w.getReason() + "`");
		t.setFooterName(author.getAsTag());
		t.setFooterURL(author.getAvatarUrl());
		return t;
	}

	public static Template warnings(String table) {
		Template t = new Template("Warnings", table);
		return t;
	}

	public static Template warningNotFound(String string) {
		Template t = new Template("Warning not found", "the warning with the id `" + string + "` could not be found");
		return t;
	}

	public static Template warningsRemovedSuccess(String string, Member warned) {
		Template t = new Template("Warning removed Successfully",
				"the warning has been removed from the user " + warned.getAsMention());
		return t;
	}

	public static Template invalidBuildingName(String name) {
		Template t = new Template("Invalid building name", "The name `" + name + "` is invalid");
		return t;

	}

	public static Template argumentsNotSupplied(String[] args, String[] usage) {
		String got = "";
		for (String s : args)
			got += s + " ";
		got = got.trim();

		String expected = "";
		if (usage != null) {
			for (String s : usage)
				expected += s + "";
		}
		expected = expected.trim();

		Template t = new Template("Arguments not supplied", "expected:\t " + expected + "\ngot:\t\t`" + (got.equals("") ? "nothing" : got ) + "`");
		return t;
	}

	public static Template buildingsNameOutOfBounds(Building b) {
		Template t = new Template("Building Name out of bounds",
				"the name for that building is too long bro \\**hehe*\\*");
		return t;
	}

	public static Template buildingSaveSuccess(List<Building> buildings) {
		String names = "";
		String costs = "";
		String gener = "";

		for (Building b : buildings) {
			names += "" + b.getName() + "\n";
			costs += "" + b.getCost() + "\n";
			gener += "" + b.getGeneration() + "\n";

		}
		Template t = new Template("Building Save Success", "All available buildings");
		t.addFeild("Name", names, true);
		t.addFeild("Cost", costs, true);
		t.addFeild("Generation", gener, true);

		return t;
	}

	public static Template workOptionAddSuccess(String[] args) {
		String out = "";
		for (String string : args)
			out += string + " ";

		Template t = new Template("Work Option Add Success",
				"you've added the following to the work options in this server\n\n" + out);
		return t;
	}

	public static Template numberOutOfBounds(IndexOutOfBoundsException e) {
		Template t = new Template("Number out of bounds",
				"lol, that number threw an error, you're lucky i caught it\n\n" + e.getMessage());
		return t;
	}

	public static Template buildingsRemoveSuccess(Building b) {
		Template t = new Template("Building Remove Success", "removed the building " + b.getName());
		return t;
	}

	public static Template workRemoveSuccess(String s) {
		Template t = new Template("WOrk Remove Success", "removed the Work Option `" + s + "`");
		return t;
	}

	public static Template spamRunnableStopped(Long id) {
		Template t = new Template("Spam Stopped", "the spam with the id `" + id + "` has stopped");
		return t;
	}

	public static Template spamRunnableIdNotFound(Long id) {
		Template t = new Template("Spam ID not found", "could not find the id `" + id + "`");
		return t;
	}

	public static Template voiceJoinSuccess(VoiceChannel vc) {
		Template t = new Template("Voice Join Success", "joined channel " + vc.getName());
		return t;
	}

	public static Template voiceJoinFail(VoiceChannel vc) {
		Template t = new Template("Voice Join Fail", "could not join channel " + vc.getName());
		return t;
	}

	public static Template voiceMemberNotInChannel(Member m) {
		Template t = new Template("Voice Join Fail", "you have to be in a voice channel to use that command");
		return t;
	}

	public static Template showXPCooldown(int cooldown) {
		Template t = new Template("XP cooldown Time",
				"the xp cooldown time in this guild is currently `" + cooldown + "`");
		return t;
	}

	public static Template showCooldown(int cooldown) {
		Template t = new Template("Cooldown Time", "the cooldown time in this guild is currently `" + cooldown + "`");
		return t;

	}

	public static Template invalidNumberFormat(String num) {
		Template t = new Template("Invalid Number Format", "`" + num + "` isn't a number now, is it?");
		return t;
	}

	public static Template viewStartingBalance(int startingBalance) {
		Template t = new Template("Starting Balance",
				"the starting ballance in this server is `$" + startingBalance + "`");
		return t;
	}

	public static Template workOptions(Guild g) {
		String path = AlloyUtil.getGuildPath(g);
		String[] wo = FileReader
				.read(path + AlloyUtil.SUB + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB + AlloyUtil.WORK_OPTIONS_FILE);
		String out = "";
		for (String string : wo)
			out += string + "\n";

		Template t = new Template("Work Options", "the work options are \n\n" + out);
		return t;
	}

	public static Template buildingsList(Guild g) {
		List<Building> buildings = AlloyUtil.loadBuildings(g);
		String names = "";
		String costs = "";
		String gener = "";

		for (Building b : buildings) {
			names += "" + b.getName() + "\n";
			costs += "" + b.getCost() + "\n";
			gener += "" + b.getGeneration() + "\n";

		}
		Template t = new Template("Building list", "All available buildings");
		t.addFeild("Name", names, true);
		t.addFeild("Cost", costs, true);
		t.addFeild("Generation", gener, true);

		return t;
	}

	public static Template voiceDisconnectSuccess(VoiceChannel vc) {
		Template t = new Template("Voice Disconnect Success", "I have left " + vc.getName());
		return t;
	}

	public static Template voiceDisconnectFail() {
		Template t = new Template("Voice Disconnect Fail", "I have not left the vc i am in");
		return t;
	}

	public static Template onCooldown(Member m) {
		Template t = new Template("Slow down there", "you are on cooldown " + m.getAsMention());
		return t;
	}

	public static Template sayAdmin(String out, Message msg) {
		Template t = new Template("Announcement", out);
		t.setFooterName(msg.getAuthor().getAsTag());
		t.setFooterURL(msg.getAuthor().getAvatarUrl());
		return t;

	}

	public static Template help(String out) {
		Template t = new Template("HELP", out);
		return t;
	}

	public static Template helpSentTemplate() {
		Template t = new Template("Help has been sent", "albeit in the form of a DM");
		return t;
	}

	public static Template infoSelf() {
		Template t = new Template("My name is alloy",
				"I am Alloy and i am a Discord Bot frontsnapk1ck has been working on for a little while now. if you have any question feel free to reach out to him and join the official Alloy Support Server here https://discord.gg/7UNxyXRxBh \n\nthanks!");
		t.setFooterName("frontsnapk1ck");
		t.setFooterURL("https://cdn.discordapp.com/avatars/312743142828933130/7c63b41c5ed601b3314c1dce0d0e0065.png");
		return t;

	}

	public static Template infoUser(Member m) {
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

		Template t = new Template("User Info", "");
		t.addFeild(userF);
		t.addFeild(userIDF);

		t.addFeild(roles);

		t.addFeild(nickF);
		t.addFeild(botF);

		t.addFeild(permF);

		t.addFeild(joinF);
		t.addFeild(createF);

		t.setImageURL(getURL(m));

		return t;
	}

	private static String getURL(Member m) {
		String url = m.getUser().getAvatarUrl();
		if (url == null)
			url = m.getUser().getDefaultAvatarUrl();
		return url;
	}

	private static Field loadRolesField(Member m) {
		String roleOut = "```txt\n";
		List<Role> roles = m.getRoles();

		int i = 0;
		for (Role r : roles) {
			if (i < MAX_ROLE_SHOW) {
				roleOut += r.getName() + "\n";
				i++;
			}
		}
		roleOut += "```";

		String name = "Roles [" + i + "] (shows up to " + MAX_ROLE_SHOW + ")";

		Field f = new Field(name, roleOut, false);
		return f;
	}

	private static String loadPermString(Member m) {
		String permOut = "```txt\n";
		List<DisPerm> perms = DisPermUtil.parsePerms(m.getPermissions());
		for (DisPerm p : perms)
			permOut += "✅ " + p.getName() + "\n";
		permOut += "```";
		return permOut;
	}

	public static Template infoServer(Guild g) {
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

		Template t = new Template("Server Info", "");

		t.addFeild(nameF);
		t.addFeild(ownerF);

		t.addFeild(membersF);

		t.addFeild(idF);
		t.addFeild(regionF);

		t.addFeild(channels);

		t.addFeild(emojis);

		t.addFeild(boostF);
		t.addFeild(boostsF);

		t.addFeild(rolesF);

		t.addFeild(createdF);

		t.setImageURL(getURL(g));

		return t;
	}

	private static String getURL(Guild g) {
		String url = g.getIconUrl();
		if (url == null)
			url = AlloyUtil.DEFAULT_DISCORD_PHOTO;
		return url;
	}

	private static Field loadGuildRoles(Guild g) {
		List<Role> roles = g.getRoles();
		String roleOut = "```txt\n";

		int i = 0;
		for (Role r : roles) {
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

	private static Field loadEmojis(Guild g) {
		List<Emote> emojis = g.getEmotes();
		int animated = 0;
		int normal = 0;
		for (Emote e : emojis) {
			if (e.isAnimated())
				animated++;
			else
				normal++;
		}
		String name = "Server emojis [" + emojis.size() + "]";
		String value = "```txt\nNormal: " + normal + "\t|\tAnimated: " + animated + "\n```";
		return new Field(name, value, false);
	}

	private static Field loadChannelsFelid(Guild g) {
		List<GuildChannel> channels = g.getChannels();
		List<Category> catagories = g.getCategories();
		int text = 0;
		int voice = 0;
		int announcement = 0;
		for (GuildChannel c : channels) {
			if (c instanceof TextChannel) {
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

	private static Field loadMembersFelid(Guild g) {
		List<Member> members = g.getMembers();
		int user = 0;
		int bot = 0;
		for (Member member : members) {
			if (member.getUser().isBot())
				bot++;
			else
				user++;
		}

		String name = "Server members [" + members.size() + "]";
		String value = "```txt\nMember: " + user + "\t|\tBots: " + bot + "\n```";
		return new Field(name, value, false);
	}

	public static Template roleNotFound(String role) {
		Template t = new Template("Role not found", " i couldn't find the role " + role);
		return t;
	}

	public static Template infoRole(Role r) {
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

		Template t = new Template("Role Info", "");

		t.addFeild(nameF);
		t.addFeild(idF);

		t.addFeild(permsF);

		t.addFeild(memberCF);
		t.addFeild(rolePosF);
		t.addFeild(colorF);

		t.addFeild(members);

		t.addFeild(createdF);

		return t;
	}

	private static String getRoleColor(Role role) {
		Color c = role.getColor();
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		String hex = String.format("#%02x%02x%02x", r, g, b);
		return hex;
	}

	private static Field loadRoleMembers(Role r) {
		Guild g = r.getGuild();
		List<Member> members = g.getMembersWithRoles(r);
		String out = "```txt\n";

		int i = 0;
		for (Member member : members) {
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

	private static String loadRolePerms(Role r) {
		List<DisPerm> perms = DisPermUtil.parsePerms(r.getPermissions());
		String permOut = "```txt\n";
		for (DisPerm p : perms)
			permOut += "✅ " + p.getName() + "\n";
		permOut += "```";
		return permOut;
	}

	public static Template invite() {
		Template t = new Template("Invite", "Thanks for thinking of me");
		String rickroll = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
		t.setTitle("Click here to invite me", rickroll);
		return t;
	}

	public static Template inviteActual(Member m) {
		String inviteLink = "https://discord.com/api/oauth2/authorize?client_id=762825892006854676&permissions=435514430&scope=bot";
		Template t = new Template("This is your actual invite",
				"my creator will never miss an opportunity to rick roll someone\n\nclick [here](" + inviteLink + ")");
		return t;

	}

	public static Template uptime(String relativeTime) {
		String date = TimeUtil.date();
		String time = TimeUtil.time();
		Template t = new Template("Uptime",
				"i have been up for `" + relativeTime + "`\nthe time on my host is `" + date + "`\t`" + time + "`");
		return t;
	}

	public static Template noBlacklistedChannels() {
		Template t = new Template("No blacklisted Channels", "this guild doesn't have any blacklisted channels ");
		return t;
	}

	public static Template listBlackListedChannels(List<Long> blacklisted) {
		String out = "";
		for (Long id : blacklisted)
			out += "<#" + id + ">\n";

		Template t = new Template("Black Listed Channels for XP ", out);
		return t;
	}

	public static Template invalidChannel(String c) {
		Template t = new Template("Invalid Channel", c);
		return t;
	}

	public static Template channelIsNotBlacklisted(String c) {
		Template t = new Template("Channel is not blacklisted", "the channel " + c + " is not blacklisted");
		return t;
	}

	public static Template blackListRemoveSuccess(String c) {
		Template t = new Template("Blacklist Removed Success",
				"removed channel " + c + " from the blacklisted channels");
		return t;
	}

	public static Template blackListAddSuccess(String c) {
		Template t = new Template("Blacklist Add Success", "added channel " + c + " to the blacklisted channels");
		return t;
	}

	public static Template leaderboard(List<String> lb) {
		String out = "";
		for (String string : lb)
			out += string + "\n";

		Template t = new Template("leaderboard", out);
		return t;
	}

	public static Template userNotFound(String user) {
		Template t = new Template("User not found", "i could not find the user " + user);
		return t;
	}

	public static Template rank(Member target, int level, String progress) {
		Template t = new Template("Rank", target.getAsMention() + "\nlevel: `" + level + "`\nxp: `" + progress + "`");
		return t;
	}

	public static Template invalidRole(String string) {
		Template t = new Template("invalid role", "i did not recognize the role " + string);
		return t;
	}

	public static Template duplicateRankup(int level) {
		Template t = new Template("duplicate level detected",
				"you may have tried to make a level get announced twice - level" + level);
		return t;
	}

	public static Template levelNotFound(String string) {
		Template t = new Template("Level not found", string);
		return t;
	}

	public static Template rankupAddSuccess(RankUp ru) {
		Template t = new Template("rank up add Success", "added a rankup messaged to level " + ru.getLevel());
		return t;
	}

	public static Template rankupRemoveSuccess(RankUp toRm) {
		Template t = new Template("rankup remove Success", "removed a rankup messaged from level " + toRm.getLevel());
		return t;
	}

	public static Template viewRankUps(List<RankUp> rus) {
		String out = "";
		for (RankUp rankUp : rus)
			out += "level `" + rankUp.getLevel() + "`\n";
		Template t = new Template("Levels with Rankups", out);
		return t;
	}

	public static Template xpSetSuccess(Member target, int xp) {
		Template t = new Template("XP set Success", "set " + target.getAsMention() + "'s xp to `" + xp + "`");
		return t;
	}

	public static Template cannotModerateModerators() {
		Template t = new Template("Cannot Moderate Moderators",
				"sorry, i cant moderate the moderators. if you need to punish them remove their perms first");
		return t;
	}

	public static Template showBuildings(User author, Map<Building, Integer> owned) {
		Set<Building> bs = owned.keySet();
		String[][] data = new String[3][bs.size()];

		int i = 0;
		for (Building building : bs) {
			int num = owned.get(building);
			String name = building.getName();
			int gener = building.getGeneration();

			data[0][i] = "" + name + "\n";
			data[1][i] = "" + gener + "\n";
			data[2][i] = "" + num + "\n";

			i++;
		}

		Template t = new Template(author.getAsTag(), "the buildings you own");
		t.addFeild("Name", StringUtil.joinStrings(data[0]), true);
		t.addFeild("Generation", StringUtil.joinStrings(data[1]), true);
		t.addFeild("Quantity", StringUtil.joinStrings(data[2]), true);
		return t;
	}

	public static Template buildingNameNotRecognized(String args) {
		Template t = new Template("Building Name Not recognized",
				"i couldn't find a building with the name " + args + " in this guild\nmake sure to check spelling");
		return t;
	}

	public static Template buildingBuySuccess(Building toBuy, User author) {
		Template t = new Template("Building Bought", "you have bought the building `" + toBuy.getName() + "` "
				+ author.getAsMention() + " for the price of `$" + toBuy.getCost() + "`");
		return t;
	}

	public static Template workSuccess(String option, int amt) {
		Template t = new Template("Work", option + "\nyou made $" + amt);
		return t;
	}

	public static Template spamChannelChanged(TextChannel target) {
		Template t = new Template("Spam Channel Changed",
				"the spam channel has been changed to " + target.getAsMention());
		return t;
	}

	public static Template channelIsAlreadyBlacklisted(TextChannel channel) {
		Template t = new Template("Channel Already Blacklisted",
				"this channel " + channel.getAsMention() + " is already blacklisted");
		return t;
	}

	public static Template remindMe(String out) {
		Template t = new Template("Reminder", "you have a reminder with the message:\n" + out);
		return t;
	}

	public static Template timeNotRecognized(String time) {
		Template t = new Template("Time not recognized", "i didn't recognize the time `" + time + "`");
		return t;
	}

	public static Template remindCard(String time, String out) {
		Template t = new Template("Reminder", "I will remind you in `" + time + "` with the message:\n" + out);
		return t;
	}

	public static Template remindMeDM(String out, Message msg) {
		String link = DisUtil.getLink(msg);
		Template t = new Template("Reminder",
				"you have a reminder with the message:\n" + out + "\n\nlink [here](" + link + ")");
		return t;
	}

	public static Template linkEmbed(String link, String text) 
	{
		Template t = new Template ("link" , "[" + text + "](" + link + ")" );
		return t;
	}

	public static Template donate(User author) 
	{
		// String link = "https://paypal.me/frontsnapk1ck";
		String link = "UNAVAILABLE RIGHT NOW";
		Template t = new Template("Donations"  , "thank you so much for thinking of me " + author.getAsMention() + "\n\nyou can donate to me [here](" + link + ")" );
		return t;
	}

	public static Template debug(DebugEvent e) 
	{
		if (e.getLevel() == Level.ERROR)
			return error(e);
		if (e.getLevel() == Level.INFO)
			return info(e);
		return new Template("Something Happened" , "You should take a look, ig");
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

}