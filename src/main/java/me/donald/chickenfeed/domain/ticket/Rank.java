package me.donald.chickenfeed.domain.ticket;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Rank {

	@Column(name = "ranking")
	private RankType ranking;

	protected Rank() {}

	Rank(int hit) {
		this.ranking = RankType.getRank(hit);
	}

	public RankType getRanking() {
		return ranking;
	}

	void bonusHit() {
		this.ranking = RankType.SECOND;
	}
}
