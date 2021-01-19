package gui;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

/**
 * 	Basic framework for a screen in the game. Screens
 * 	are designed to interact with the WindowFramework
 * 	and represent different "screens" during the game.
 * 	
 */
public abstract class ScreenFramework {

	
	/** Container for this screen */
    private JPanel panel;
    
	public ScreenFramework()
	{
		// Create container to hold screen
		this.panel = new JPanel();
		this.panel.setOpaque( true );
		
		// Register so when the panel is resized,
		//	we can intercept and update the screen.
		this.panel.addComponentListener( new ComponentAdapter() 
		{
			@Override public void componentResized( ComponentEvent e ) 
			{
				onScreenResize();
			}
		});
	}

//=============================================================
//					PANEL DETAILS
//=============================================================
	
	/** Returns the panel container for this screen */
	public JPanel getPanel()
	{
		return this.panel;
	}
	
	/**
	 * 	Sets the bounds of this screen and subsequently
	 * 	the bounds of the underlying container panel.
	 */
	public void setBounds( int x, int y, int w, int h )
	{
		this.getPanel().setBounds( x, y, w, h );
	}
	
	/** 
	 * 	Returns the width of this screen. By default
	 * 	this is the size of the main panel that 
	 * 	acts as the physical screen container.
	 */
	public int getWidth()
	{
		return this.getPanel().getWidth();
	}

	/** 
	 * 	Returns the height of this screen. By default
	 * 	this is the size of the main panel that 
	 * 	acts as the physical screen container.
	 */
	public int getHeight()
	{
		return this.getPanel().getHeight();
	}

//=============================================================
//					LOOK & FEEL
//=============================================================
	
	/**
	 * 	Sets the background color for this screen.
	 */
	public void setBackground( Color bg )
	{
		this.getPanel().setBackground( bg );
	}
	
//=============================================================
//					RESIZING
//=============================================================
	
	/**
	 * 	Handles any extra details that need to be
	 * 	calculated when this screen's size is
	 * 	changed. By default the screen's container
	 * 	panel's LayoutManager will handle components
	 * 	that have been added to it, unless that manager
	 * 	was removed by the screen. In this case this
	 * 	method will need to be override to manage
	 * 	what to do when the screen is resized.
	 */
	protected void onScreenResize()
	{
		// Nothing to do yet.
	}

}