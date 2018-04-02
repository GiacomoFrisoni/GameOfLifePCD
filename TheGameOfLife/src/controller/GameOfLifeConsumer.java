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
	public GameOfLifeConsumer(final BlockingQueue<GenerationResult> queue, final GameOfLifeFrame view, final Flag stopFlag) {
		this.queue = queue;
		this.view = view;
		this.stopFlag = stopFlag;
	}
	
	@Override
	public void run() {
		GenerationResult res;
		while (!stopFlag.isOn()) {
			try {
				// Waits for minimum view updating frequency
				Thread.sleep(2000);
				
				// Retrieves a generation result, waiting if necessary until an element becomes available.
				res = queue.take();
				
				// Updates view
				System.out.println(res.getComputationTime());
				view.setGenerationInfo(res.getGenerationNumber(), res.getComputationTime(), res.getCellsAlive());
				view.drawCells(res.getCellsStates());

			} catch (InterruptedException ie) {
				ie.printStackTrace();
				// Stop + view notification
			}
		}
	}
}
