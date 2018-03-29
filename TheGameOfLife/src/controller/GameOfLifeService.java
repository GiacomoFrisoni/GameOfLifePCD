package controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import model.ConwayCellMap;
import model.GenerationResult;
import view.GameOfLifeFrame;

public class GameOfLifeService extends Thread {
	
	private static final int BUFFER_SIZE = 100;
	
	BlockingQueue<GenerationResult> queue;
	private final ConwayCellMap model;
	private final GameOfLifeFrame view;
	private final Flag stopFlag;
	
	public GameOfLifeService(final ConwayCellMap model, final GameOfLifeFrame view, final Flag stopFlag) {
		this.model = model;
		this.view = view;
		this.stopFlag = stopFlag;
	}
	
	@Override
	public void run() {
		if (this.model.getCellMap().isEmpty()) {
			this.stopFlag.setOn();
			//view.changeState("No point in Matrix!");
		}
		
		final int poolSize = Runtime.getRuntime().availableProcessors() + 1;
		
		this.queue = new ArrayBlockingQueue<>(BUFFER_SIZE);
		
		new GameOfLifeProducer(this.queue, this.model, poolSize, this.stopFlag).start();
		
		new GameOfLifeConsumer(this.queue, this.view, this.stopFlag).start();
	}
}
