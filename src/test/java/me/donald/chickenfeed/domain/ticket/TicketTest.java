package me.donald.chickenfeed.domain;

import me.donald.chickenfeed.domain.ticket.Ball;
import me.donald.chickenfeed.domain.ticket.Rank;
import me.donald.chickenfeed.domain.ticket.RankType;
import me.donald.chickenfeed.domain.ticket.Ticket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TicketTest {

	/**
	 * 티켓이 생성 가능한지 검증
	 */
	@Test
	public void checkIssuable_emptyRequest() {
		// given
		int[] requestBalls = new int[]{0,0,0,0,0,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isTrue();
	}

	@Test
	public void checkIssuable_justOneBallRequest() {
		// given
		int[] requestBalls = new int[]{0,0,3,0,0,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isTrue();
	}

	@Test
	public void checkIssuable_moreThanOneBallsRequest() {
		// given
		int[] requestBalls = new int[]{0,24,0,0,32,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isTrue();
	}

	@Test
	public void checkIssuable_failedByNotOrdered() {
		// given
		int[] requestBalls = new int[]{0,12,0,8,0,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isFalse();
	}

	@Test
	public void checkIssuable_failedByOneBallLeftSide() {
		// given
		int[] requestBalls = new int[]{0,1,0,0,0,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isFalse();
	}

	@Test
	public void checkIssuable_failedByOneBallRightSide() {
		// given
		int[] requestBalls = new int[]{0,0,0,0,45,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isFalse();
	}

	@Test
	public void checkIssuable_failedByOneMoreBalls() {
		// given
		int[] requestBalls = new int[]{0,12,0,13,0,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isFalse();
	}

	/**
	 *	티켓을 발행함
	 */
	@Test
	public void issueTicket_ticketTypeAuto() {
		// given
		int[] requestBalls = new int[]{0,0,0,0,0,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		ticket.issueTicket();

		// then
		boolean isBlank = false;
		for (Ball ball : ticket.getBalls()) {
			if (ball.getNumber() == 0) {
				isBlank = true;
				break;
			}
		}
		assertThat(isBlank).isFalse();
	}

	@Test
	public void issueTicket_ticketTypeManual() {
		// given
		int[] requestBalls = new int[]{0,24,0,0,32,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		ticket.issueTicket();

		// then
		boolean isBlank = false;
		for (Ball ball : ticket.getBalls()) {
			if (ball.getNumber() == 0) {
				isBlank = true;
				break;
			}
		}
		assertThat(isBlank).isFalse();
	}

	/**
	 * 해당 티켓이 당첨 등수를 확인
	 */
	@Test
	public void confirmWinning_third() {
		// given
		Ticket ticket = new Ticket(0, new int[]{1,2,3,4,5,6});

		// when
		Rank rank = ticket.confirmWinning(new int[]{1, 2, 3, 4, 5, 8}, 10);

		// then
		assertThat(rank.getRanking()).isEqualTo(RankType.THIRD);
	}

	@Test
	public void confirmWinning_second() {
		// given
		Ticket ticket = new Ticket(0, new int[]{1,2,3,4,5,6});

		// when
		Rank rank = ticket.confirmWinning(new int[]{1, 2, 3, 4, 5, 8}, 6);

		// then
		assertThat(rank.getRanking()).isEqualTo(RankType.SECOND);

	}

	@Test
	public void confirmWinning_lostWithTwoHits() {
		// given
		Ticket ticket = new Ticket(0, new int[]{1,2,3,4,5,6});

		// when
		Rank rank = ticket.confirmWinning(new int[]{1, 2, 7, 8, 9, 10}, 6);

		// then
		assertThat(rank.getRanking()).isEqualTo(RankType.LOST);
	}

}
