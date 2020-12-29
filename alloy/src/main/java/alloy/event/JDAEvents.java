package alloy.event;

import alloy.input.discord.AlloyInput;
import alloy.input.discord.AlloyInputEvent;
import alloy.main.Alloy;
import alloy.main.handler.AlloyHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JDAEvents extends ListenerAdapter {

    private AlloyHandler bot;

    public JDAEvents(Alloy alloy) {
        this.bot = alloy;
    }

    @Override
    public void onStatusChange(StatusChangeEvent event) {
        super.onStatusChange(event);
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e) {
        Guild g = e.getGuild();
        Alloy.LOGGER.info("JDAEvents", "[event] JOINED SERVER! " + g.getName());
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e) {
        Guild g = e.getGuild();
        Alloy.LOGGER.info("JDAEvents", "[event] LEFT SERVER! " + g.getName());
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        super.onMessageReactionRemove(event);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) 
    {
        Guild        g = e.getGuild();
        TextChannel  c = e.getChannel();
        User         u = e.getAuthor();
        Message      m = e.getMessage();

        AlloyInputEvent event = new AlloyInputEvent( g , c , u , m , e);

        AlloyInput in = new AlloyInput("USER", event);
        bot.handleMessage( in );
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) 
    {
        bot.handlePrivateMessage(e.getChannel() , e.getAuthor() , e.getMessage());
    }

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) 
    {
        super.onGuildMemberUpdateNickname(event);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) 
    {
        super.onGuildMemberJoin(event);
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) 
    {
        super.onGuildMemberRemove(event);
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) 
    {
        super.onGuildVoiceJoin(event);
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) 
    {
        super.onGuildVoiceMove(event);
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) 
    {
        super.onGuildVoiceLeave(event);
    }

    @Override
    public void onRoleDelete(RoleDeleteEvent event) 
    {
        super.onRoleDelete(event);
    }

    @Override
    public void onCategoryCreate(CategoryCreateEvent event) 
    {
        super.onCategoryCreate(event);
    }

    @Override
    public void onCategoryDelete(CategoryDeleteEvent event) 
    {
        super.onCategoryDelete(event);
    }

    @Override
    public void onCategoryUpdateName(CategoryUpdateNameEvent event) 
    {
        super.onCategoryUpdateName(event);
    }

    @Override
    public void onGuildInviteCreate(GuildInviteCreateEvent event) 
    {
        super.onGuildInviteCreate(event);
    }

    @Override
    public void onGuildInviteDelete(GuildInviteDeleteEvent event) 
    {
        super.onGuildInviteDelete(event);
    }

    @Override
    public void onGuildUnban(GuildUnbanEvent event) 
    {
        super.onGuildUnban(event);
    }

    @Override
    public void onGuildBan(GuildBanEvent event) 
    {
        super.onGuildBan(event);
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) 
    {
        super.onGuildMemberRoleAdd(event);
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) 
    {
        super.onGuildMemberRoleRemove(event);
    }

    @Override
    public void onRoleCreate(RoleCreateEvent event) 
    {
        super.onRoleCreate(event);
    }

    @Override
    public void onRoleUpdateColor(RoleUpdateColorEvent event) 
    {
        super.onRoleUpdateColor(event);
    }

    @Override
    public void onRoleUpdateName(RoleUpdateNameEvent event) 
    {
        super.onRoleUpdateName(event);
    }

    @Override
    public void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event) 
    {
        super.onRoleUpdatePermissions(event);
    }
}
