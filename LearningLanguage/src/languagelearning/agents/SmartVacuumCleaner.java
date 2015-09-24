package languagelearning.agents;

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
            int DustNorth = getEnvironment().getDustValue(getX(), getY()-1); 
            int DustEast = getEnvironment().getDustValue(getX()+1, getY());
            int DustSouth = getEnvironment().getDustValue(getX(), getY()+1);
            int DustWest = getEnvironment().getDustValue(getX()-1, getY()); 
            
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
