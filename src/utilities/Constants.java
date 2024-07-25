package utilities;

public class Constants 
{
	public static class Directions
	{
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}
	
	public static class PlayerConstants
	{
		public static final int RUNNING = 0;
		public static final int IDLE = 1;
		public static final int JUMP = 2;
		public static final int FALLING = 3;
		public static final int GROUND = 4;
		public static final int HIT = 5;
		public static final int ATTACK = 6;
		public static final int DASH = 7;
		public static final int RUNNING_REVERSE = 8;	
		
		public static int GetSpriteAmount(int player_action)
		{
			switch(player_action)
			{
			case RUNNING: 
			case RUNNING_REVERSE: 
				return 8;
			case JUMP:
				return 3;
			case GROUND:
			case HIT:
			case ATTACK:
				return 2;
			case DASH:
				return 4;
			case IDLE: 
			case FALLING:
			default:
				return 1;
			}
		}
	}
}
