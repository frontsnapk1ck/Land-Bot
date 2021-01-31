package alloy.handler;

import java.util.List;

import alloy.command.util.PunishType;
import alloy.gameobjects.Case;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.settings.CaseSettings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class CaseHandler {

    public static int nextID(Guild g) 
    {
        List<Case> cases = AlloyUtil.loadAllCases(g);
        if (cases.size() == 0)
            return 1;
        Case last = cases.get(cases.size() - 1);
        return last.getNum() + 1;
    }

    public static Case buildCase(int caseID, User author, PunishType punishType, String message, Member targetUser,
            Message msg) {
        if (message.equals(""))
            message = "No reason provided";

        String path = getCasePath(msg.getGuild(), caseID);

        CaseSettings settings = new CaseSettings();
        settings.setIssuer(author.getIdLong()).setMessageId(msg.getIdLong()).setNum(caseID).setPunishType(punishType)
                .setReason(message).setPath(path);

        return new Case(settings);
    }

    private static String getCasePath(Guild guild, int caseID) {
        String path = AlloyUtil.getGuildPath(guild);
        path += AlloyUtil.CASE_FOLDER;
        path += AlloyUtil.SUB;
        path += caseID;
        path += AlloyUtil.CASE_EXT;
        return path;
    }

    public static MessageEmbed toEmbed(Case c) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Case " + c.getNum());
        eb.addField(c.getPunishType().getKeyword(), "", true);
        eb.setDescription(c.getReason() + "\n\nissued by: <@!" + c.getIssuer() + ">");
        eb.setColor(c.getPunishType().getColor());
        return eb.build();
    }

    public static Case getCase(long idLong, String caseId) {
        try {
            int num = Integer.parseInt(caseId);
            List<Case> cases = AlloyUtil.loadAllCases(idLong);
            for (Case c : cases) {
                if (c.getNum() == num)
                    return c;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static void update(Case c) {

    }

}
