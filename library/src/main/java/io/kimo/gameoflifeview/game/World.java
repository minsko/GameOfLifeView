package io.kimo.gameoflifeview.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that represents the world of cells.
 */
public class World {
    private int width,height;
    private Cell[] cells;
    private Cell[][] board;
    private final Random rand;


    public static World genesis(int width, int height) {
        World w = new World(width, height, false);
        w.genesis();
        return w;
    }

    public World(int width, int height, boolean random) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[this.width*this.height];
        this.board = new Cell[this.width][this.height];
        if (random)
            this.rand = new Random();
        else
            this.rand = new Random(110100);
        setup();
    }

    public int getWidth() {return this.width;}
    public int getHeight() {return this.height;}
    public Cell[] getCells() {return this.cells;}
    public Cell[][] getBoard() {return this.board;}

    private void setup() {
        for(int i = 0; i < this.width; i++)
            for(int j = 0; j < this.height; j++)
                this.board[i][j] = new Cell(i,j,this.rand.nextBoolean());

        updateCells();
    }

    private void updateCells() {
        List<Cell> boardCells = new ArrayList<Cell>();

        for(int i = 0; i < this.width; i++)
            for(int j = 0; j < this.height; j++)
                boardCells.add(this.board[i][j]);

        this.cells = boardCells.toArray(new Cell[this.width*this.height]);
    }

    private void genesis() {
        for(int i = 0; i < this.width; i++)
            for(int j = 0; j < this.height; j++)
                revive(i, j);
    }

    public Cell get(int i, int j) {
        return this.board[i][j];
    }

    public void kill(int i, int j) {
        this.board[i][j].die();
    }

    public void revive(int i, int j) {
        this.board[i][j].reborn();
    }

    public Cell[] liveNeighboursOf(int i, int j) {
        List<Cell> liveNeighbours = new ArrayList<Cell>();

        //up-left
        if (i-1 >= 0 && j+1 <= this.height-1) {
            Cell neighbour = get(i-1, j+1);
            if(neighbour.isAlive)
                liveNeighbours.add(neighbour);
        }

        //up
        if(j+1 <= this.height-1) {
            Cell neighbour = get(i,j+1);
            if(neighbour.isAlive)
                liveNeighbours.add(neighbour);
        }

        //up-right
        if(i+1 <= this.width-1 && j+1 <= this.height-1) {
            Cell neighbour = get(i+1,j+1);
            if(neighbour.isAlive)
                liveNeighbours.add(neighbour);
        }

        //left
        if(i-1 >= 0) {
            Cell neighbour = get(i-1,j);
            if(neighbour.isAlive)
                liveNeighbours.add(neighbour);
        }

        //right
        if(i+1 <= this.width-1) {
            Cell neighbour = get(i+1,j);
            if(neighbour.isAlive)
                liveNeighbours.add(neighbour);
        }

        //down-left
        if(i-1 >= 0 && j-1 >= 0) {
            Cell neighbour = get(i-1,j-1);
            if(neighbour.isAlive)
                liveNeighbours.add(neighbour);
        }

        //down
        if(j-1 >= 0) {
            Cell neighbour = get(i,j-1);
            if(neighbour.isAlive)
                liveNeighbours.add(neighbour);
        }

        //down-right
        if(i+1 <= this.width-1 && j-1 >= 0) {
            Cell neighbour = get(i+1,j-1);
            if(neighbour.isAlive)
                liveNeighbours.add(neighbour);
        }

        return liveNeighbours.toArray(new Cell[liveNeighbours.size()]);
    }

    public void rotate() {

        List<Cell> futureLiveCells = new ArrayList<Cell>();
        List<Cell> futureDeadCells = new ArrayList<Cell>();

        for (Cell cell : this.cells) {
            Cell[] neighbours = liveNeighboursOf(cell.x, cell.y);

            if(rule1(cell,neighbours))
                futureDeadCells.add(cell);
            if(rule2(cell,neighbours))
                futureLiveCells.add(cell);
            if(rule3(cell, neighbours))
                futureDeadCells.add(cell);
            if(rule4(cell,neighbours))
                futureLiveCells.add(cell);
        }

        updateBoard(futureLiveCells,futureDeadCells);
        updateCells();

    }


    private void updateBoard(List<Cell> lives, List<Cell> deads) {
        for (Cell cell : lives)
            revive(cell.x, cell.y);
        for (Cell cell : deads)
            kill(cell.x,cell.y);
    }

    private boolean rule1(Cell c, Cell[] n) {
        if(c.isAlive && n.length < 2)
            return true;
        else
            return false;
    }

    private boolean rule2(Cell c, Cell[] n) {
        if(c.isAlive && (n.length == 3 || n.length == 2))
            return true;
        else
            return false;
    }

    private boolean rule3(Cell c, Cell[] n) {
        if(c.isAlive && n.length > 3)
            return true;
        else
            return false;
    }

    private boolean rule4(Cell c, Cell[] n) {
        if(!c.isAlive && n.length == 3)
            return true;
        else
            return false;
    }
}
