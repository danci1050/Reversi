package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class is an implementation of the board game reversi contains the main menu and the all the game mechanics of the game
 * @author Daniel Nagy
 * @version 1.0
 *
 */
public class Reversi extends Application {
	/**
	 * tileSize This is the size of a tile in pixels
	 */
	public static final int tileSize = 100;
	/**
	 *  width This is the number of tiles in row
	 */
	public static final int width = 8;
	/**
	 *  width This is the number of tiles in column
	 */
	public static final int height = 8;
	/**
	 *  tileGroup This group stores the tiles
	 */
	private Group tileGroup = new Group();
	/**
	 *  tileGroup This group stores the pieces
	 */
	private Group pieceGroup = new Group();
	/**
	 *  This is the board of the game
	 */
	private Tile[][] board = new Tile[width][height];
	/**
	 *  This stores the number of number of turns
	 */
	private static int turn = -4;
	/**
	 * This stores the player's color
	 */
	private static PieceType playerscolor = PieceType.BLACK;
	/**
	 * This stores the ai's color
	 */
	private static PieceType aiscolor = PieceType.WHITE;
/**
 * This stores which color begins the game
 */
	private static PieceType whosTurn = PieceType.WHITE;
	/**
	 * If the ai is enabled or not
	 */
	private static boolean ai = true;

	/**
	 * Returns if the ai enabled or not
	 * 
	 * @return the ai
	 */
	public static boolean isAi() {
		return ai;
	}

	/**
	 * Ths method enables/disables the ai
	 * 
	 *  @param ai new value of the ai
	 */
	public static void setAi(boolean ai) {
		Reversi.ai = ai;
	}

	/**
	 * This method returns the players color
	 * 
	 *  @return the playersColor The color of the player's pieces
	 */
	public static PieceType getPlayersColor() {
		return playerscolor;
	}

	/**
	 * This method sets the player's color
	 * 
	 *  @param playersColor The new color of the player's pieces
	 */
	public static void setPlayersColor(PieceType playersColor) {
		playerscolor = playersColor;
	}

	/**
	 * This method returns the Aiscolor
	 * 
	 * @return the aisColor The color of the ai's pieces
	 */
	public static PieceType getAisColor() {
		return aiscolor;
	}

	/**
	 * This method sets the AisColor
	 * 
	 * @param aisColor The new color of the ai's pieces
	 */
	public static void setAisColor(PieceType aisColor) {
		aiscolor = aisColor;
	}

	/**
	 * This method returns the game board
	 * 
	 * @return The board
	 */
	public Tile[][] getBoard() {
		return board;
	}

	/**
	 * This method sets the game board
	 * 
	 * @param board The new board
	 */
	public void setBoard(Tile[][] board) {
		this.board = board;
	}

	/**
	 * This method returns the turn
	 * 
	 * @return This is the number of the turns of the game
	 */
	public static int getTurn() {
		return turn;
	}

	/**
	 * This method sets the turn of the game and it also calls the ai method, and
	 * makes the call to announce the winner.
	 * 
	 * @param turn The new value of the number of turns
	 */
	public void setTurn(int turn) {

		this.turn = turn; // sets the turn value

		/*
		 * Updates who's turn it is
		 */
		if (whosTurn == playerscolor && turn != 0) {
			whosTurn = aiscolor;
		} else if (turn != 0) {
			whosTurn = playerscolor;
		}

		/*
		 * Clears the possible moves from the board
		 */
		clearValidMoves();

		/*
		 * Displays the valid moves
		 */
		if (whosTurn == playerscolor) {
			showValidMoves(board, playerscolor, false);
		} else {
			showValidMoves(board, aiscolor, false);
		}

		/*
		 * Calls the ai
		 */
		ai();

		if (showValidMoves(board, aiscolor, true).size() == 0 && aiscolor == whosTurn
				&& turn < 96 && turn>4) {
			setTurn(getTurn() + 1);// adds one to the number of turns if player2 can not
									// make a valid move
		}
		if (showValidMoves(board, playerscolor, true).size() == 0 && playerscolor == whosTurn
				&& turn < 96 && turn>4) {
			setTurn(getTurn() + 1);// adds one to the number of turns if player1 can not make a
									// valid move
		}
		/*
		 * Calls for the annunciation of the winner
		 */
		System.out.println(turn);
		if (turn == 96) {
			if (piecesOnBoard(board)[0] > piecesOnBoard(board)[1]) {
				annaunceWinner("black");
			} else {
				annaunceWinner("white");
			}

		}

	}

