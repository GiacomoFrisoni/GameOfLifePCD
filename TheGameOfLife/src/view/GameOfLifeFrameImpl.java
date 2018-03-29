package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.GameController;
import model.ConwayCell;
import view.ImageLoader.GameImage;

/**
 * A view for the rendering of the main screen.
 *
 */
public class GameOfLifeFrameImpl implements GameOfLifeFrame {

	private static final String FRAME_NAME = "The Game Of Life";
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 800;
	
	
	private boolean initialized;
	
	private GameController controller;
	
	private JFrame frame;
	private CellMapDrawPanel cellMapPanel;
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
		this.frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                exitProcedure();
            }
        });
		this.frame.setResizable(false);
		
		// Sets the panels
		this.cellMapPanel = new CellMapDrawPanel();
		this.menuPanel = new MenuPanel(this.controller);
		
		// Sets the layout
		final JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(this.cellMapPanel, BorderLayout.CENTER);
		mainPanel.add(this.menuPanel, BorderLayout.EAST);
	
		
		this.frame.add(mainPanel);
		this.frame.setLocationByPlatform(true);
		this.frame.setFocusable(true);
		this.frame.pack();
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
    }
    
    @Override
	public void drawCells(Set<ConwayCell> cells) {
    	checkInitialization();
    	this.cellMapPanel.setCellsToPaint(cells);
        this.cellMapPanel.repaint();
    	this.cellMapPanel.revalidate();
	}
    
    @Override
    public void closeView() {
        checkInitialization();
        this.frame.dispose();
    }
    
    /*
     * Throws a {link IllegalStateException} when a method is called without first initializing the frame.
     */
    private void checkInitialization() {
        if (!this.initialized) {
            throw new IllegalStateException("Main frame not initialized");
        }
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

	
}
