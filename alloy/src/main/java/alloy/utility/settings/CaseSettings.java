package alloy.utility.settings;

import alloy.command.util.PunishType;

public class CaseSettings extends AbstractSettings {
    
    private int num;
    private long issuer;
    private String reason;
    private PunishType punishType;
    private long messageId;
    private long target;

    public CaseSettings() 
    {
        this.num = -1;
        this.issuer = 0l;
        this.reason = "No Reason Provided";
        this.punishType = null;
        this.messageId = 0l;
        this.target = 0l;
        setPath("");
    }

    public long getIssuer() {
        return issuer;
    }

    public long getMessageId() {
        return messageId;
    }

    public int getNum() {
        return num;
    }

    public PunishType getPunishType() {
        return punishType;
    }

    public String getReason() {
        return reason;
    }

    public long getTargetId() 
    {
        return target;
    }

    @Override
    public CaseSettings setPath(String path) 
    {
        super.setPath(path);
        return this;
    }

    public CaseSettings setIssuer(long issuer) 
    {
        this.issuer = issuer;
        return this;

    }

    public CaseSettings setMessageId(long messageId) 
    {
        this.messageId = messageId;
        return this;
    }

    public CaseSettings setNum(int num) 
    {
        this.num = num;
        return this;
    }

    public CaseSettings setPunishType(PunishType punishType) 
    {
        this.punishType = punishType;
        return this;
    }

    public CaseSettings setReason(String reason) 
    {
        this.reason = reason;
        return this;
    }

    public CaseSettings setTarget(long target) 
    {
        this.target = target;
        return this;
    }

    public CaseSettings copy() 
    {
        CaseSettings settings = new CaseSettings();
        settings.setIssuer(issuer)
                .setMessageId(messageId)
                .setNum(num)
                .setPath(getPath())
                .setReason(reason)
                .setTarget(target);
        
        return settings;
    }

}
