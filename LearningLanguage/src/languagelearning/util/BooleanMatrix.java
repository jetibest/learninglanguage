package languagelearning.util;

import languagelearning.agents.Direction;

public class BooleanMatrix {
	private boolean[][] matrix = null;
	
	public BooleanMatrix(boolean[][] values) {
		this.matrix = values;
	}
	
	public BooleanMatrix rotateClockWise() {
		   /* W and H are already swapped */
	    int w = matrix.length;
	    int h = matrix[0].length;
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
	
	public boolean[][] getValues() {
		return matrix;
	}
	
	public String toString(char falseChar,char trueChar) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < matrix.length; i++) {
			boolean[] row = matrix[i];
			for (int j = 0; j < row.length; j++) {
				boolean value = row[j];
				buffer.append(value?trueChar:falseChar);
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	/*public static void main(String[] args) {
		BooleanMatrix m = new BooleanMatrix(new boolean[][]{{true,true,true},{true,true,false},{false,false,true}});
		System.out.println(m.toString('0', '1'));
		System.out.println(m.rotateClockWise().toString('0','1'));
	}*/
}
