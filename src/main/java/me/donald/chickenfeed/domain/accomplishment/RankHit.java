package me.donald.chickenfeed.domain.accomplishment;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class RankHit {

	@Column(name = "hit")
	private int hit;

	protected RankHit() {}

	RankHit(int hit) {
		this.hit = hit;
	}

	int getHit() {
		return hit;
	}
}
