package controller;

import java.awt.Dimension;
import java.awt.Point;
import java.util.concurrent.ExecutionException;

import model.ConwayCellMap;
import view.GameOfLifeFrame;

public class GameOfLifeService extends Thread {
	
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
		try {
			if (this.model.getCellMap().isEmpty()) {
				this.stopFlag.setOn();
				//view.changeState("No point in Matrix!");
			}
			
			final int poolSize = Runtime.getRuntime().availableProcessors() + 1;

			final Master master = new Master(model, poolSize, stopFlag);
			
			final Chrono cron = new Chrono();
			long liveCells = 0;
			
			while (!stopFlag.isOn()) {
				Thread.sleep(3000);
				
				System.out.println("Generation: " + this.model.getGenerationNumber() + ". Time elapsed: " + cron.getTime() + " ms.");
				System.out.println("Live cells: " + liveCells);
				this.model.print();
				
				cron.start();
				liveCells = master.compute();
				cron.stop();

				System.out.println("Generation: " + this.model.getGenerationNumber() + ". Time elapsed: " + cron.getTime() + " ms.");
				System.out.println("Live cells: " + liveCells);
				view.setCurrentGenerationInfo("" + this.model.getGenerationNumber());
				view.setTimeElapsedInfo("" + cron.getTime() + " ms");
				view.setLiveCellsInfo("" + liveCells);
				
				this.model.nextGeneration();
				
				view.drawCells(this.model.getCellMap());
				//Thread.sleep(2000);
				
				if (stopFlag.isOn()) {
					// view.changeState("Interrupted. Cell live: " + result.size());
				} else {
					// view.changeState(String.valueOf(result.size()));
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			// view.changeState("An error has occurred");
		}
	}
}