	public Reversi() {

	}

	/**
	 * This method calls the minimax function from the computer class to find the
	 * best move for the ai
	 */
	public void ai() {
		/*
		 * Checks if the ai is enabled or not
		 */
		if (ai) {
			Computer ai = new Computer();
			int[] bestMove; // stores the ai's move
			if (whosTurn == aiscolor && showValidMoves(board, aiscolor, true).size() != 0) {
				bestMove = ai.returnBestMove(board, 5, true); // calls minimax
				Piece newpiece = null;

				if (bestMove != null) {

					newpiece = makePiece(getAisColor(), bestMove[0], bestMove[1]);// creates a piece on the
																					// board
					board[(int) bestMove[0]][bestMove[1]].setPiece(newpiece);
					pieceGroup.getChildren().add(newpiece); // adds the piece to the pieve group
					flip((int) bestMove[0], bestMove[1], false, board, aiscolor); // flips the pieces after the
																					// ai's move
					setTurn(getTurn() + 1); // add one to the turn (recursive)

				}
			}

		} 
	}

	/**
	 * creates a new pieces according to the input parameters
	 * 
	 * @param type The type of the piece (Black/White)
	 * @param x    The x position of the piece
	 * @param y    The y position of the piece
	 * @return The created piece
	 */
	public static Piece makePiece(PieceType type, int x, int y) {
		Piece piece = new Piece(type, x, y);
		return piece;
	}

