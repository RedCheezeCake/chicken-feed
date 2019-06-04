package me.donald.chickenfeed.domain.accomplishment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Accomplishment {

	private static final int RANK_SIZE = 6;

	@Id
	@Column(name = "round")
	private int round;

	@ElementCollection
	@CollectionTable(name = "hits",
			joinColumns = @JoinColumn(name = "round"))
	@OrderColumn(name = "rank")
	private List<RankHit> hits;

	protected Accomplishment() {}

	public Accomplishment(int round) {
		this.round = round;
		this.hits = new ArrayList<>();

		for (int rank = 0; rank < RANK_SIZE; rank++)
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
		if (rank > RANK_SIZE || rank < 1)
			throw new IllegalArgumentException("범위를 벗어난 순위입니다.");

		RankHit rankHit = this.hits.get(rank - 1);
		int beforeHit = rankHit.getHit();
		this.hits.set(rank - 1, new RankHit(beforeHit + hit));
	}
}
