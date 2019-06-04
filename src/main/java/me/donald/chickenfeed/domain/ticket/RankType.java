package me.donald.chickenfeed.domain.ticket;

public enum RankType {
	WIN, SECOND, THIRD, FOURTH, FIFTH, LOST;

	public static RankType getRank(int hit) {
		if (hit < 3)
			return LOST;
		else if (hit == 3)
			return FIFTH;
		else if (hit == 4)
			return FOURTH;
		else if (hit == 5)
			return THIRD;
		else return WIN;
	}
}
