package application;

import java.io.Serializable;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
/**
 * This class represent a piece.
 * @author danci
 * @version 1.0
 */
public class Piece extends StackPane implements Serializable {
	/**
	 * This is the type of the piece(Black/White)
	 */
	public PieceType type;

	/**
	 * This method returns the piece's type
	 * @return The type of the piece
	 */
	public PieceType getType() {
		return type;
	}

	/**
	 * This method sets the type of a piece
	 * @param type The new type of the piece
	 */
	public void setType(PieceType type) {
		this.type = type;
	}
	/**
	 * This is the constructor of the piece
	 * @param type This is the type of the piece
	 * @param x This is the x coordinate of the piece
	 * @param y This is the y coordinate of the piece
	 */
	public Piece(PieceType type, int x, int y) {
		relocate(x * Reversi.tileSize, y * Reversi.tileSize);
		this.type = type;
		Circle circle = new Circle();
		circle.setRadius(Reversi.tileSize * 0.40);
		circle.setFill(type == PieceType.BLACK ? Color.BLACK : Color.WHITE);
		circle.setTranslateX((Reversi.tileSize - Reversi.tileSize * 0.40 * 2) / 2);
		circle.setTranslateY((Reversi.tileSize - Reversi.tileSize * 0.40 * 2) / 2);
		getChildren().addAll(circle);

	}
}
