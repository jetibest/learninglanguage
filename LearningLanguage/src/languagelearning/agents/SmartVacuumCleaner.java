package languagelearning.agents;

import languagelearning.env.Environment;

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
                collectDustWithoutSound();             
                        
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
                else if(DustSouth > 0)
                {
                   moveSouth();
                }
                else
                {
                    int randomdirection = (int)(Math.random()*4);
                    if (randomdirection == 0)
                    {
                        moveNorth();
                    }
                    else if(randomdirection == 1)
                    {
                        moveEast();
                    }
                    else if(randomdirection == 2)
                    {
                        moveSouth();
                    }
                    else
                    {
                        moveWest();
                    }
                }
                
			
	}   
}
