package languagelearning.states;

public enum StateVariable {
	DUST_BELOW,
	DUST_AHEAD, DUST_TWO_AHEAD,
	DUST_NORTH, DUST_EAST, DUST_SOUTH, DUST_WEST,
	OBSTACLE_AHEAD,
	OBSTACLE_NORTH, OBSTACLE_EAST, OBSTACLE_SOUTH, OBSTACLE_WEST,
	INTERNAL_STATE_A,
	SOUND_A_BELOW, SOUND_B_BELOW, SOUND_C_BELOW,
	SOUND_A_AHEAD, SOUND_B_AHEAD, SOUND_C_AHEAD;
}
