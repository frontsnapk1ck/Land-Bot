package botcord.components.message.attachment;

import java.io.File;

import net.dv8tion.jda.api.entities.MessageEmbed;

public class DisMessageAttachment {

    public static AbstractMessageAttachment load( File f)
    {
        String ex = f.getName().split(".")[1];
        //TODO add in more extensions
        boolean photo = ex.equalsIgnoreCase(".gif") ||
                        ex.equalsIgnoreCase(".png") ||
                        ex.equalsIgnoreCase(".jpg");

        boolean video = ex.equalsIgnoreCase(".mp4") ||
                        ex.equalsIgnoreCase(".mp3") ||
                        ex.equalsIgnoreCase(".mov");
        if ( photo )
            return new PhotoMessageAttachment(f);
        if ( video )
            return new VideoMessageAttachment(f);

        throw new RuntimeException("UNKNOWN FILE TYPE", new Throwable());

    }

    public static AbstractMessageAttachment load (MessageEmbed embed)
    {
        return new EmbedMessageAttachment(embed);
    }


}
