package languagelearning.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import languagelearning.actions.Action;

/**
 *
 * @author yeti
 */
public class YetiVacuumCleaner extends VacuumCleaner
{
    /*
    State:
        -> Multiple sensory inputs.
        -> One possible action output.
        -> One possible reward, 0 or 1.
    
    Sensors:
        -> Dust-sensor.     (look ahead one cell for dust)
        -> Bumper-sensor.   (look ahead one cell for obstactle)
    
    Actions:
        -> Do Nothing.
        -> Turn Left.
        -> Turn Right.
        -> Move Forward.
        -> Collect Dust.
        -> Produce Sound A.
    
    Reward:
        -> 0.
        -> 1 of hoger.
    
    State U
    {
        dust-sensor:    1
        bumper-sensor:  0
        sound-sensor:   0
    }
    State V
    {
        dust-sensor:    1
        bumper-sensor:  0
        sound-sensor:   0
    }
    State W
    {
        dust-sensor:    1
        bumper-sensor:  0
        sound-sensor:   0
    }
    State X
    {
        dust-sensor:    0
        bumper-sensor:  0
        sound-sensor:   0
    }
    State Y
    {
        dust-sensor:    0
        bumper-sensor:  0
        sound-sensor:   0
    }
    State Z
    {
        dust-sensor:    1
        bumper-sensor:  0
        sound-sensor:   0
    }
    ,-------------------.
    | O- | ## | .. | .. |
    |----+----+----+----|
    | .. | ## | ## | ## |
    |----+----+----+----|
    | .. | .. | ## | ## |
    `-------------------'
    
    Rule:
        If reward is zero, and the state did not change, move back to begin state that matches current sensory values.
        If reward is zero, and the state did change, create a new State-node with current sensory values, and continue.
        If reward is non-zero, move back to begin state that matches current sensory values.
    
      _______                                            
     /       \       Action=[Do Nothing], Reward=0       
    | State U |  --------------------------------------> Back to State U
     \_______/                                           
         |                                                 _______                                                                                                      _______
         |          Action=[Move Forward], Reward=0       /       \     Action=[Collect Dust], Reward=1                                                                /       \
         |\_____,--------------------------------------> | State X | ---------------------------------------> REWARD, THUS BACK TO A BEGIN STATE THAT MATCHES {0,0,0} | State U |
         |                                                \_______/                                                                                                    \_______/
         |                                               
         |          Action=[Collect Dust], Reward=0      
          \_____,--------------------------------------> Back to State U

      _______
     /       \       Action=[Do Nothing], Reward=0
    | State Y |  --------------------------------------->
     \_______/
    
    New states are made after every new action, or new states for new sensor-values
    However, if a certain state with existing sensor-values already exist, and there were previous actions, and reward is zero, insert the begin-pointer at that state
    */
    
    private static final Action[] ACTIONS = new Action[]
    {
        Action.DO_NOTHING,
        Action.TURN_LEFT,
        Action.TURN_RIGHT,
        Action.MOVE_FORWARD,
        Action.COLLECT_DUST,
        Action.PRODUCE_SOUND_A
    };
    private static final int SENSOR_DUST = 0;       // See if there is dust ahead
    private static final int SENSOR_BUMPER = 1;     // See if bumper collided
    private static final int[] SENSORS = new int[2];
    private int reward = 0;
    // static, because this actionlist should hold for every YetiVacuumCleaner instance
    private static List<ActionList> actionLists = new ArrayList<ActionList>();
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
        int dustAfter = Math.max(getEnvironment().getDustMin(), getEnvironment().getDustValue(getX(), getY()) - getDustCleanValue());
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
        
        // We can give a reward to the action-list
        // And based on that see if the action-list is a good action-list
        // But we need to combine the reward with a certain sensory input state
        // While sorting the list
        // Sort on both highest reward, but highest reward in combination with input state
        // So per input state, we have a set of action-lists!
        // So we need to make this algorithm generic.
        // And use it double-wise.
        // Architecture:
        
        // YetiVacuumCleaner: YVC
        // 
        // Data structure: Graph with nodes
        // 
        // every node represents a certain sensory state
        // every connection represents a certain action
        // every connection also has a reward memory
        // there are no hard states
        // rather, soft approximations of states, 
        
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
