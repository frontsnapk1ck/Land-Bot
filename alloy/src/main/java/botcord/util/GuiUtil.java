package botcord.util;

import java.awt.Color;

public class GuiUtil {

    public static Color randomColor() 
    {
        int r = (int)(Math.random() * 256);
        int g = (int)(Math.random() * 256);
        int b = (int)(Math.random() * 256);
        
        Color c = new Color(r,g,b);
        return c;
	}
    
    public static Color randomGrey()
    {
        int g = (int)(Math.random() * 256);
        
        Color c = new Color(g,g,g);
        return c;
    }

}
