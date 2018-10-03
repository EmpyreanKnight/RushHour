import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;

class Solver {
	// classic Rush Hour parameters
	static final String SHORTS = "2BX"; // length 2 cars
	static final char GOAL_CAR = 'X';
	static final char EMPTY = '.'; // empty space, movable into
	static final char VOID = '@'; // represents everything out of bound
	private static final int N = 6;
	private static final int M = 6;
	private static final int GOAL_R = 2;
	private static final int GOAL_C = 5;

    private static final String HORZS = "23X"; // horizontal-sliding cars
    private static final String VERTS = "BC"; // vertical-sliding cars
    private static final String LONGS = "3C"; // length 3 cars
    private static final String SHORTS = "2BX"; // length 2 cars
    private static final char GOAL_CAR = 'X';
    private static final char EMPTY = '.'; // empty space, movable into
    private static final char VOID = '@'; // represents everything out of bound

	// conventional row major 2D-1D index transformation
    private static int rc2i(int r, int c) {
		return r * N + c;
	}

	// checks if an entity is of a given type
    private static boolean isType(char entity, String type) {
		return type.indexOf(entity) != -1;
	}

	// finds the length of a cars
    private static int length(char car) {
		return isType(car, LONGS) ? 3 : isType(car, SHORTS) ? 2 : 0 / 0; 
		// a shortcut for throwing IllegalArgumentException
	}

	// in given state, returns the entity at a given coordinate, possibly out of bound
    private static char at(String state, int r, int c) {
		return (inBound(r, M) && inBound(c, N)) ? state.charAt(rc2i(r, c)) : VOID;
	}

    private static boolean inBound(int v, int max) {
		return (v >= 0) && (v < max);
	}

	// checks if a given state is a goal state
    private static boolean isGoal(String state) {
		return at(state, GOAL_R, GOAL_C) == GOAL_CAR;
	}

	// in a given state, starting from given coordinate, toward the given direction,
	// counts how many empty spaces there are (origin inclusive)
    private static int countSpaces(String state, int r, int c, int dr, int dc) {
		int k = 0;
		while (at(state, r + k * dr, c + k * dc) == EMPTY) {
			k++;
		}
		return k;
	}

	// the predecessor map, maps currentState => previousState
    private static Map<String, String> pred = new HashMap<>();
	// the breadth first search queue
    private static Queue<String> queue = new LinkedList<>();

	// the breadth first search proposal method: 
	// if we haven't reached it yet, we map the given state and add to queue
    private static void propose(String next, String prev) {
		if (!pred.containsKey(next)) {
			pred.put(next, prev);
			queue.add(next);
		}
	}

	// in a given state, from a given origin coordinate, attempts to find a cars of a given type
	// at a given distance in a given direction; if found, slide it in the opposite direction
	// one spot at a time, exactly n times, proposing those states to the breadth first search
    private static void slide(String current, int r, int c, String type, int distance, int dr, int dc, int n) {
		r += distance * dr;
		c += distance * dc;
		char car = at(current, r, c);
		if (!isType(car, type))
			return;
		final int L = length(car);
		StringBuilder sb = new StringBuilder(current);
		for (int i = 0; i < n; i++) {
			r -= dr;
			c -= dc;
			sb.setCharAt(rc2i(r, c), car);
			sb.setCharAt(rc2i(r + L * dr, c + L * dc), EMPTY);
			propose(sb.toString(), current);
			current = sb.toString(); // comment to combo as one stepCount
		}
	}

	// explores a given state; searches for next level states in the breadth
    private static void explore(String current) {
		for (int r = 0; r < M; r++) {
			for (int c = 0; c < N; c++) {
				if (at(current, r, c) != EMPTY)
					continue;
				int nU = countSpaces(current, r, c, -1, 0);
				int nD = countSpaces(current, r, c, +1, 0);
				int nL = countSpaces(current, r, c, 0, -1);
				int nR = countSpaces(current, r, c, 0, +1);
				slide(current, r, c, VERTS, nU, -1, 0, nU + nD - 1);
				slide(current, r, c, VERTS, nD, +1, 0, nU + nD - 1);
				slide(current, r, c, HORZS, nL, 0, -1, nL + nR - 1);
				slide(current, r, c, HORZS, nR, 0, +1, nL + nR - 1);
			}
		}
	}

