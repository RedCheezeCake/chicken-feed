package me.donald.chickenfeed.domain.ticket;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
		Integer[] requestBalls = new Integer[]{0,0,0,0,0,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		ticket.issueTicket();

		// then
		assertThat(ticket.getIssueTime()).isEqualToIgnoringMinutes(LocalDateTime.now());
	}

	@Test
	public void checkIssuable_justOneBallRequest() {
		// given
		Integer[] requestBalls = new Integer[]{0,0,3,0,0,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		ticket.issueTicket();

		// then
		assertThat(ticket.getIssueTime()).isEqualToIgnoringMinutes(LocalDateTime.now());
	}

	@Test
	public void checkIssuable_moreThanOneBallsRequest() {
		// given
		Integer[] requestBalls = new Integer[]{0,24,0,0,32,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		ticket.issueTicket();

		// then
		assertThat(ticket.getIssueTime()).isEqualToIgnoringMinutes(LocalDateTime.now());
	}

	@Test(expected = IllegalArgumentException.class)
	public void checkIssuable_failedByNotOrdered() {
		// given
		Integer[] requestBalls = new Integer[]{0,12,0,8,0,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		ticket.issueTicket();
	}

	@Test(expected = IllegalArgumentException.class)
	public void checkIssuable_failedByOneBallLeftSide() {
		// given
		Integer[] requestBalls = new Integer[]{0,1,0,0,0,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		ticket.issueTicket();
	}

	@Test(expected = IllegalArgumentException.class)
	public void checkIssuable_failedByOneBallRightSide() {
		// given
		Integer[] requestBalls = new Integer[]{0,0,0,0,45,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		ticket.issueTicket();
	}

	@Test(expected = IllegalArgumentException.class)
	public void checkIssuable_failedByOneMoreBalls() {
		// given
		Integer[] requestBalls = new Integer[]{0,12,0,13,0,0};
		Ticket ticket = new Ticket(0, requestBalls);

		// when
		ticket.issueTicket();
	}

	/**
	 *	티켓을 발행함
	 */
	@Test
	public void issueTicket_ticketTypeAuto() {
		// given
		Integer[] requestBalls = new Integer[]{0,0,0,0,0,0};
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
		Integer[] requestBalls = new Integer[]{0,24,0,0,32,0};
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
		Ticket ticket = new Ticket(0, new Integer[]{1,2,3,4,5,6});

		// when
		Rank rank = ticket.confirmWinning(new int[]{1, 2, 3, 4, 5, 8}, 10);

		// then
		assertThat(rank.getRanking()).isEqualTo(RankType.THIRD);
	}

	@Test
	public void confirmWinning_second() {
		// given
		Ticket ticket = new Ticket(0, new Integer[]{1,2,3,4,5,6});

		// when
		Rank rank = ticket.confirmWinning(new int[]{1, 2, 3, 4, 5, 8}, 6);

		// then
		assertThat(rank.getRanking()).isEqualTo(RankType.SECOND);

	}

	@Test
	public void confirmWinning_lostWithTwoHits() {
		// given
		Ticket ticket = new Ticket(0, new Integer[]{1,2,3,4,5,6});

		// when
		Rank rank = ticket.confirmWinning(new int[]{1, 2, 7, 8, 9, 10}, 6);

		// then
		assertThat(rank.getRanking()).isEqualTo(RankType.LOST);
	}

}
