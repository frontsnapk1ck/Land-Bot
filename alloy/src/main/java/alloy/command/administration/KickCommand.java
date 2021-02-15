package alloy.command.administration;

import java.util.function.Consumer;

import alloy.command.util.AbstractModerationCommand;
import alloy.command.util.PunishType;
import alloy.main.Alloy;
import alloy.main.intefs.Sendable;
import alloy.utility.discord.perm.DisPerm;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class KickCommand extends AbstractModerationCommand {

    @Override
    protected PunishType getPunishType()
    {
        return PunishType.KICK;
    }

    @Override
    protected DisPerm getRequiredPermission()
    {
        return DisPerm.KICK_MEMBERS;
    }

    @Override
    protected boolean punish(Sendable bot, Guild guild, Member member) 
    {
        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("KickCommand", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_USER, consumer);

        guild.kick(member).queue(null, handler);
        return true;
    }

}