	// stepCount guide vector, move the IDth cars (fromX, fromY) => (toX, toY)
	private static Vector<Steps> results = new Vector<>();
	// cars status vector, only maintain (fromX, fromY) coordinates to ID
	private static Vector<Steps> cars = new Vector<>();
	
	// update startMusic position of the moved cars
	// return the ID of moved cars
    private static int updateID(int r, int c, int dx, int dy) {
		for(Steps car : cars) {
			if(car.fromX == r && car.fromY == c) {
				car.fromX += dx;
				car.fromY += dy;
				return car.ID;
			}
		}
		return 0;
	}
	
	// the recorder method, add a stepCount to the result vector
    private static void addTrace(String prev, String curr) {
		if(prev == null) {
			return;
		}
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if(prev.charAt(rc2i(i, j)) != curr.charAt(rc2i(i, j))) {
					if(prev.charAt(rc2i(i, j)) == '.') {
						int dx = isType(curr.charAt(rc2i(i, j)), HORZS) ? 0 : 1;
						int dy = isType(curr.charAt(rc2i(i, j)), VERTS) ? 0 : 1;
						int ID = updateID(i + dx, j + dy, -dx, -dy);
						results.add(new Steps(ID, i + dx, j + dy, i, j));
					}
					else if (curr.charAt(rc2i(i, j)) == '.') {
						int dx = isType(prev.charAt(rc2i(i, j)), HORZS) ? 0 : 1;
						int dy = isType(prev.charAt(rc2i(i, j)), VERTS) ? 0 : 1;
						int ID = updateID(i, j, dx, dy);
						results.add(new Steps(ID, i, j, i + dx, j + dy));
					}
					return;
				}
			}
		}
	}

	// the predecessor tracing method, implemented using recursion for brevity;
	// guaranteed no infinite recursion
    private static int trace(String current) {
		String prev = pred.get(current);
		int step = (prev == null) ? 0 : trace(prev) + 1;
		//System.out.println(stepCount);
		//System.out.println(prettify(current));
		addTrace(prev, current);
		return step;
	}
	
	// initialize all containers
    private static void init() {
		results.clear();
		cars.clear();
		pred.clear();
		queue.clear();
	}
	
	// typical queue-based breadth first search implementation
	static Vector<Steps> solve(Map<Integer, Steps> position) {
		init();
		
		StringBuilder initial = new StringBuilder();
		for(int i = 0; i < N*M; i++) {
			initial.append('.');
		}

		for (Steps pos : position.values()) {
			cars.add(pos);
			if(pos.ID == 1) {
				for (int i = pos.fromY; i <= pos.toY; i++) {
					initial.setCharAt(rc2i(pos.fromX, i), 'X');
				}
				continue;
			}
			
			if (pos.fromX == pos.toX) {
				for (int i = pos.fromY; i <= pos.toY; i++) {
					initial.setCharAt(rc2i(pos.fromX, i), (pos.toY - pos.fromY == 2 ? '3' : '2'));
				}
			}
			else if (pos.fromY == pos.toY) {
				for(int i = pos.fromX; i <= pos.toX; i++) {
					initial.setCharAt(rc2i(i, pos.fromY), (pos.toX - pos.fromX == 2 ? 'C' : 'B'));
				}
			}
		}

		propose(initial.toString(), null);
		while (!queue.isEmpty()) {
			String current = queue.remove();
			if (isGoal(current)) {
				trace(current);
				break;
			}
			explore(current);
		}
		
		//System.out.println(pred.size() + " explored");
		return results;
	}
}


class Steps {
	int ID;
	int fromX, fromY;
	int toX, toY;

	Steps(int ID, int fromX, int fromY, int toX, int toY) {
		this.ID = ID;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}
}
