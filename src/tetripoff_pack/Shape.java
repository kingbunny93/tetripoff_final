package tetripoff_pack;

import java.util.Random;


/**
 * The Shape class creates Tetrominoes or the blocks to be placed on the array grid of the board.
 * It contains the coordinates of the shape which include it's current X and Y position which allow the
 * pieces to be rotated left and right. This would've been a nightmare to figure out on my own.
 * It also uses random to randomly generate pieces. This can be advanced more to ensure that
 * a Straight Line Block will appear more frequently.
 * that is in the off chance that it still might not appear after 1000 blocks. lol that would suck.
 */
public class Shape {
    protected enum Tetrominoe { NoShape, RSShape, SShape, LineShape,
        TShape, SquareShape, LShape, RLShape }

    private Tetrominoe pieceShape;
    private int coords[][];
    private int[][][] coordsTable;

    /** the Shape constructor's purpose is to call the initShape method which,
     * as the name implies, initializes the shape*/
    public Shape() {
        initShape();
    }

    /**
     * this method initializes the shapes based on the enumeration of the block order.
     */
    private void initShape() {
        coords = new int[4][2];
        coordsTable = new int[][][] {
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },   // no block
                { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },  // RS block
                { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },   // S Block
                { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },   // Straight block
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },   // T block
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },   // Square block
                { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },   // L block
                { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }    // RL block
        };
        setShape(Tetrominoe.NoShape);
    }

    /**
     * method called by initShape which sets the shape in relation to the board.
     * @param shape
     */
    protected void setShape(Tetrominoe shape) {
        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = coordsTable[shape.ordinal()][i][j];
            }
        }
        pieceShape = shape;
    }
    /** setter for x coord */
    private void setX(int index, int x) {
        coords[index][0] = x;
    }
    /** setter for y coord */
    private void setY(int index, int y) {
        coords[index][1] = y;
    }
    /** getter for x coord */
    public int x(int index) {
        return coords[index][0];
    }
    /** getter for x coord */
    public int y(int index) {
        return coords[index][1];
    }
    /** getter for y coord */
    public Tetrominoe getShape() {
        return pieceShape;
    }
    /** this is the randomizer that allows each piece to be randomized. Again, this can be
     * improved to ensure that the Line piece will eventually show up within a certain amount
     * of moves instead of infinitely random.
     */
    public void setRandomShape() {
        var r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Tetrominoe[] values = Tetrominoe.values();
        setShape(values[x]);
    }

    /** used to keep track of the minimum X coordinate of the piece. */
    public int minX() {
        int m = coords[0][0];
        for (int i=0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }
    /** used to keep track of the minimum Y coordinate of the piece. */
    public int minY() {
        int m = coords[0][1];
        for (int i=0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    /** Method to rotate the piece counter clock-wise. */
    public Shape rotateLeft() {
        if (pieceShape == Tetrominoe.SquareShape) {
            return this;
        }
        var result = new Shape();
        result.pieceShape = pieceShape;
        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }
    /** Method to rotate the piece clockwise. */
    public Shape rotateRight() {
        if (pieceShape == Tetrominoe.SquareShape) {
            return this;
        }
        var result = new Shape();
        result.pieceShape = pieceShape;
        for (int i = 0; i < 4; ++i) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }
}

