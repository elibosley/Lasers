import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LasersPTUI {

    /**
     * an empty cell
     */
    public final static char EMPTY = '.';
    /**
     * a cell occupied with a pillar that accepts any # of lasers
     */
    public final static char ANYPILLAR = 'X';
    /**
     * a cell occupied with a laser emitter
     */
    public final static char LASER = 'L';
    /**
     * a cell occupied with a laser beam
     */
    public final static char BEAM = '*';

    // OUTPUT CONSTANTS
    /**
     * A horizontal divider
     */
    public final static char HORI_DIVIDE = '-';
    /**
     * A vertical divider
     */
    public final static char VERT_DIVIDE = '|';

    private char[][] grid;
    private int width;
    private int height;

    /**
     * First constructor, creates a safe object when only given a safe, with no laser placements
     *
     * @param safeFile the safe file to parse in
     * @throws FileNotFoundException
     */
    public LasersPTUI(String safeFile) throws FileNotFoundException {

        Scanner in = new Scanner(new File(safeFile));

        width = Integer.parseInt(in.next());
        height = Integer.parseInt(in.next());

        grid = new char[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = in.next().charAt(0);
            }
        }
        in.close();

        System.out.println(this);
    }


    public void readInputFile(String input) throws Exception {
        Scanner in = new Scanner(new File(input));
        while (in.hasNextLine()) {
            String line = in.nextLine();

            parseCommand(line);
        }
    }

    public void parseCommand(String str) throws Exception {
        String[] line = str.split(" ");
        String command = line[0];
        if (command.equals("a") || command.equals("add")) {
            if (line.length != 3) {
                throw new Exception("Incorrect coordinates");
            }
            add(Integer.parseInt(line[1]), Integer.parseInt(line[2]));
        } else if (command.equals("d") || command.equals("display")) {
            display();
        } else if (command.equals("h") || command.equals("help")) {
            help();
        } else if (command.equals("q") || command.equals("quit")) {
            quit();
        } else if (command.equals("r") || command.equals("remove")) {
            if (line.length != 3) {
                throw new Exception("Incorrect coordinates");
            }
            remove(Integer.parseInt(line[1]), Integer.parseInt(line[2]));
        } else if (command.equals("v") || command.equals("verify")) {
            verify();
        } else {
            throw new Exception("Unknown Command: ", new Throwable(line[0]));
        }
    }

    /**
     * Verifies that the current location is valid and present in the grid
     * @param r the row
     * @param c the column
     * @return true if position is valid, false if not
     */
    public boolean checkCoords(int r, int c) {
        if (r < 0 || r >= height || c < 0 || c >= height) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Adds a laser given a row and a column, and draws the beams from that coordinate
     * @param r the row to add the laser to
     * @param c the column to add the laser to
     */
    public void add(int r, int c) {
        //If pillar or laser
        System.out.println("> a " + r + " " + c); // I don't know why this is necessary, but try wants it
        if ("1234LX".indexOf(grid[r][c]) != -1 || !checkCoords(r, c)) {
            System.out.println("Error adding laser at: (" + r + ", " + c + ")");
            display();
        } else {

            System.out.println("Laser added at: (" + r + ", " + c + ")");
            grid[r][c] = 'L';

            /*char t = LASER; //TODO removed temporarily / permanantly depending on how well updateBeams works
            leftBeam(r, c, t);
            rightBeam(r, c, t);
            upBeam(r, c, t);
            downBeam(r, c, t);
            */

            display();
        }
    }

    /**
     * Helper functon to run through the grid and run the beam drawing function when a laser is found.
     */
    public void updateBeams() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (grid[row][col] == LASER) {

                    leftBeam(row, col, BEAM);
                    rightBeam(row, col, BEAM);
                    upBeam(row, col, BEAM);
                    downBeam(row, col, BEAM);
                }
            }
        }
    }

    /**
     * Removes a laser given a row and a column, and removes the beam from the laser
     * @param r the row to remove the laser from
     * @param c the column to remove the laser from
     */
    public void remove(int r, int c) {
        System.out.println("> r " + r + " " + c); // I don't know why this is necessary, but try wants it
        //If pillar or laser
        if (grid[r][c] != 'L' || !checkCoords(r, c)) {
            System.out.println("Error removing laser at: (" + r + ", " + c + ")");
            display();
        } else {
            System.out.println("Laser removed at: (" + r + ", " + c + ")");
            char t = '.';
            grid[r][c] = t;
            leftBeam(r, c, t);
            rightBeam(r, c, t);
            upBeam(r, c, t);
            downBeam(r, c, t);
            display();
        }

    }

    public boolean checkBeams(int r, int c) {
        return (!leftBeam(r, c, 'v') && !rightBeam(r, c, 'v') && !upBeam(r, c, 'v') && !downBeam(r, c, 'v'));
    }

    public boolean leftBeam(int r, int c, char type) {
        if (c == 0) {
            return false;
        }
        for (int leftIterator = c - 1; leftIterator >= 0; leftIterator--) {
            if (type == 'v') {
                if (grid[r][leftIterator] == LASER) {
                    return true;
                } else if ("01234X".indexOf(grid[r][leftIterator]) != -1) {
                    return false;
                }
            } else {
                if ("01234LX".indexOf(grid[r][leftIterator]) != -1) {
                    break;
                } else {
                    grid[r][leftIterator] = type;
                }
            }

        }
        return false;
    }

    public boolean rightBeam(int r, int c, char type) {
        if (c == width - 1) {
            return false;
        }
        //right
        for (int rightIterator = c + 1; rightIterator < width; rightIterator++) {
            if (type == 'v') {
                if (grid[r][rightIterator] == LASER) {
                    return true;
                } else if ("01234X".indexOf(grid[r][rightIterator]) != -1) {
                    return false;
                }
            } else {
                if ("01234LX".indexOf(grid[r][rightIterator]) != -1) {
                    break;
                } else {
                    grid[r][rightIterator] = type;
                }
            }
        }
        return false;
    }

    public boolean upBeam(int r, int c, char type) {
        if (r == 0) {
            return false;
        }
        //up
        for (int upIterator = r - 1; upIterator >= 0; upIterator--) {
            if (type == 'v') {
                if (grid[upIterator][c] == LASER) {
                    return true;
                } else if ("01234X".indexOf(grid[upIterator][c]) != -1) {
                    return false;
                }
            } else {
                if ("01234LX".indexOf(grid[upIterator][c]) != -1) {
                    break;
                } else {
                    grid[upIterator][c] = type;
                }
            }
        }
        return false;
    }

    public boolean downBeam(int r, int c, char type) {
        if (r == height - 1) {
            return false;
        }
        //down
        for (int downIterator = r + 1; downIterator < height; downIterator++) {
            if (type == 'v') {
                if (grid[downIterator][c] == LASER) {
                    return true;
                } else if ("01234X".indexOf(grid[downIterator][c]) != -1) {
                    return false;
                }
            } else {
                if ("01234LX".indexOf(grid[downIterator][c]) != -1) {
                    break;
                } else {
                    grid[downIterator][c] = type;
                }
            }
        }
        return false;
    }

    public int checkNeighbors(int r, int c) {
        int laserCount = 0;
        // check left
        if (c > 0) {
            if (grid[r][c - 1] == 'L') laserCount++;
        }
        // check right
        if (c < width - 1) {
            if (grid[r][c + 1] == 'L') laserCount++;
        }
        // check up
        if (r > 0) {
            if (grid[r - 1][c] == 'L') laserCount++;
        }
        if (r < height - 1) {
            if (grid[r + 1][c] == 'L') laserCount++;
        }
        return laserCount;
    }

    public void display() {
        updateBeams();
        System.out.println(this);
    }

    public void help() {
        System.out.println("a|add r c: Add laser to (r,c)");
        System.out.println("d|display: Display safe");
        System.out.println("h|help: Print this help message");
        System.out.println("q|quit: Exit program");
        System.out.println("r|remove r c: Remove laser from (r,c)");
        System.out.println("v|verify: Verify safe correctness");
    }

    public void quit() {
        System.exit(0);
    }

    public void verify() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                //Checks no aligned lasers
                if (grid[row][col] == LASER) {
                    if (!checkBeams(row, col)) {
                        System.out.println("Error verifying at: (" + row + ", " + col + ")");
                        display();
                        return;
                    }
                    //Checks correct amount of emitters per pillar
                } else if ("01234X".indexOf(grid[row][col]) != -1) {
                    int neighbors = checkNeighbors(row, col);
                    if (!(grid[row][col] == 'X')) {

                        if (neighbors != Integer.parseInt(grid[row][col] + "")) {
                            System.out.println("Error verifying at: (" + row + ", " + col + ")");
                            display();
                            return;
                        }
                    }
                    //checks no more empties
                } else if (grid[row][col] == EMPTY) {
                    System.out.println("Error verifying at: (" + row + ", " + col + ")");
                    display();
                    return;
                }
            }

        }
        System.out.println("Safe is fully verified!");
        display();
    }

    @Override
    public String toString() { //TODO for some reason this method is outputting a mirrored top and bottom row
        String result = "  ";
        // Creates labels for the top columns
        for (int i = 0; i < width + (width - 1); i++) {
            if (i % 2 == 0) result += i / 2 + " ";
        }
        result += "\n  ";
        // Creates dividers for the top part of the Laser puzzle
        for (int i = 0; i < width + (width - 1); i++) {
            result += HORI_DIVIDE;
        }
        result += "\n";
        // nested for loops to generate the visible part of the grid
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // this if creates the row numbers and left hand divider
                if (col == 0) {
                    result += row + "" + VERT_DIVIDE;
                }

                result += (grid[row][col]);

                // this if adds spacing after every item gets placed in the puzzle
                if (col >= 0 && col < width - 1) {
                    result += " ";
                }

            }
            // new line after running through the whole X line
            if (row != height -1) {
                result += "\n";
            }
        }
        return result;
    }


    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java LasersPTUI safe-file [input]");
            System.exit(0);
        }
        LasersPTUI lasers = new LasersPTUI(args[0]);
        if (args.length == 2) {
            lasers.readInputFile(args[1]);
        }

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            lasers.parseCommand(in.nextLine());

        }

    }
}
