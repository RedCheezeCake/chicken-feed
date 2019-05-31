package me.donald.chickenfeed.domain;

import me.donald.chickenfeed.domain.ticket.Ticket;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketTest {

	/**
	 * 티켓이 생성 가능한지 검증
	 */
	@Test
	public void checkIssuable_emptyRequest() {
		// given
		int[] requestBalls = new int[]{0,0,0,0,0,0};
		Ticket ticket = new Ticket(requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isTrue();
	}

	@Test
	public void checkIssuable_justOneBallRequest() {
		// given
		int[] requestBalls = new int[]{0,0,3,0,0,0};
		Ticket ticket = new Ticket(requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isTrue();
	}

	@Test
	public void checkIssuable_moreThanOneBallsRequest() {
		// given
		int[] requestBalls = new int[]{0,24,0,0,32,0};
		Ticket ticket = new Ticket(requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isTrue();
	}

	@Test
	public void checkIssuable_failedByNotOrdered() {
		// given
		int[] requestBalls = new int[]{0,12,0,8,0,0};
		Ticket ticket = new Ticket(requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isFalse();
	}

	@Test
	public void checkIssuable_failedByOneBallLeftSide() {
		// given
		int[] requestBalls = new int[]{0,1,0,0,0,0};
		Ticket ticket = new Ticket(requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isFalse();
	}

	@Test
	public void checkIssuable_failedByOneBallRightSide() {
		// given
		int[] requestBalls = new int[]{0,0,0,0,45,0};
		Ticket ticket = new Ticket(requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isFalse();
	}

	@Test
	public void checkIssuable_failedByOneMoreBalls() {
		// given
		int[] requestBalls = new int[]{0,12,0,13,0,0};
		Ticket ticket = new Ticket(requestBalls);

		// when
		boolean issuable = ticket.checkIssuable();

		// then
		assertThat(issuable).isFalse();
	}

	/**
	 *	티켓을 발행함
	 */
	@Test
	public void issueTicket() {

	}

	/**
	 * 해당 티켓이 당첨 등수를 확인
	 */
	@Test
	public void confirmWinningResult() {

	}
}
