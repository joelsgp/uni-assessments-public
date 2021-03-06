public class GameMap {
    enum Tile {
        EMPTY,
        OBSTACLE,
        RESOURCE
    }

    private static final int INF = Integer.MAX_VALUE;

    /**
     * The board representation as a 2D array of int. A value of 0 means an
     * empty cell, a value of 1 means that the cell is occupied by an
     * obstacle, and a value of 2 means that a resource is contained within
     * that cell.
     */
    Tile[][] board;

    /**
     * The dimension fo the board.
     */
    int width, height;

    /**
     * Construct a game board given a 2D array of int. The content of the array
     * is not checked and therefore it could contain negative values. In addition,
     * the constructor does not throw an error if the parameter is null or if the
     * parameter is not a rectangular array, that is all rows have the same number
     * of columns.
     * 
     * @param board The 2D array used to initialise the board.
     */
    public GameMap(int[][] board) {
        this.width = board.length;
        this.height = board[0].length;
        this.board = new Tile[width][height];

        // BE CAREFULL OF THE REPRESENTATION USED HERE, the ROWS are
        // the Y-COORDINATES and the COLUMNS are the X-COORDINATES.
        // To get the value of the board at position (x=1, y=0) you
        // must make the call board[0][1].
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                this.board[c][r] = intToTile(board[r][c]);
            }
        }
    }

    /////////////// ADD YOUR CODE BELOW ///////////////
    private Tile intToTile(int tile) {
        switch (tile) {
            case 0:
                return Tile.EMPTY;
            case 1:
                return Tile.OBSTACLE;
            case 2:
                return Tile.RESOURCE;
            default:
                throw new IllegalArgumentException();
        }
    }

    private Position getClosestMatch(
        int[][] boardDistance, boolean[][] boardVisited,
        boolean visited, Tile tile, boolean match
    ) {
        Position closest = new Position(0, 0, INF);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int currentDistance = boardDistance[x][y];
                if (
                    boardVisited[x][y] == visited
                    && (board[x][y] == tile) == match
                    && currentDistance < closest.getDistance()
                ) {
                    closest.setX(x);
                    closest.setY(y);
                    closest.setDistance(currentDistance);
                }
            }
        }
        return closest;
    }

    private boolean allVisited(boolean[][] boardVisited) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!boardVisited[x][y] && board[x][y] != Tile.OBSTACLE) {
                    return false;
                }
            }
        }
        return true;
    }

    private Position dijkstra(int x, int y) {
        // Dijkstra's algorithm I guess
        // initialise distance and visited
        int[][] boardDistance = new int[width][height];
        boolean[][] boardVisited = new boolean[width][height];
        for (int y2 = 0; y2 < height; y2++) {
            for (int x2 = 0; x2 < width; x2++) {
                boardVisited[x2][y2] = false;
                boardDistance[x2][y2] = INF;
            }
        }
        boardDistance[x][y] = 0;
        int currentDistance = 1;
        
        // loop
        boolean someUnvisited = true;
        while (someUnvisited) {
            // process neighbours
            int[] offsets = {-1, 1};
            // some duplicated code huh. what ever
            for (int off : offsets){
                int x2 = x + off;
                if (!(0 <= x2 && x2 < width)) {
                    continue;
                }
                if (
                    !boardVisited[x2][y]
                    && board[x2][y] != Tile.OBSTACLE
                    && currentDistance < boardDistance[x2][y]
                ) {
                    boardDistance[x2][y] = currentDistance;
                }
            }
            for (int off : offsets){
                int y2 = y + off;
                if (!(0 <= y2 && y2 < height)) {
                    continue;
                }
                if (
                    !boardVisited[x][y2]
                    && board[x][y2] != Tile.OBSTACLE
                    && currentDistance < boardDistance[x][y2]
                ) {
                    boardDistance[x][y2] = currentDistance;
                }
            }
            // end duplicated code
            boardVisited[x][y] = true;

            // find the closest unvisited, check if done
            Position closestUnvisited = getClosestMatch(
                boardDistance, boardVisited, false, Tile.OBSTACLE, false
            );
            x = closestUnvisited.getX();
            y = closestUnvisited.getY();
            currentDistance = closestUnvisited.getDistance();
            if (currentDistance == INF || allVisited(boardVisited)) {
                someUnvisited = false;
            }
            currentDistance += 1;
        }

        // return closest
        Position closestResource = getClosestMatch(
            boardDistance, boardVisited, true, Tile.RESOURCE, true
        );
        if (closestResource.getDistance() == INF) {
            return null;
        }
        return closestResource;
    }

    public Position getClosestResource(int x, int y) {
        Position closest;

        Tile startTile = board[x][y];
        if (startTile == Tile.OBSTACLE) {
            closest = null;
        } else if (startTile == Tile.RESOURCE) {
            closest = new Position(x, y, 0);
        } else {
            closest = dijkstra(x, y);
        }

        return closest;
    }
}
