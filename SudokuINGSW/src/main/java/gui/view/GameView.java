package gui.view;

import java.util.ArrayList;
import java.util.Random;

import gui.model.DIFFICULTY;
import gui.model.NumberButton;
import gui.model.SudokuButton;
import gui.model.SudokuCell;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.stage.Stage;
import logic.ai.Cell;
import logic.ai.GameManager;

@SuppressWarnings({ "restriction" })
public class GameView extends ViewManager implements IView {
	
	private Stage stage;
	
	private DIFFICULTY difficulty;
	
	private ArrayList<SudokuButton> gameButtons;
	private ArrayList<SudokuCell> sudokuCells;
	private ArrayList<SudokuCell> startGrid;
	
	public GameView(DIFFICULTY difficulty) 
	{
		stage = new Stage();

		this.difficulty = difficulty;

		sudokuCells = new ArrayList<SudokuCell>();
		gameButtons = new ArrayList<SudokuButton>();
		
		gameManager = new GameManager();
		gameManager.generateSudoku();

		createBackground();
		createButtons();
		createGrid(gameManager.getGrid());
		
		stage.setScene(scene);
		stage.show();
	}

	public GameView(ArrayList<SudokuCell> sudokuCells) 
	{
		stage = new Stage();

		gameManager = new GameManager();
		
		this.sudokuCells = new ArrayList<SudokuCell>();
		this.sudokuCells = sudokuCells;
		
		gameButtons = new ArrayList<SudokuButton>();

		createBackground();
		createButtons();		
		loadGrid();
		
		stage.setScene(scene);
		stage.show();
	}

