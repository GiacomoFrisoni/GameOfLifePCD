package controller;

import java.util.concurrent.BlockingQueue;

import model.GenerationResult;
import view.GameOfLifeFrame;

/**
 * This class models a Game Of Life Consumer.
 * It picks up the results of the computations from the buffer and shows them
 * on video with the minimum frequency desired.
 *
 */
public class GameOfLifeConsumer extends Thread {
	
	private static final int DEFAULT_MIN_TICK_TIME = 1500;
	
	// Time when last update happened. Used for controlling the frame rate
	private long lastUpdate;
	
	// Desired frame duration
	private volatile int minTickTime;
	
	private final BlockingQueue<GenerationResult> queue;
	private final GameOfLifeFrame view;
	private final Flag stopFlag;
	
	
	/**
	 * Constructs a new Game Of Life consumer.
	 * 
	 * @param queue
	 * 		the producer / consumer queue
	 * @param view
	 * 		the application view
	 * @param stopFlag
	 * 		the stop flag
	 */
	public GameOfLifeConsumer(final BlockingQueue<GenerationResult> queue, final GameOfLifeFrame view,
			final Flag stopFlag) {
		this.queue = queue;
		this.view = view;
		this.stopFlag = stopFlag;
		this.minTickTime = DEFAULT_MIN_TICK_TIME;
	}
	
	/**
	 * Counts time that passed since last game update
	 * and sleeps for a while if this time was shorter than target frame time.
	 * @throws InterruptedException
	 */
	private void limitFPS() throws InterruptedException {
		final long now = System.currentTimeMillis();
		if (lastUpdate > 0) {
			final long delta = now - lastUpdate;
			if (delta < this.minTickTime) {
				Thread.sleep(this.minTickTime - delta);
			}
		} else {
			Thread.sleep(this.minTickTime);
		}
		lastUpdate = System.currentTimeMillis();
	}
	
	/**
	 * @return the current minimum delay used by the view consumer.
	 */
	public int getConsumerSpeed() {
		return this.minTickTime;
	}
	
	/**
	 * Sets the minimum delay between each view consumer representation.
	 * 
	 * @param minTickTime
	 * 		the minimum delay between each frame
	 */
	public void setConsumerSpeed(final int minTickTime) {
		this.minTickTime = minTickTime;
	}
	
	@Override
	public void run() {
		GenerationResult res;
		while (!stopFlag.isOn()) {
			try {
				// Waits for minimum view updating frequency
				limitFPS();
				
				// Retrieves a generation result, waiting if necessary until an element becomes available.
				res = queue.take();
				
				// Updates view
				this.view.setGenerationInfo(res.getGenerationNumber(), res.getComputationTime(), res.getCellsAlive());
				this.view.drawCells(res.getCellsStates());
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				// Stop + view notification
			}
		}
	}
}