	/**
	 * This method creates the scene where the game is displayed
	 * @param primaryStage This is the stage where the game elements will be placed
	 * @return The game scene
	 */
	private Parent createContent(Stage primaryStage) {
		
		Pane root = new Pane();
		Text blackPiecesCount = new Text("2"); // number of black pieces on board on the begging of the game
		Text whitePiecesCount = new Text("2");// number of white pieces on board on the begging of the game
		blackPiecesCount.setStyle("-fx-font: 45px Cambria");// styling for blackPiecesCount (sets font size to 45px and
															// font family to Cambria)
		whitePiecesCount.setStyle("-fx-font: 45px Cambria");// styling for whitePiecesCount (sets font size to 45px and
															// font family to Cambria)
		whitePiecesCount.setFill(Color.WHITE); // font color for whitePiecesCount
		blackPiecesCount.setFill(Color.WHITE);// font color for blackPiecesCount
		/*
		 * Positioning of whitePiecesCount
		 */
		whitePiecesCount.setLayoutY(135);
		whitePiecesCount.setLayoutX(880);
		/*
		 * Positioning of blackPiecesCount
		 */
		blackPiecesCount.setLayoutX(880);
		blackPiecesCount.setLayoutY(70);
		root.getChildren().addAll(blackPiecesCount, whitePiecesCount); // adds the score counter to root to be displayed
		root.setPrefSize(width * tileSize + 200, height * tileSize); // sets the size of the scene
		root.getChildren().addAll(tileGroup, pieceGroup);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Piece piece = null;

				Tile tile = new Tile(x, y); // creates the tiles

				tile.setOnMouseClicked(
						(EventHandler<? super MouseEvent>) new EventHandler<javafx.scene.input.MouseEvent>() { 
							@Override
							public void handle(javafx.scene.input.MouseEvent e) {
								Piece newpiece = null;
								if (whosTurn == aiscolor && !tile.hasPiece()
										&& validMove(tile.getXcord(), tile.getYcord(), board, aiscolor)) { 
									if (!ai) {// checks if the ai is enabled
										newpiece = makePiece(getAisColor(), tile.getXcord(), tile.getYcord());
										tile.setPiece(newpiece);
										pieceGroup.getChildren().add(newpiece);
										flip(tile.getXcord(), tile.getYcord(), false, board, aiscolor);
										setTurn(getTurn() + 1);// adds one to the number of turns after player1 made its move
																
										

									}

								} else if (whosTurn == playerscolor && !tile.hasPiece()
										&& validMove(tile.getXcord(), tile.getYcord(), board, playerscolor)) {
									newpiece = makePiece(getPlayersColor(), tile.getXcord(), tile.getYcord());
									tile.setPiece(newpiece);
									pieceGroup.getChildren().add(newpiece);
									flip(tile.getXcord(), tile.getYcord(), false, board, playerscolor);
									setTurn(getTurn() + 1); // adds one to the number of turns after player1 made its move
															
								}
								

								blackPiecesCount.setText(String.valueOf(piecesOnBoard(board)[0]));
								whitePiecesCount.setText(String.valueOf(piecesOnBoard(board)[1]));
								root.getChildren().remove(whitePiecesCount);
								root.getChildren().add(whitePiecesCount);

							}
						});
				if (board[x][y] != null) {
					tile.setPiece(board[x][y].getPiece());

				}
				board[x][y] = tile;
				tileGroup.getChildren().add(tile);

				if (board[x][y].hasPiece()) {

					if (board[x][y].getPiece().getType() == getPlayersColor()) {
						turn += 1;
						piece = makePiece(getPlayersColor(), x, y);
					} else {
						turn += 1;
						piece = makePiece(getAisColor(), x, y);
					}
				}
				if (piece != null) {
					tile.setPiece(piece);
					pieceGroup.getChildren().add(piece);
				}
				Circle blackcounter = new Circle();
				blackcounter.setRadius(Reversi.tileSize * 0.20);
				blackcounter.setFill(Color.BLACK);
				blackcounter.setLayoutX(830);
				blackcounter.setLayoutY(60);

				Circle whitecounter = new Circle();
				whitecounter.setRadius(Reversi.tileSize * 0.20);
				whitecounter.setFill(Color.WHITE);
				whitecounter.setLayoutX(830);
				whitecounter.setLayoutY(120);

				Button save = new Button("Save");
				save.setLayoutX(830);
				save.setLayoutY(150);
				save.setStyle(
						"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;");
				save.setOnMouseEntered(e -> save.setStyle(
						"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;-fx-text-fill: white;"));
				save.setOnMouseExited(e -> save.setStyle(
						"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;"));
				save.setOnAction(e -> saveFile(board, fileChooser(primaryStage, true)));
				Button menu = new Button("Return to \n the Menu");
				menu.setLayoutX(790);
				menu.setLayoutY(200);
				menu.setStyle(
						"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;");
				menu.setOnMouseEntered(e -> menu.setStyle(
						"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;-fx-text-fill: white;"));
				menu.setOnMouseExited(e -> menu.setStyle(
						"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;"));

				menu.setOnAction(e -> backToMenu(primaryStage));

				root.getChildren().addAll(whitecounter, blackcounter, save, menu);
				root.setStyle("-fx-background-color: rgb(79,98,114)");

			}
		}
		if (whosTurn == aiscolor) {
			setTurn(getTurn());
			blackPiecesCount.setText(String.valueOf(piecesOnBoard(board)[0]));
			whitePiecesCount.setText(String.valueOf(piecesOnBoard(board)[1]));
			root.getChildren().remove(whitePiecesCount);
			root.getChildren().add(whitePiecesCount);
		}
		return root;
	}

	/**
	 * This method prompts the user to choose the new games properties
	 * 
	 * @param primaryStage The stage where the dialog box will be shown
	 */
	public void newGame(Stage primaryStage) {
		Dialog<ComboBox> gameSetup = new Dialog<>();
		gameSetup.setTitle("Game setup");

		ButtonType loginButtonType = new ButtonType("Start", ButtonData.OK_DONE);
		gameSetup.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		ComboBox colors = new ComboBox();
		colors.getItems().add("white");
		colors.getItems().add("black");
		colors.getSelectionModel().selectFirst();
		ComboBox opponents = new ComboBox();
		opponents.getItems().add("Human");
		opponents.getItems().add("Computer");
		opponents.getSelectionModel().selectFirst();

		ComboBox initiator = new ComboBox();
		
		initiator.getItems().add("white");
		initiator.getItems().add("black");
		initiator.getSelectionModel().selectFirst();

		grid.add(new Label("Choose Color:"), 0, 0);
		grid.add(colors, 1, 0);
		grid.add(new Label("Choose Opponent"), 0, 1);
		grid.add(opponents, 1, 1);
		grid.add(new Label("Choose who should begin"), 0, 2);
		grid.add(initiator, 1, 2);

		gameSetup.getDialogPane().setContent(grid);

		gameSetup.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				if (colors.getValue() == "white") {
					playerscolor = PieceType.WHITE;
					aiscolor = PieceType.BLACK;
				} else {
					aiscolor = PieceType.WHITE;
					playerscolor = PieceType.BLACK;
				}
				if (opponents.getValue() == "Human") {
					ai = false;
				} else {
					ai = true;
				}
				if (initiator.getValue() == "white") {
					whosTurn = PieceType.WHITE;
				} else {
					whosTurn = PieceType.BLACK;
				}

				loadFile("basicsetup.txt", primaryStage, true);
			}
			return null;
		});
		gameSetup.showAndWait();

	}
	/**
	 * This method displays the winner of the game
	 * @param winner The player who won the game
	 */
	public void annaunceWinner(String winner) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (winner.equals("black")) {

			alert.setTitle("Black Won");
			alert.setHeaderText(null);
			alert.setContentText("Black won the game!");
		} else {
			alert.setTitle("White won");
			alert.setHeaderText(null);
			alert.setContentText("White won the game!");
		}
		alert.showAndWait();
	}
	/**
	 * This method takes the user back to the menu
	 * @param primaryStage The stage where the menu is displayed
	 */
	public void backToMenu(Stage primaryStage) {
		ButtonType yes = new ButtonType("Yes");
		ButtonType no = new ButtonType("No");
		Alert wantToSave = new Alert(AlertType.CONFIRMATION);
		wantToSave.getButtonTypes().clear();
		wantToSave.getButtonTypes().addAll(yes, no);
		Scene menu = new Scene(createMenu(primaryStage, primaryStage.getScene()));
		if (turn < 128) {

			wantToSave.setTitle("Exit");
			wantToSave.setHeaderText(null);
			wantToSave.setContentText("Do you want to save the game before quitting?");
			Optional<ButtonType> option = wantToSave.showAndWait();

			if (option.get() == yes) {
				saveFile(board, fileChooser(primaryStage, true));
				primaryStage.setScene(menu);

			} else {
				primaryStage.setScene(menu);
			}

		} else {
			primaryStage.setScene(menu);
		}
	}

	/**
	 * Counts the number of white and black pieces on the board
	 * 
	 * @param board The board where the piecies are counted
	 * @return The number of white and black pieces
	 */
	public static int[] piecesOnBoard(Tile[][] board) {
		int[] numberofpieces = { 0, 0 };
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j].hasPiece()) {
					if (board[i][j].getPiece().getType() == aiscolor) {
						numberofpieces[0] = numberofpieces[0] + 1;
					} else {
						numberofpieces[1] += 1;
					}
				}
			}
		}
		return numberofpieces;
	}
	/**
	 * Shows a window to the user to choose a file
	 * @param primarStage Where the file chooser will be displayed
	 * @param save If the user is saving something
	 * @return The path that the user choose
	 */
	public String fileChooser(Stage primarStage, boolean save) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Othello save");
		if (save) {
			File file = fileChooser.showSaveDialog(primarStage);
			return file.getPath();
		} else {
			File file = fileChooser.showOpenDialog(primarStage);
			return file.getPath();
		}

	}

	/**
	 * This method creates the menu scene
	 * 
	 * @param primaryStage The stage where the menu will be shown
	 * @param scene        The game scene
	 * @return The menu scene
	 */
	private Parent createMenu(Stage primaryStage, Scene scene) {
		Pane root = new Pane();
		Canvas background = new Canvas(1000, 800);
		root.getChildren().add(background);
		root.setStyle("-fx-background-color: rgb(79,98,114)");

		Text reversitext = new Text("Reversi");
		reversitext.setLayoutX(250);
		reversitext.setLayoutY(350);
		reversitext.setFill(Color.WHITE);
		reversitext.setStyle("-fx-font: 150px Cambria");

		Button start = new Button("New Game");
		start.setStyle(
				"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;");
		start.setOnMouseEntered(e -> start.setStyle(
				"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;-fx-text-fill: white;"));
		start.setOnMouseExited(e -> start.setStyle(
				"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;"));
		start.setLayoutX(380);
		start.setLayoutY(400);
		start.setOnAction(e -> newGame(primaryStage));
		Button load = new Button("Load Game");
		load.setStyle(
				"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;");
		load.setOnMouseEntered(i -> load.setStyle(
				"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;-fx-text-fill: white;"));
		load.setOnMouseExited(i -> load.setStyle(
				"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;"));
		load.setLayoutX(380);
		load.setLayoutY(460);
		load.setOnAction(e -> loadFile(fileChooser(primaryStage, false), primaryStage, false));

		Button rules = new Button("Rules");
		rules.setStyle(
				"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;");
		rules.setOnMouseEntered(i -> rules.setStyle(
				"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;-fx-text-fill: white;"));
		rules.setOnMouseExited(i -> rules.setStyle(
				"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;"));
		rules.setLayoutX(420);
		rules.setLayoutY(520);
		rules.setOnAction(e -> getHostServices().showDocument("https://www.yourturnmyturn.com/rules/reversi.php"));

		Button exit = new Button("Exit");
		exit.setStyle(
				"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;");
		exit.setOnMouseEntered(j -> exit.setStyle(
				"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;-fx-text-fill: white;"));
		exit.setOnMouseExited(j -> exit.setStyle(
				"-fx-font: 40px Cambria; -fx-border-color: transparent;-fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: transparent;"));
		exit.setLayoutX(430);
		exit.setLayoutY(580);
		exit.setOnAction(e -> primaryStage.close());
		root.getChildren().addAll(start, reversitext, load, exit, rules);

		return root;
	}

	/**
	 * This method creates a save file
	 * 
	 * @param board    The board which is saved
	 * @param fileName The name of the save file
	 */
	public static void saveFile(Tile[][] board, String fileName) {

		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(whosTurn);
			out.writeObject(aiscolor);
			out.writeObject(playerscolor);
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					out.writeObject(board[i][j]);
				}

			}
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method loads a save file
	 * 
	 * @param fileLocation The location of the save file
	 * @param primaryStage The stage where the file chooser will be shown
	 * @param newGame      If the loaded file is the base setup of the game
	 */
	public void loadFile(String fileLocation, Stage primaryStage, boolean newGame) {

		try {
			FileInputStream fileIn = new FileInputStream(fileLocation);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			if (newGame) {
				in.readObject();
				in.readObject();
				in.readObject();
			} else {
				whosTurn = (PieceType) in.readObject();
				aiscolor = (PieceType) in.readObject();
				playerscolor = (PieceType) in.readObject();
			}
			pieceGroup = new Group();
			for(int i=0; i<board.length;i++) {
				for(int j=0; j<board.length;j++) {
					System.out.println("run");
					board[i][j]=null;
				}
			}
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					board[i][j] = (Tile) in.readObject();
				}

			}
			fileIn.close();
			in.close();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		}
		
		Scene scene = new Scene(createContent(primaryStage));
		primaryStage.setScene(scene);
		showValidMoves(board, whosTurn, false);

	}

	/**
	 * Gets a piece on the board with the inputed coordinates
	 * 
	 * @param x     The x coordinate of the piece
	 * @param y     The y coordinate of the piece
	 * @param board The board where the piece is placed
	 * @return The piece with the matching x and y coordinates
	 */
	public Piece getPiece(int x, int y, Tile[][] board) {
		return board[x][y].getPiece();
	}

	/**
	 * This is the method initiates the scenes and shows the primaryStatge
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene2;

		Scene scene = new Scene(createContent(primaryStage));

		scene2 = new Scene(createMenu(primaryStage, scene));

		primaryStage.setTitle("Reversi");
		primaryStage.setScene(scene2);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * Changes the color of the piece when the piece is flipped
	 * 
	 * @param xcoordinate the x coordinate of the piece being flipped
	 * @param ycoordinate The y coordinate of the piece being flipped
	 * @param board       The board where the changes are made
	 */
	public void changeColor(int xcoordinate, int ycoordinate, Tile[][] board) {
		Piece piece = null;
		Circle circle = new Circle();
		if (board[xcoordinate][ycoordinate].hasPiece()) {

			piece = getPiece(xcoordinate, ycoordinate, board);
			if (whosTurn == getPlayersColor()) {

				piece.setType(getPlayersColor());
				circle = (Circle) piece.getChildren().get(0);
				if (playerscolor == PieceType.WHITE) {
					circle.setFill(Color.WHITE);
				} else {
					circle.setFill(Color.BLACK);
				}
				piece.getChildren().clear();
				piece.getChildren().addAll(circle);
			} else {
				piece.setType(getAisColor());
				circle = (Circle) piece.getChildren().get(0);
				if (aiscolor == PieceType.WHITE) {
					circle.setFill(Color.WHITE);
				} else {
					circle.setFill(Color.BLACK);
				}
				piece.getChildren().clear();
				piece.getChildren().addAll(circle);
			}
		}
	}

	/**
	 * This method checks if there is a piece with a specific color on the tile
	 * 
	 * @param x        x coordinate of the tested tile
	 * @param y        y coordinate of the tested tile
	 * @param board    the board where the testing is made
	 * @param whosTurn The color which is tested
	 * @return If there is a piece with the right color on the tile
	 */
	public boolean checkForNextPiecePosition(int x, int y, Tile[][] board, PieceType whosTurn) {
		Piece piece = null;
		if (board[x][y].hasPiece()) {
			piece = getPiece(x, y, board);
			if (whosTurn == getPlayersColor()) {
				if (piece.getType() == getPlayersColor()) {

					return (true);
				}
			} else {

				if (piece.getType() == getAisColor()) {

					return (true);
				}
			}
		}
		return (false);
	}

	/**
	 * This method makes the checks for the flipping of the pieces
	 * 
	 * @param x    The x coordinate of the last placed piece
	 * @param y		The y coordinate of the last placed piece
	 * @param xChange The change in x for the direction to check
	 * @param yChage  The change in y for the direction to check
	 * @param test If the flipping is a test move or not
	 * @param board The board where the pieces are turned
	 * @param whosTurn The color which made the last move
	 * @return The number of pieces flipped
	 */
	public int flipPieces(int x, int y, int xChange, int yChage, boolean test, Tile[][] board, PieceType whosTurn) {
		int piecesFlipped = 0;
		boolean turn = false;
		boolean emptytile = true;
		int xcheck = x;
		int ycheck = y;
		while (xcheck + xChange <= 7 && ycheck + yChage <= 7 && xcheck + xChange >= 0 && ycheck + yChage >= 0) {

			xcheck += xChange;
			ycheck += yChage;
			if (!board[xcheck][ycheck].hasPiece()) {
				emptytile = false;
			}
			if (checkForNextPiecePosition(xcheck, ycheck, board, whosTurn) && emptytile) {
				turn = true;

			}

		}
		if (turn) {
			while (x + xChange <= 7 && y + yChage <= 7 && x + xChange >= 0 && y + yChage >= 0) {
				x += xChange;
				y += yChage;

				if (checkForNextPiecePosition(x, y, board, whosTurn) || !board[x][y].hasPiece()) {

					break;
				} else {
					if (!test) {

						changeColor(x, y, board);
					}
					piecesFlipped += 1;

				}
			}
		}
		return piecesFlipped;
	}

	/**
	 * This method calls flipPieces horizontally vertically diagonally
	 * 
	 * @param xcoordinate This is the x coordinate of the placed piece
	 * @param ycoordinate This is the y coordinate of the placed piece
	 * @param test        This is for testing if piece is placed would it flip any
	 *                    other piece
	 * @param board       This is the board where the pieces will be flipped
	 * @param whoisnext   This is the player who placed the piece
	 * @return The number of pieces flipped with the move
	 */
	public int flip(int xcoordinate, int ycoordinate, boolean test, Tile[][] board, PieceType whoisnext) {
		int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } };
		int piecesFlipped = 0;
		for (int i[] : directions) {
			piecesFlipped += flipPieces(xcoordinate, ycoordinate, i[0], i[1], test, board, whoisnext);
		}
		return piecesFlipped;
	}

	/**
	 * This method checks if a moved is valid or not
	 * 
	 * @param x        The x coordinate of the tested move
	 * @param y        The y coordinate of the tested move
	 * @param board    The board where the move is tested
	 * @param whosTurn The player who makes the move
	 * @return The possibility of the move
	 */
	public boolean validMove(int x, int y, Tile[][] board, PieceType whosTurn) {
		int piecesFlipped = 0;
		piecesFlipped = flip(x, y, true, board, whosTurn);
		if (piecesFlipped > 0) {
			return true;
		}

		return false;
	}

	/**
	 * Checks the board for the move
	 * 
	 * @param board    Where the moves are tested
	 * @param whosTurn The player who makes the move
	 * @param test     If the board has to be colored
	 * @return A list of valid moves
	 */
	public List<List<Integer>> showValidMoves(Tile[][] board, PieceType whosTurn, boolean test) {
		List<List<Integer>> validMoves = new ArrayList<>();
		ArrayList<Integer> cordinate = new ArrayList<Integer>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {

				if (validMove(i, j, board, whosTurn) && !board[i][j].hasPiece() && board[i][j] != null) {
					cordinate.add(i);
					cordinate.add(j);
					validMoves.add(cordinate);
					cordinate = new ArrayList<Integer>();
					if (!test) {
						board[i][j].setFill(Color.GREEN);
					}

				}
			}
		}
		return validMoves;
	}

	/**
	 * This method sets all the tiles to the same color
	 */
	public void clearValidMoves() {
		Tile tile = null;
		for (int i = 0; i < tileGroup.getChildren().size(); i++) {
			tile = (Tile) tileGroup.getChildren().get(i);
			tile.setFill(Color.valueOf("#feb"));
			tileGroup.getChildren().set(i, tile);

		}
	}

	/**
	 * This is the main method launches the application
	 * @param args cmd commands
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
