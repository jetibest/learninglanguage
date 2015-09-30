package languagelearning.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import languagelearning.actions.Action;
import static languagelearning.agents.VacuumCleaner.DUST_CLEAN_VALUE;
import languagelearning.env.Environment;

/**
 *
 * @author yeti
 */
public class YetiVacuumCleaner extends VacuumCleaner
{
    private static final Action[] ACTIONS = new Action[]
    {
        Action.DO_NOTHING,
        Action.TURN_LEFT,
        Action.TURN_RIGHT,
        Action.MOVE_FORWARD,
        Action.COLLECT_DUST
    };
    private static final int SENSOR_DUST = 0;       // See if there is dust ahead
    private static final int SENSOR_BUMPER = 1;     // See if bumper collided
    private static final int[] SENSORS = new int[2];
    private int reward = 0;
    private List<ActionList> actionLists = new ArrayList<ActionList>();
    private ActionList currentActionList = null;
    private ActionList previousActionList = null;
    
    public YetiVacuumCleaner(int xp, int yp)
    {
        super(xp, yp);
        
        // generate first actionlists, at length of 1
        
        for(int i=0;i<ACTIONS.length;i++)
        {
            ActionList aList = new ActionList();
            aList.addAction(ACTIONS[i]);
            actionLists.add(aList);
        }
    }
    
    private void resetReward()
    {
        reward = 0;
    }
    
    private void addReward(int value)
    {
        reward += value;
    }
    
    private int getReward()
    {
        return reward;
    }
    
    @Override
    public int collectDust()
    {
        int dustBefore = getEnvironment().getDustValue(getX(), getY());
        int dustAfter = Math.max(Environment.DUST_MIN, getEnvironment().getDustValue(getX(), getY()) - DUST_CLEAN_VALUE);
        getEnvironment().setDustValue(getX(), getY(), dustAfter);
        
        
        if(dustAfter < dustBefore)
        {
            addReward(1);
        }
        
        return 0;
    }
    
    @Override
    public int doAction(Action a)
    {
        if(a == Action.COLLECT_DUST)
        {
            collectDust();
            return 0;
        }
        return super.doAction(a);
    }
    
    @Override
    public int moveForward()
    {
        if(!getEnvironment().canMove(getNewXInDirection(getDirection()), getNewYInDirection(getDirection())))
        {
            SENSORS[SENSOR_BUMPER] = 1;
        }
        return super.moveForward();
    }
    
    private void resetSensors()
    {
        SENSORS[SENSOR_BUMPER] = 0;
    }
    
    private void updateSensors()
    {
        SENSORS[SENSOR_DUST] = getEnvironment().getDustValue(getNewXInDirection(getDirection()), getNewYInDirection(getDirection()));
    }
    
    @Override
    public void run()
    {
        // update sensors
        updateSensors();
        
        // make decision on action and decision table
        Action selectedAction;
        
        // possible strategy could look like:
        // if sensor_dust is > x_significant
        //    moveForward --> collectDust
        // else
        //    turnLeft/Right randomly
        if(currentActionList == null || currentActionList.isFinished())
        {
            // Select a new actionlist, because current one does not exist or already finished
            
            Collections.sort(actionLists);
            for(int i=0;i<actionLists.size() && currentActionList == null;i++)
            {
                ActionList aList = actionLists.get(i);

                if(Math.random() > 0.1 || i + 1 == actionLists.size())
                {
                    // Select usually the first ones, which are highest ranked (but leave options over for less succesful actionlists)
                    // This is a bad way to do it, but sufficient for now

                    currentActionList = aList;
                }
            }
        }
        selectedAction = currentActionList.getNextAction();
        
        // reset sensors before action
        resetSensors();
        
        // reset reward before action
        resetReward();
        
        // do action
        doAction(selectedAction);
        
        // find reward
        int reward = getReward();
        
        // update previous actionList's last action
        if(previousActionList != null)
        {
            previousActionList.latestAction(selectedAction, reward);
        }
        
    }
    
    private class ActionList implements Comparable
    {
        // A combination of sequential actions
        private List<Action> actions = new ArrayList<Action>();
        private int reward = 0;
        private int index = 0;
        private Map<Action, Integer[]> expandedAction = new HashMap<Action, Integer[]>();
        
        public ActionList()
        {
            
        }
        
        public void latestAction(Action a, int r)
        {
            Integer[] res = expandedAction.get(a);
            if(res == null)
            {
                res = new Integer[]{0, 0};
            }
            res[0] += r;    // 0 is reward
            res[1] += 1;    // 1 is count
            expandedAction.put(a, res);
            
            // when a certain expanded action seems to have a high reward ratio, add that action to the actionlist
            // but make a copy of the actionlist, and expand the copy
            // do this in run()
        }
        
        private void reset()
        {
            index = 0;
        }
        
        public void addAction(Action a)
        {
            actions.add(a);
        }
        
        public List<Action> getActions()
        {
            return actions;
        }
        
        public boolean isFinished()
        {
            return index >= actions.size();
        }
        
        public Action getNextAction()
        {
            if(index == 0 || index >= actions.size())
            {
                reset();
            }
            Action a = actions.get(index);
            index++;
            return a;
        }
        
        public void setReward(int r)
        {
            reward = r;
        }
        
        public int getReward()
        {
            return reward;
        }
        
        @Override
        public int compareTo(Object obj)
        {
            return ((ActionList) obj).getReward() - reward;
        }
    }
}
