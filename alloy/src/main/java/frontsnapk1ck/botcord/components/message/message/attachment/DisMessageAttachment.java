package frontsnapk1ck.botcord.components.message.message.attachment;

import net.dv8tion.jda.api.entities.MessageEmbed;

public class DisMessageAttachment {

    public static AbstractMessageAttachment load( String string)
    {
        String ex = string.substring(string.length() - 8).split("\\.")[1];
        //TODO add in more extensions
        boolean photo = ex.equalsIgnoreCase("gif") ||
                        ex.equalsIgnoreCase("png") ||
                        ex.equalsIgnoreCase("jpg");

        boolean video = ex.equalsIgnoreCase("mp4") ||
                        ex.equalsIgnoreCase("mp3") ||
                        ex.equalsIgnoreCase("mov");
        if ( photo )
            return new PhotoMessageAttachment(string);
        if ( video )
            return new VideoMessageAttachment(string);

        throw new RuntimeException("UNKNOWN FILE TYPE", new Throwable());

    }

    public static AbstractMessageAttachment load (MessageEmbed embed)
    {
        return new EmbedMessageAttachment(embed);
    }


}
