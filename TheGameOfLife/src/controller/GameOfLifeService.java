package controller;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import model.ConwayCellMap;
import model.GenerationResult;
import view.GameOfLifeFrame;

/**
 * This class models the thread that creates the producer and the consumer
 * of the game.
 *
 */
public class GameOfLifeService extends Thread {

	private final BlockingQueue<GenerationResult> queue;
	private final ExecutorService executor;
	private final ConwayCellMap model;
	private final GameOfLifeFrame view;
	private final Flag stopFlag;
	
	/**
	 * Creates a new Game Of Life service.
	 * 
	 * @param queue
	 * 		the producer / consumer queue
	 * @param executor
	 * 		the executor service
	 * @param model
	 * 		the application model
	 * @param view
	 * 		the application view
	 * @param stopFlag
	 * 		the stop flag
	 */
	public GameOfLifeService(final BlockingQueue<GenerationResult> queue, final ExecutorService executor,
			final ConwayCellMap model, final GameOfLifeFrame view, final Flag stopFlag) {
		this.queue = queue;
		this.executor = executor;
		this.model = model;
		this.view = view;
		this.stopFlag = stopFlag;
	}
	
	@Override
	public void run() {
		if (this.model.getCellsToEvaluate().isEmpty()) {
			this.stopFlag.setOn();
			// view.changeState("No point in Matrix!");
		}
		
		// Starts producer and consumer threads
		new GameOfLifeProducer(this.queue, this.executor, this.model, this.stopFlag).start();
		new GameOfLifeConsumer(this.queue, this.view, this.stopFlag).start();
	}
	
}
