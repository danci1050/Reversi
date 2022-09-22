package application;

import java.io.*;
import java.util.*;

import jdk.jshell.execution.StreamingExecutionControl;


/**
 * This class represents the Computer player which is using minimax to find the best move
 * @author Daniel Nagy
 * @version 1.0
 *
 */
public class Computer {
	/**
	 * This is the default constructor for Computer
	 */
	public Computer(){
		
	}
	
	
	
	private int[] bestMove = { 0, 0 };
	/**
	 * This method finds all the possible moves for the ai and calls the minimax method on them to find the best possible move
	 * @param board The board to find the best move on
	 * @param depth The depth of the search for the minimax algorithm
	 * @param maximizingPlayer This stores who if the maximizing  player is the one that called the method
	 * @return The best move for the ai
	 */
	public int[] returnBestMove(Tile[][] board, int depth, boolean maximizingPlayer) {
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		double bestScore = Integer.MIN_VALUE;
		Reversi reversi = new Reversi();
		List<List<Integer>> validMoves = reversi.showValidMoves(board, Reversi.getAisColor(), true);
		for (int i = 0; i < validMoves.size(); i++) {
			Piece newpiece = null;
			Tile[][] new_board = copyBoard(board);
			List<Integer> move = validMoves.get(i);
			newpiece = Reversi.makePiece(reversi.getAisColor(), move.get(0), move.get(1));
			new_board[move.get(0)][move.get(1)].setPiece(newpiece);
			reversi.flip(move.get(0), move.get(1), false, new_board, Reversi.getAisColor());
			double score = minimax(new_board, 4, false, alpha, beta);
			if (score > bestScore) {
				bestScore = score;
				bestMove[0] = move.get(0);
				bestMove[1] = move.get(1);
			}
		}
		return bestMove;
	}
/**
 * This method makes a copy of a board
 * @param board The board to be copied
 * @return The copy of the board
 */
	public Tile[][] copyBoard(Tile[][] board) {
		Tile[][] new_board = new Tile[8][8];
		for (int i = 0; i < board.length; i++) {

			for (int j = 0; j < board[i].length; j++) {

				int xcord = board[i][j].getXcord();
				int ycord = board[i][j].getYcord();
				Tile copytile = new Tile(xcord, ycord);
				if (board[i][j].hasPiece()) {
					Piece copypiece = new Piece(board[i][j].getPiece().getType(), i, j);
					copytile.setPiece(copypiece);

				}
				new_board[i][j] = copytile;
			}

		}
		return new_board;
	}
	/**
	 * This is the  minimax  for the ai
	 * @param board This is the node
	 * @param depth This is the depth of the search
	 * @param isMaximizingPlayer Who calls the function
	 * @param alpha The alpha value for alpha-beta pruning
	 * @param beta The  beta value for alpha-beta pruning
	 * @return The score of the node
	 */
	public double minimax(Tile[][] board, int depth, boolean isMaximizingPlayer, double alpha, double beta) {
		Reversi reversi = new Reversi();
	
		int remainingMoves = 0;
		if (Reversi.getTurn() % 2 == 0) {
			remainingMoves = reversi.showValidMoves(board,Reversi.getAisColor(), true).size();
		} else {
			remainingMoves = reversi.showValidMoves(board, Reversi.getAisColor(), true).size();
		}
		double value = 0;

		if (depth == 0 || remainingMoves == 0) {
			return evaluate(board);
		}

		if (isMaximizingPlayer) {
			double bestVal = Double.NEGATIVE_INFINITY;

			List<List<Integer>> validMoves = reversi.showValidMoves(board, Reversi.getAisColor(), true);
			for (int i = 0; i < validMoves.size(); i++) {
				Piece newpiece = null;
				Tile[][] new_board = copyBoard(board);
				List<Integer> move = validMoves.get(i);
			System.out.println(move);
				newpiece = Reversi.makePiece(reversi.getAisColor(), move.get(0), move.get(1));
				new_board[move.get(0)][move.get(1)].setPiece(newpiece);
				reversi.flip(move.get(0), move.get(1), false, new_board, Reversi.getAisColor());
				value = minimax(new_board, depth - 1, !isMaximizingPlayer, alpha, beta);
				if (value > bestVal) {
					bestVal = value;
				}
				alpha = Math.max(alpha, bestVal);
				if (beta <= alpha) {
					break;
				}

			}
			return bestVal;

		} else {
			double bestVal = Double.POSITIVE_INFINITY;

			List<List<Integer>> validMoves = reversi.showValidMoves(board, Reversi.getPlayersColor(), true);
			for (int i = 0; i < validMoves.size(); i++) {
				Piece newpiece = null;
				Tile[][] new_board = copyBoard(board);
				List<Integer> move = validMoves.get(i);
				newpiece = Reversi.makePiece(reversi.getPlayersColor(), move.get(0), move.get(1));
				new_board[move.get(0)][move.get(1)].setPiece(newpiece);
				reversi.flip(move.get(0), move.get(1), false, new_board, Reversi.getPlayersColor());

				value = minimax(new_board, depth - 1, !isMaximizingPlayer, alpha, beta);
				if (value < bestVal) {
					bestVal = value;

				}
				beta = Math.min(beta, bestVal);
				if (beta <= alpha) {
					break;
				}

			}
			return bestVal;
		}

	}
	/**
	 * This is the heuristic function for the minimax algorithm
	 * @param board The board to be evaluated
	 * @return The score of the board
	 */
	public int evaluate(Tile[][] board) {
		Reversi reversi = new Reversi();
		int playesrsPieces = 0; //number of player pieces
		int aisPieces = 0; //number of ai pieces
		//board values source:http://samsoft.org.uk/reversi/strategy.htm
		int[][] boardvalues = { { 500, -8, 8, 6, 6, 8, -8, 500 }, { -8, -24, -4, -3, -3, -4, -24, -8 },
				{ 8, -4, 7, 4, 4, 7, -4, 8 }, { 6, -3, 4, 0, 0, 4, -3, 6 }, { 6, -3, 4, 0, 0, 4, -3, 6 },
				{ 8, -4, 7, 4, 4, 7, -4, 8 }, { -8, -24, -4, -3, -3, -4, -24, -8 }, { 500, -8, 8, 6, 6, 8, -8, 500 } }; 


		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j].hasPiece()) {
					if (board[i][j].getPiece().getType() == Reversi.getAisColor()) {
						aisPieces += 5 + boardvalues[i][j];
					} else {
						playesrsPieces += boardvalues[i][j] + 5;
					}
				}

			}


		}

		return aisPieces - playesrsPieces;
	}
}
