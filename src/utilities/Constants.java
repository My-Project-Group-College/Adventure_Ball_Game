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
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int JUMP = 2;
		public static final int FALLING = 3;
		public static final int HIT = 4;
		public static final int ATTACK = 5;
		public static final int ATTACK_JUMP = 6;
		
		public static int GetSpriteAmount(int player_action)
		{
			switch(player_action)
			{
			case RUNNING: 
				return 1;
			case IDLE: 
				return 1;
			case HIT:
			case JUMP:
			case ATTACK:
			case ATTACK_JUMP:
			case FALLING:
			default:
				return 1;
			}
		}
	}
}
