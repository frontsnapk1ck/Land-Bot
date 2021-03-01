package disterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import disterface.util.DIUtil;

public class DisInterClient {

    private Socket socket;

    public DisInterClient() throws IOException 
    {
        tellConnecting();
        this.socket = new Socket(DIUtil.ADDRESS, DIUtil.PORT);
        tellConnected();
    }
    
    protected void tellConnected() 
    {
        System.out.println("client connected to " + DIUtil.ADDRESS.getHostAddress() + " on port " + DIUtil.PORT);
    }

    protected void tellConnecting() 
    {
        System.out.println("client is connecting to " + DIUtil.ADDRESS.getHostAddress() + " on port " + DIUtil.PORT);
    }

    public void send(MessageData data) throws IOException 
    {
        byte[] bytes = convertToBytes(data);

        OutputStream out = this.socket.getOutputStream();
        out.write(bytes);
    }

    private byte[] convertToBytes(Object object) throws IOException
    {
        try 
        (
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos)
        ) 
        {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }
    
    
}
