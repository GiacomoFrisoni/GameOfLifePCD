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
	
	// Desired frame duration
	private static final int MIN_TICK_TIME = 1500;
	
	// Time when last update happened. Used for controlling the frame rate
	private long lastUpdate;
	
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
			if (delta < MIN_TICK_TIME) {
				Thread.sleep(MIN_TICK_TIME - delta);
			}
		}
		lastUpdate = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		GenerationResult res;
		this.lastUpdate = System.currentTimeMillis();
		while (!stopFlag.isOn()) {
			try {
				// Retrieves a generation result, waiting if necessary until an element becomes available.
				res = queue.take();
				
				// Updates view
				view.setGenerationInfo(res.getGenerationNumber(), res.getComputationTime(), res.getCellsAlive());
				view.drawCells(res.getCellsStates());
				view.updateProgress(0);

				// Waits for minimum view updating frequency
				limitFPS();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				// Stop + view notification
			}
		}
	}
}
