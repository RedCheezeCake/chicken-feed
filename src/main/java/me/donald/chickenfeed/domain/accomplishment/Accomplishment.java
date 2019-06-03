package me.donald.chickenfeed.domain.accomplishment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Accomplishment {

	@Id
	@Column(name = "round")
	private int round;

	@ElementCollection
	@CollectionTable(name = "hits",
			joinColumns = @JoinColumn(name = "round"))
	@OrderColumn(name = "rank")
	private List<RankHit> hits;

	public Accomplishment(int round) {
		this.round = round;
		this.hits = new ArrayList<>();

		for (int rank = 0; rank < 6; rank++)
			this.hits.add(new RankHit(0));
	}

	public int getRound() {
		return this.round;
	}

	public List<RankHit> getHits() {
		return this.hits;
	}

	/**
	 * 특정 순위에 적중 횟수를 추가함
	 *
	 * @param rank 순위
	 * @param hit  적중 횟수
	 */
	public void addRankHit(int rank, int hit) {
		RankHit rankHit = this.hits.get(rank - 1);
		int beforeHit = rankHit.getHit();
		this.hits.set(rank - 1, new RankHit(beforeHit + hit));
	}
}
