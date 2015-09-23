package languagelearning;

/**
 *
 * @author simon
 */
public class SmartVacuumCleaner extends VacuumCleaner
{   
    public SmartVacuumCleaner(int x, int y)
	{
            super(x, y);
            
	}
    
        @Override
	public void run()
	{	
            int DustNorth = env.getDustValue(getX(), getY()-1); 
            int DustEast = env.getDustValue(getX()+1, getY());
            int DustSouth = env.getDustValue(getX(), getY()+1);
            int DustWest = env.getDustValue(getX()-1, getY()); 
            
                System.out.print("DustNorth = " + DustNorth + " DustEast = " + DustEast + " DustSouth = " + DustSouth + "DustWest = " + DustWest + "\n\n");
            
            
                // Move to direction with most dust
                collectDust();             
                        
                if(DustNorth > DustEast && DustNorth > DustWest && DustNorth > DustSouth)
                {
                   moveNorth(); 
                }                            
                else if(DustEast > DustWest && DustEast > DustSouth)
                {
                   moveEast();
                }
                else if(DustWest > DustSouth)
                {
                   moveWest();
                }
                else
                {
                   moveSouth();
                }
			
	}   
}
