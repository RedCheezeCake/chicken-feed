package me.donald.chickenfeed.domain.win;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class WinTest {

	/**
	 * 당첨 번호 입력
	 */
	@Test(expected = IllegalArgumentException.class)
	public void setWinNumbers_exceptionByRange() {
		// given
		Win win = new Win(0);

		// when
		win.setWinNumbers(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));

		// then
	}

	/**
	 * 1, 2, 3 등에 해당하는 당첨금 입력
	 */
	@Test
	public void setRankPrizes_normal() {
		// given
		Win win = new Win(0);
		List<Long> prizes = Arrays.asList(100L, 10L, 1L);

		// when
		win.setRankPrizes(prizes);

		// then
		assertThat(win.getRankPrize(1)).isEqualTo(100L);
		assertThat(win.getRankPrize(2)).isEqualTo(10L);
		assertThat(win.getRankPrize(3)).isEqualTo(1L);
		assertThat(win.getRankPrize(4)).isEqualTo(50000L);
		assertThat(win.getRankPrize(5)).isEqualTo(5000L);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setRankPrizes_exceptionRange() {
		// given
		Win win = new Win(0);
		List<Long> prizes = Arrays.asList(100L, 10L, 1L, 0L);

		// when
		win.setRankPrizes(prizes);

		// then
	}

	/**
	 * 등수에 따른 당첨금 조회
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getRankPrize_exceptionRange() {
		// given
		Win win = new Win(0);
		List<Long> prizes = Arrays.asList(100L, 10L, 1L);
		win.setRankPrizes(prizes);

		// when
		win.getRankPrize(0);

		// then
	}

	@Test(expected = IllegalStateException.class)
	public void getRankPrize_exceptionNotPreparedState() {
		// given
		Win win = new Win(0);

		// when
		win.getRankPrize(1);

		// then
	}
}