package languagelearning.util;

import languagelearning.agents.Direction;

public class BooleanMatrix {
	public static final BooleanMatrix TRIANGLE_7x5 = new BooleanMatrix(
				new boolean[][]{
						{true,true,true,true,true},
						{false,true,true,true,false},
						{false,false,true,false,false},
						{false,false,false,false,false},
						{false,false,false,false,false},
						{false,false,false,false,false},
						{false,false,false,false,false}
				}
			);
	
	public static final BooleanMatrix SQUARE_9x9 = new BooleanMatrix(
			new boolean[][]{
					{true,true,true,true,true,true,true,true,true},
					{true,true,true,true,true,true,true,true,true},
					{true,true,true,true,true,true,true,true,true},
					{true,true,true,true,false,true,true,true,true},
					{true,true,true,true,false,true,true,true,true},
					{true,true,true,true,true,true,true,true,true},
					{true,true,true,true,true,true,true,true,true},
					{true,true,true,true,true,true,true,true,true},
					{true,true,true,true,true,true,true,true,true},
			}
		);

	public static final BooleanMatrix SQUARE_7x7 = new BooleanMatrix(
			new boolean[][]{
					{true,true,true,true,true,true,true},
					{true,true,true,true,true,true,true},
					{true,true,true,false,true,true,true},
					{true,true,true,false,true,true,true},
					{true,true,true,true,true,true,true},
					{true,true,true,true,true,true,true},
					{true,true,true,true,true,true,true}
			}
		);

	
	public static final BooleanMatrix SQUARE_5x5 = new BooleanMatrix(
			new boolean[][]{
					{true,true,true,true,true},
					{true,true,false,true,true},
					{true,true,false,true,true},
					{true,true,true,true,true},
					{true,true,true,true,true}
			}
		);

	public static final BooleanMatrix SQUARE_3x3 = new BooleanMatrix(
			new boolean[][]{
					{true,false,true},
					{true,false,true},
					{true,true,true},
			}
		);

	public static final BooleanMatrix CROSS_5x5 = new BooleanMatrix(
			new boolean[][]{
					{false,false,true,false,false},
					{false,false,false,false,false},
					{true,true,false,true,true},
					{false,false,true,false,false},
					{false,false,true,false,false}
			}
		);
	
	private boolean[][] matrix = null;
	
	public BooleanMatrix(boolean[][] values) {
		this.matrix = values;
		if (getWidth() % 2 != 1) {
			throw new RuntimeException("Number of columns is not odd");
		}
		if (getHeight() % 2 != 1) {
			throw new RuntimeException("Number of rows is not odd");
		}
	}
	
	public BooleanMatrix rotateClockWise() {
	    int w = getHeight();
	    int h = getWidth();
	    boolean[][] matrix2 = new boolean[h][w];
	    for (int i = 0; i < h; ++i) {
	        for (int j = 0; j < w; ++j) {
	        	matrix2[i][j] = matrix[w - j - 1][i];
	        }
	    }
	    
	    return new BooleanMatrix(matrix2);
	}
	
	public BooleanMatrix rotateInDirection(Direction direction) {
		if (direction == Direction.EAST) {
			return rotateClockWise();
		} else if (direction == Direction.SOUTH) {
			return rotateClockWise().rotateClockWise();
		} else if (direction == Direction.WEST) {
			return rotateClockWise().rotateClockWise().rotateClockWise();
		}
		return this;
	}
	
	public int getHeight() {
		return matrix.length;
	}
	
	public int getWidth() {
		return matrix[0].length;
	}
	
	public int getMiddleY() {
		return getHeight() / 2;
	}
	
	public int getMiddleX() {
		return getWidth() / 2;
	}
	
	public boolean getValue(int x,int y) {
		return matrix[y][x];
	}
	
	public boolean getValueRelativeToMiddlePoint(int deltaX,int deltaY) {
		int middleX = getMiddleX();
		int middleY = getMiddleY();
		int x = middleX + deltaX;
		int y = middleY + deltaY;
		return getValue(x,y);
	}
	
	public int getMinRelativeX() {
		return -getMiddleX();
	}

	public int getMaxRelativeX() {
		return getMiddleX();
	}

	public int getMinRelativeY() {
		return -getMiddleY();
	}

	public int getMaxRelativeY() {
		return getMiddleY();
	}

	public String toString(char falseChar,char trueChar) {
		StringBuffer buffer = new StringBuffer();
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				boolean value = getValue(x,y);
				buffer.append(value?trueChar:falseChar);
			}
			if (y < getHeight()-1) {
				buffer.append("\n");
			}
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		BooleanMatrix m = new BooleanMatrix(new boolean[][]{{true,true,true},{true,true,false},{false,false,true}});
		m = BooleanMatrix.TRIANGLE_7x5;
		System.out.println(m.toString('0', '1'));
		System.out.println("middle x="+m.getMiddleX() + " middle y="+m.getMiddleY());
		m = m.rotateClockWise();
		System.out.println(m.toString('0','1'));
		System.out.println("middle x="+m.getMiddleX() + " middle y="+m.getMiddleY());
	}
}
