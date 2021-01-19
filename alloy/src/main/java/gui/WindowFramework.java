package gui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;


/**
 * 	Base class for the main window of the game. This class
 * 	handles all layout details. It should be extends to
 * 	manage game specific details that need to be added.
 * 
 * 	A WindowFramework handles the screen that is currently
 * 	active in the game. The screen can be switched out
 * 	for another one at any time. Whenever this window is
 * 	resized, it will also resize the current screen. 
 * 	
 */
public abstract class WindowFramework extends JFrame {

    private static final long serialVersionUID = 2438097905045342949L;
    
    private ScreenFramework currentScreen;
	
	
	/**
	 * 	Sets up the window's framework.
	 */
	public WindowFramework()
	{
		// Remove layout so we have control
		this.setLayout( null );
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		// Register so when this window is resized,
		//	we can intercept and update the screen.
		this.addComponentListener( new ComponentAdapter() 
		{
			@Override public void componentResized( ComponentEvent e ) 
			{
				onWindowResize();
			}
		});
	}

//=============================================================
//				SCREENS
//=============================================================
	
	/**
	 * 	Returns the current screen being displayed.
	 */
	public ScreenFramework getCurrentScreen()
	{
		return this.currentScreen;
	}
	
	/**
	 * 	Sets the current screen for this window
	 * 	and returns the screen that was replaced
	 * 	if there was one.
	 */
	public ScreenFramework setCurrentScreen( ScreenFramework screen )
	{		
		// Setup the dimensions of new screen.
		this.packScreen( screen );

		// Switch screens
		ScreenFramework old = this.getCurrentScreen();
		this.currentScreen = screen;
		
		// Extract the panel details
		if ( old != null )
			this.remove( old.getPanel() );
		this.add( screen.getPanel() );

		//Update window
		this.revalidate();
		this.repaint();

		// Return the replaced screen
		return old;
	}
	
	/**
	 * 	Sets the bounds of the given screen to fill
	 * 	this window.
	 */
	protected void packScreen( ScreenFramework screen )
	{
		if ( screen == null )
			return;
		
		int w = this.getWidth();
		int h = this.getHeight();
		screen.getPanel().setBounds( 0, 0, w, h );
	}
	
	/**
	 * 	Automatically packs the current screen if
	 * 	there is one.
	 */
	protected void packScreen()
	{
		ScreenFramework screen = this.getCurrentScreen();
		if ( screen != null )
			this.packScreen( screen );
	}
	
//=============================================================
//				DIMENSIONS
//=============================================================
	
	/** 
	 * 	Updates the size of this window and updates
	 * 	the current screen to fit
	 */
	@Override 
	public void setSize( int w, int h )
	{
		super.setSize( w, h );
		
		// Update screen
		this.packScreen();
	}
	
	/** 
	 * 	Updates the size of this window and updates
	 * 	the current screen to fit
	 */
	@Override
	public void setSize( Dimension d )
	{
		super.setSize( d );
		
		// Update screen
		this.packScreen();
	}

//=============================================================
//				RESIZING
//=============================================================
	
	/**
	 * 	Handles what to do when this window's size
	 * 	has changed. By default this will update the
	 * 	current screen's dimensions.
	 */
	protected void onWindowResize()
	{
		// Repack the current screen
		this.packScreen();
	}

}