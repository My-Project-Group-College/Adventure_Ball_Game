package utilities;

import main.Game;

public class Constants {
	public static class EnemyConstants {
		public static final int BABLU = 0;

		public static final int RUNNING = 0;
		public static final int IDLE = 1;
		public static final int HIT = 2;
		public static final int ATTACK = 3;
		public static final int RUNNING_REVERSE = 4;

		public static final int BABLU_WIDTH_DEFAULT = 64;
		public static final int BABLU_HEIGHT_DEFAULT = 64;

		public static final int BABLU_WIDTH = (int) ((BABLU_WIDTH_DEFAULT * Game.SCALE - 32) * 0.75);
		public static final int BABLU_HEIGHT = (int) ((BABLU_HEIGHT_DEFAULT * Game.SCALE - 32) * 0.75);

		public static final int BABLU_DRAWOFFSET_X = (int) (6.5 * Game.SCALE);
		public static final int BABLU_DRAWOFFSET_Y = (int) (8.5 * Game.SCALE);

		public static int GetSpriteAmount(int enemy_type, int enemy_state) {
			switch (enemy_type) {

			case BABLU:
				switch (enemy_state) {
				case RUNNING:
				case RUNNING_REVERSE:
					return 8;
				case IDLE:
					return 1;
				case HIT:
					return 2;
				case ATTACK:
					return 2;
				}
			default:
				return 0;
			}
		}

		public static int GetMaxHealth(int enemy_type) {
			switch (enemy_type) {
			case BABLU:
				return 3;
			default:
				return 1;
			}
		}

		public static int GetEnemyDmg(int enemy_type) {
			switch (enemy_type) {
			case BABLU:
				return 1;
			default:
				return 0;
			}
		}
	}

	public static class UI {
		public static class Buttons {
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
		}

		public static class PauseButtons {
			public static final int SOUND_B_SIZE_DEFAULT = 42;
			public static final int SOUND_B_SIZE = (int) (SOUND_B_SIZE_DEFAULT * Game.SCALE);
		}

		public static class URMButtons {
			public static final int URM_B_DEFAULT_SIZE = 56;
			public static final int URM_B_SIZE = (int) (URM_B_DEFAULT_SIZE * Game.SCALE);
		}

		public static class VolumeButtons {
			public static final int VOLUME_B_DEFAULT_WIDTH = 28;
			public static final int VOLUME_B_DEFAULT_HEIGHT = 44;
			public static final int SLIDER_DEFAULT_WIDTH = 214;

			public static final int VOLUME_B_WIDTH = (int) (VOLUME_B_DEFAULT_WIDTH * Game.SCALE);
			public static final int VOLUME_B_HEIGHT = (int) (VOLUME_B_DEFAULT_HEIGHT * Game.SCALE);
			public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);

		}
	}

	public static class Directions {
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}

	public static class PlayerConstants {
		public static final int RUNNING = 0;
		public static final int IDLE = 1;
		public static final int JUMP = 2;
		public static final int FALLING = 3;
		public static final int GROUND = 4;
		public static final int HIT = 5;
		public static final int ATTACK = 6;
		public static final int DASH = 7;
		public static final int RUNNING_REVERSE = 8;
//		public static final int DASH_REVERSE = 9;

		public static final int PLAYER_NORMAL_DAMAGE = 2;

		public static int GetSpriteAmount(int player_action) {
			switch (player_action) {
			case RUNNING:
			case RUNNING_REVERSE:
				return 8;
			case DASH:
//			case DASH_REVERSE:
				return 4;
			case JUMP:
				return 3;
			case GROUND:
			case HIT:
			case ATTACK:
				return 2;
			case IDLE:
			case FALLING:
			default:
				return 1;
			}
		}
	}
}
