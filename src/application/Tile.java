package application;

import java.io.Serializable;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
/**
 * This class is an imlementation of a tile.
 * @author Daniel Nagy
 * @version 1.0
 *
 */
public class Tile extends Rectangle implements Serializable {
	private int xcord = -1;
	private int ycord = -1;
	private Piece piece;

	/**
	 * This method returns the x coordinate of the tile
	 * @return The x coordinate of the tile
	 */
	public int getXcord() {
		return xcord;
	}

	/**
	 * This method sets the x coordinate of the tile
	 * @param xcord The new x coordinate of the tile
	 */
	public void setXcord(int xcord) {
		this.xcord = xcord;
	}

	/**
	 * This method returns the y coordinate of the tile
	 * @return The y coordinate of the tile
	 */
	public int getYcord() {
		return ycord;
	}

	/**
	 * This method returns the y coordinate of the tile
	 * @param ycord The new y coordinate of the tile
	 */
	public void setYcord(int ycord) {
		this.ycord = ycord;
	}
	/**
	 * This method checks if there is a piece placed on the tile
	 * @return Returns true if there is a piece placed on the tile
	 */
	public boolean hasPiece() {
		return piece != null;
	}

	/**
	 * This method returns the piece placed on the tile
	 * @return This is the piece placed on the tile
	 */
	public Piece getPiece() {
		return piece;
	}

	/**
	 * This method sets the piece placed on the tile
	 * @param piece This is the new piece placed on the tile
	 */
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	/**
	 * This is the default constructor for the Tile
	 * @param x x coordinate of the tile
	 * @param y y coordinate of the tile
	 */
	public Tile(int x, int y) {
		setWidth(Reversi.tileSize);
		setHeight(Reversi.tileSize);
		relocate(x * Reversi.tileSize, y * Reversi.tileSize);
		setFill(Color.valueOf("#feb"));
		setStroke(Color.BLACK);
		setStrokeType(StrokeType.INSIDE);
		xcord = x;
		ycord = y;
	}
}
