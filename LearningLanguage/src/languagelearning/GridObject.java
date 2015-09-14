package languagelearning;

public class GridObject
{
	private int x;
	private int y;
	private Environment env;
	
	public GridObject(int x, int y)
	{
		this.x = x;
		this.y = y;
		env = LearningLanguage.MAIN.getEnvironment();
	}
	
	public void move(int x, int y)
	{
		if(!env.canMove(x, y))
		{
			return;
		}
		this.x = x;
		this.y = y;
	}
	
	public void moveNorth()
	{
		int ny = y - 1;
		if (env.isBoundless()) {
			ny = (y - 1 + LearningLanguage.GRID_HEIGHT) % LearningLanguage.GRID_HEIGHT;
		}
		move(x, ny);
	}
	
	public void moveEast()
	{
		int nx = x + 1;
		if (env.isBoundless()) {
			nx = (x + 1) % LearningLanguage.GRID_WIDTH;
		}
		move(nx, y);
	}
	
	public void moveSouth()
	{
		int ny = y + 1;
		if (env.isBoundless()) {
			ny = (y + 1) % LearningLanguage.GRID_HEIGHT;
		}
		move(x, ny);
	}
	
	public void moveWest()
	{
		int nx = x - 1;
		if (env.isBoundless()) {
			nx = (x - 1 + LearningLanguage.GRID_WIDTH) % LearningLanguage.GRID_WIDTH;
		}
		move(nx, y);
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
}
