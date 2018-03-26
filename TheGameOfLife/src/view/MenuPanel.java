package view;

import java.awt.Dimension;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.GameController;

public class MenuPanel extends JPanel {

	/**
	 * Auto-generated UID.
	 */
	private static final long serialVersionUID = -3478859524085262735L;

	private static final int PANEL_WIDTH = 100;
	
	private final GameController controller;
	
	private final JButton start;
	private final JButton end;
	
	private MenuObserver observer;
	
	public MenuPanel(final GameController controller) {
		this.controller = Objects.requireNonNull(controller);

        final GUIFactory factory = new GUIFactory.Standard();
        this.setLayout(new GridLayout(0, 2));
        this.setBackground(BG_COLOR);

        // Sets the panel containing the time and the score
        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 2));
        infoPanel.setOpaque(false);
        this.time = factory.createLabel(factory.getDescriptionFont(), Color.WHITE);
        this.time.setHorizontalAlignment(SwingConstants.CENTER);
        updateTime(0L);
        infoPanel.add(this.time);
        this.score = factory.createLabel(factory.getDescriptionFont(), Color.WHITE);
        infoPanel.add(this.score);
        this.add(infoPanel);

        // Sets the panel containing the statistics of the hero
        final JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(0, NUM_STATS));
        statsPanel.setOpaque(false);
        this.life = factory.createLabel(factory.getDescriptionFont(), Color.WHITE);
        this.attack = factory.createLabel(factory.getDescriptionFont(), Color.WHITE);
        this.bombs = factory.createLabel(factory.getDescriptionFont(), Color.WHITE);
        this.range = factory.createLabel(factory.getDescriptionFont(), Color.WHITE);
        statsPanel.add(factory.createImageWithLabelPanel(ImageLoader.createImage(GameImage.LIFE_INFO), this.life));
        statsPanel.add(factory.createImageWithLabelPanel(ImageLoader.createImage(GameImage.ATTACK_INFO), this.attack));
        statsPanel.add(factory.createImageWithLabelPanel(ImageLoader.createImage(GameImage.BOMBS_INFO), this.bombs));
        statsPanel.add(factory.createImageWithLabelPanel(ImageLoader.createImage(GameImage.RANGE_INFO), this.range));
        this.add(statsPanel);
        */
	}
	
	@Override
    public final Dimension getPreferredSize() {
        return new Dimension(PANEL_WIDTH, 0);
    }
	
	/**
     * Set the observer of the MenuScene.
     * 
     * @param observer
     *          the observer to use
     */
    public void setObserver(final MenuObserver observer) {
        this.observer = observer;
    }

    /**
     * This interface indicates the operations that an observer
     * of the MenuScene can do.
     *
     */
    public interface MenuObserver {

        /**
         * Starts the game.
         */
        void start();
        
        /**
         * Show the scores of the player.
         */
        void stop();
    }
}
