package me.donald.chickenfeed.domain.accomplishment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AccomplishmentTest {

	/**
	 * 적중 횟수 추가
	 */
	@Test
	public void addRankHit_normal() {
		// given
		Accomplishment accomp = new Accomplishment(0);

		// when
		accomp.addRankHit(1, 1);
		accomp.addRankHit(2, 2);
		accomp.addRankHit(3, 3);
		accomp.addRankHit(4, 4);
		accomp.addRankHit(5, 5);
		accomp.addRankHit(6, 6);

		// then
		List<RankHit> hits = accomp.getHits();
		assertThat(hits.get(0).getHit()).isEqualTo(1);
		assertThat(hits.get(1).getHit()).isEqualTo(2);
		assertThat(hits.get(2).getHit()).isEqualTo(3);
		assertThat(hits.get(3).getHit()).isEqualTo(4);
		assertThat(hits.get(4).getHit()).isEqualTo(5);
		assertThat(hits.get(5).getHit()).isEqualTo(6);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addRankHit_exceptionOverRange() {
		// given
		Accomplishment accomp = new Accomplishment(0);

		// when
		accomp.addRankHit(7, 1);

		// then
	}

	@Test(expected = IllegalArgumentException.class)
	public void addRankHit_exceptionUnderRange() {
		// given
		Accomplishment accomp = new Accomplishment(0);

		// when
		accomp.addRankHit(0, 1);

		// then
	}
}