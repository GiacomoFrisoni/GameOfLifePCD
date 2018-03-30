package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.GameController;
import model.ConwayCell;

/**
 * A view for the rendering of the main screen.
 *
 */
public class GameOfLifeFrameImpl implements GameOfLifeFrame {

	private static final String FRAME_NAME = "The Game Of Life";
	private static final int FRAME_SCALE = 70;
	
	private boolean initialized;
	
	private GameController controller;
	
	private JFrame frame;
	private CellMapViewer cellMapViewer;
	private MenuPanel menuPanel;
	
	/**
	 * Creates a new frame for the game rendering.
	 */
	public GameOfLifeFrameImpl() {
		this.initialized = false;
	}
	
	@Override
	public void initView() {
		// Sets the frame
		this.frame = new JFrame();
		this.frame.setTitle(FRAME_NAME);
		this.frame.setLocationByPlatform(true);
		this.frame.setFocusable(true);
		setWindowSize();

		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                exitProcedure();
            }
        });
		
		// Sets the panels
		this.menuPanel = new MenuPanel(this.controller, this);
		this.cellMapViewer = new CellMapViewer(this.controller, this);
		
		// Sets the layout and add panels
		final JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(cellMapViewer, BorderLayout.CENTER);
		mainPanel.add(this.menuPanel, BorderLayout.EAST);
	
		//Add all panels to the frame
		this.frame.add(mainPanel);
		this.frame.pack();
		setStopped();
		this.initialized = true;
	}
	
	/**
     * Custom exit procedure to execute before the frame's closing.
     */
    private void exitProcedure() {
    	/*
        if (this.gameLoop.isRunningLoop()) {
            this.gameLoop.pauseLoop();
            if (JOptionPane.showConfirmDialog(frame,
                    LanguageHandler.getHandler().getLocaleResource().getString("exitConfirm"),
                    LanguageHandler.getHandler().getLocaleResource().getString("exit"),
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.gameLoop.stopLoop();
                closeView();
            } else {
                this.gameLoop.unPauseLoop();
            }
        } else {
            closeView();
        }
        */
    }
    
    @Override
    public void setObserver(final GameController observer) {
        this.controller = Objects.requireNonNull(observer);
    }
    
    @Override
    public void showView() {
        checkInitialization();
        this.frame.setVisible(true);
        
        //Update sizes of views
        this.cellMapViewer.getCellMap().initialize();
        this.menuPanel.setPreviewDimensionInfo(this.cellMapViewer.getCellMap().getDrawableXCellsNumber(), this.cellMapViewer.getCellMap().getDrawableYCellsNumber());
    }
    
    @Override
	public void drawCells(Set<ConwayCell> cells) {
    	checkInitialization();
    	this.cellMapViewer.getCellMap().setCellsToPaint(cells);
    	//this.cellMapViewer.getCellMap().revalidate();
	}
    
    @Override
    public void closeView() {
        checkInitialization();
        this.frame.dispose();
    }
    

    

	@Override
	public void setCurrentGenerationInfo(String text) {
		menuPanel.setCurrentGenerationInfo(text);
	}

	@Override
	public void setTimeElapsedInfo(String text) {
		menuPanel.setTimeElapsedInfo(text);
	}

	@Override
	public void setLiveCellsInfo(String text) {
		menuPanel.setLiveCellsInfo(text);
	}

	@Override
	public Dimension getMapDimension() {
		return this.menuPanel.getMapDimension();
	}

	@Override
	public void setStarted() {
		this.menuPanel.setStarted();
	}

	@Override
	public void setStopped() {
		this.menuPanel.setStopped();
	}
	
	@Override
	public void reset() {
		this.menuPanel.reset();
		//this.cellMapPanel.clear();
		this.menuPanel.reset();
	}

	
	MenuPanel getMenuPanel() {
		return this.menuPanel;
	}
	
	CellMapViewer getCellMapViewer() {
		return this.cellMapViewer;
	}
	
	
	
	
    /*
     * Throws a {link IllegalStateException} when a method is called without first initializing the frame.
     */
    private void checkInitialization() {
        if (!this.initialized) {
            throw new IllegalStateException("Main frame not initialized");
        }
    }
    
    private void setWindowSize() {
    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    	int width = (gd.getDisplayMode().getWidth() * FRAME_SCALE) / 100;
    	int height = (gd.getDisplayMode().getHeight() * FRAME_SCALE) / 100;
    	
    	this.frame.setSize(width, height);
		this.frame.setPreferredSize(new Dimension(width, height));
		this.frame.setResizable(false);
    }
}