	public void createBackground() 
	{
		Image backroundImage = new Image("/gui/resources/texture.png");
		BackgroundImage background = new BackgroundImage(backroundImage, BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		pane.setBackground(new Background(background));
		
		Rectangle rectangle = new Rectangle(160, 115, 420, 411);
		rectangle.setFill(Color.rgb(70, 169, 255));
		rectangle.setStroke(Color.rgb(90, 125, 155));
		Rectangle rectangle2 = new Rectangle(200, 145, 335, 320);
		rectangle2.setFill(Color.rgb(90, 125, 155));

		pane.getChildren().add(rectangle);
		pane.getChildren().add(rectangle2);
	}
	
	public void createButtons() 
	{
		SudokuButton backBtn = new SudokuButton("< back");
		backBtn.setLayoutX(20);
		backBtn.setLayoutY(20);
		gameButtons.add(backBtn);
		backBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)) {
					hiddenStage.show();
					stage.close();
				}
			}
			
		});
				
		SudokuButton undoBtn = new SudokuButton("undo");
		undoBtn.setLayoutX(200);
		undoBtn.setLayoutY(60);		
		gameButtons.add(undoBtn);
		
		SudokuButton redoBtn = new SudokuButton("redo");
		redoBtn.setLayoutX(350);
		redoBtn.setLayoutY(60);		
		gameButtons.add(redoBtn);		

		SudokuButton infoBtn = new SudokuButton("info");
		infoBtn.setLayoutX(590);
		infoBtn.setLayoutY(20);		
		gameButtons.add(infoBtn);

		final SudokuButton newGameBtn = new SudokuButton("new game");
		newGameBtn.setLayoutX(590);
		newGameBtn.setLayoutY(120);		
		gameButtons.add(newGameBtn);
		newGameBtn.setOnMousePressed(new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)) 
				{
					newGameBtn.setLayoutY(123.0 );		
					gameManager.generateSudoku();
					createGrid(gameManager.getGrid());
				}
			}
		});

		final SudokuButton restartBtn = new SudokuButton("restart");
		restartBtn.setLayoutX(590);
		restartBtn.setLayoutY(170);		
		gameButtons.add(restartBtn);
		restartBtn.setOnMousePressed(new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent event) 
			{
				if(event.getButton().equals(MouseButton.PRIMARY)) 
				{
					restartBtn.setLayoutY(173.0);
					sudokuCells = getStartGrid();
				}
			}
		});

		SudokuButton hintBtn = new SudokuButton("hint");
		hintBtn.setLayoutX(590);
		hintBtn.setLayoutY(220);		
		gameButtons.add(hintBtn);		
		
		SudokuButton deleteBtn = new SudokuButton("delete");
		deleteBtn.setLayoutX(590);
		deleteBtn.setLayoutY(480);		
		gameButtons.add(deleteBtn);
				
		SudokuButton stopBtn = new SudokuButton("stop");
		stopBtn.setLayoutX(200);
		stopBtn.setLayoutY(530);		
		gameButtons.add(stopBtn);	

		SudokuButton playBtn = new SudokuButton("play");
		playBtn.setLayoutX(350);
		playBtn.setLayoutY(530);		
		gameButtons.add(playBtn);

		final SudokuButton saveBtn = new SudokuButton("save");
		saveBtn.setLayoutX(590);
		saveBtn.setLayoutY(530);		
		gameButtons.add(saveBtn);
		saveBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)) {
					saveBtn.setLayoutY(533.0);
					gameManager.saveGame(difficulty, sudokuCells);
				}
			}
		});

		for(SudokuButton gb : gameButtons) {
			gb.setScaleX(0.7);
			gb.setScaleY(0.7);
			pane.getChildren().add(gb);
		}

		int pos = 170;
		for(int i = 1; i < 10; i++) {
			NumberButton b = new NumberButton(Integer.toString(i));
			b.setLayoutX(pos);
			b.setLayoutY(492);
			pos += 47;
			pane.getChildren().add(b);
		}
	}

	private void createGrid(ArrayList<Cell> cells) 
	{
		int cellToShow = 0;
		
		switch (difficulty)
		{
			case EASY:
				cellToShow = 5*9;
				break;
			case NORMAL:
				cellToShow = 4*9;
				break;
			case HARD:
				cellToShow = 3*9;
				break;
			default:
				break;
		}
		
		int x;
		int y = 130;
		
		if(sudokuCells.isEmpty())
		{
			for(int r = 0; r < 9; r++) 
			{
				x = 190;
				for (int c = 0; c < 9; c++) 
				{
					for(Cell cell : cells) 
					{
						if(cell.getRow() == r && cell.getColumn() == c) 
						{
							SudokuCell sudokuCell = new SudokuCell(r,c, cell.getValue());
							sudokuCell.setLayoutX(x);
								
							if(c == 2 || c == 5)
								x += 50;
							else
								x += 40;
	
							sudokuCell.setLayoutY(y);
							sudokuCells.add(sudokuCell);
						}
					}
				}
				if(r == 2 || r == 5)
					y += 45;
				else
					y += 38;
			}
			pane.getChildren().addAll(sudokuCells);
		}
		else {
			for(SudokuCell sudokuCell : sudokuCells) 
			{
				sudokuCell.hideContent();
				for(Cell cell : cells) 
				{
					if(cell.getRow() == sudokuCell.getRow() && cell.getColumn() == sudokuCell.getColumn()) 
					{
						sudokuCell.setValue(cell.getValue());
					}
				}
			}
		}
		
		while(cellToShow > 0) 
		{
			for(SudokuCell cell : sudokuCells) 
			{
				if(cellToShow > 0 && cell.isHide()) 
				{
					boolean bool = new Random().nextBoolean();
					if(bool) 
					{
						cell.showContent();
						cellToShow -= 1;
					}
				}
			}
		}
		
		setStartGrid(sudokuCells);
	}	

	public void setDifficulty(DIFFICULTY diff) {
		this.difficulty = diff;
	}
	
	private void loadGrid() 
	{
		int x;
		int y = 130;
		
		for(int r = 0; r < 9; r++) 
		{
			x = 190;
			for (int c = 0; c < 9; c++) 
			{
				for(SudokuCell cell : sudokuCells) 
				{
					cell.setScaleX(1.2);
					cell.setScaleY(1.2);
					if(cell.getRow() == r && cell.getColumn() == c) 
					{
						if(cell.isHide())
							cell.hideContent();
						else
							cell.showContent();
						
						cell.setLayoutX(x);
							
						if(c == 2 || c == 5)
							x += 50;
						else
							x += 40;

						cell.setLayoutY(y);
					}
				}
			}
			if(r == 2 || r == 5)
				y += 45;
			else
				y += 38;
		}
		pane.getChildren().addAll(sudokuCells);		
		setStartGrid(sudokuCells);
	}

	
	public ArrayList<SudokuCell> getStartGrid() {
		return startGrid;
	}
	
	public void setStartGrid(ArrayList<SudokuCell> startGrid) {
		this.startGrid = startGrid;
	}

	public Stage getStage() {
		return stage;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

}
