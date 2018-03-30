package controller;

import java.util.concurrent.BlockingQueue;

import model.GenerationResult;
import view.GameOfLifeFrame;

public class GameOfLifeConsumer extends Thread {
	
	private final BlockingQueue<GenerationResult> queue;
	private final GameOfLifeFrame view;
	private final Flag stopFlag;
	
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
				/*
				view.setCurrentGenerationInfo("" + res.getGenerationNumber());
				view.setTimeElapsedInfo("" + res.getComputationTime() + " ms");
				view.setLiveCellsInfo("" + res.getCellsAlive());
				view.drawCells(res.getUpdatedCells());
				*/
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				// Stop + view notification
			}
		}
	}
}
