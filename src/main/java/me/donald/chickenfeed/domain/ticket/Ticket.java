package me.donald.chickenfeed.domain.ticket;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
@Entity
public class Ticket {

	@Id
	@Column(name = "ticket_no")
	@GeneratedValue(strategy = GenerationType.TABLE)
	@TableGenerator(name = "gen_ticket_no")
	private long ticketNo;

	@Column(name = "round")
	private int round;

	@Embedded
	private Rank rank;

	@Column(name = "ticket_type")
	@Enumerated(EnumType.STRING)
	private TicketType type;

	@Column(name = "issue_time")
	private LocalDateTime issueTime;

	@ElementCollection
	@CollectionTable(name = "ball",
			joinColumns = @JoinColumn(name = "ticket_no"))
	private List<Ball> balls;

	protected Ticket() {}

	public Ticket(int round, Integer[] requestBalls) {
		this.round = round;
		this.balls = new ArrayList<>();
		this.type = TicketType.AUTO;

		for (int number : requestBalls) {
			if (number != 0)
				this.type = TicketType.MANUAL;

			this.balls.add(new Ball(number, true));
		}
	}

	public List<Ball> getBalls() {
		return balls;
	}

	/**
	 * 티켓 발행 가능 여부 반환
	 *
	 * @return 발행 가능 여부
	 */
	private boolean checkIssuable() {
		if (this.type.equals(TicketType.AUTO))
			return true;

		return checkSuitableBlank() && checkACSSorted();
	}

	/**
	 * 요청이 없는 공란에 랜덤 값을 생성할 수 있는 지 검증
	 *
	 * @return 생성 가능 여부
	 */
	private boolean checkSuitableBlank() {
		int beforeNumber = 0;
		int beforeIdx = -1;

		for (int idx = 0; idx < 6; idx++) {
			int number = this.balls.get(idx).getNumber();
			if (number == 0)
				continue;

			if (number - beforeNumber < idx - beforeIdx)
				return false;

			beforeNumber = number;
			beforeIdx = idx;
		}

		return 45 - beforeNumber >= 5 - beforeIdx;
	}

	/**
	 * 오름차순 정렬 검증
	 *
	 * @return 오름차순 정렬 여부
	 */
	private boolean checkACSSorted() {
		int beforeNumber = 0;
		for (Ball ball : this.balls) {
			if (ball.getNumber() == 0)
				continue;

			if (beforeNumber >= ball.getNumber())
				return false;

			beforeNumber = ball.getNumber();
		}

		return true;
	}

	/**
	 * 요청에 맞게 랜덤한 번호를 생성하여 티켓 발행
	 */
	public void issueTicket() {
		if (!checkIssuable())
			throw new IllegalArgumentException("티켓을 발급할 수 없습니다.");

		int beforeNumber = 1;
		int beforeIdx = -1;

		for (int idx = 0; idx < 6; idx++) {
			int number = this.balls.get(idx).getNumber();

			if (number == 0)
				continue;

			List<Ball> generatedBalls = generateRandomBalls(beforeNumber, number, idx - beforeIdx - 1);
			fillRandomBalls(generatedBalls, beforeIdx + 1);

			beforeNumber = number;
			beforeIdx = idx;
		}

		// 오른쪽 빈 부분
		List<Ball> lastRandomBalls = generateRandomBalls(beforeNumber, 45, 5 - beforeIdx);
		fillRandomBalls(lastRandomBalls, beforeIdx + 1);

		this.issueTime = LocalDateTime.now();
	}

	/**
	 * 시작 번호부터 끝 번호 사이에 존재하는 랜덤한 번호를 크기에 맞게 생성
	 *
	 * @param beforeNumber 시작 번호
	 * @param number       끝 번호
	 * @param size         생성 크기
	 * @return 랜덤 생성된 볼 리스트
	 */
	private List<Ball> generateRandomBalls(int beforeNumber, int number, int size) {
		List<Integer> randomNumberList = new ArrayList<>();
		Random random = new Random();

		for (int i = 0; i < size; i++)
			randomNumberList.add(random.nextInt(number - beforeNumber) + beforeNumber);

		randomNumberList.sort(Integer::compareTo);

		return randomNumberList.stream().map(i -> new Ball(i, false)).collect(Collectors.toList());
	}

	/**
	 * 생성된 볼 리스트를 티켓 볼 리스트의 특정 부분부터 삽입
	 *
	 * @param generatedBalls 생성된 볼 리스트
	 * @param beginIdx       시작 부분
	 */
	private void fillRandomBalls(List<Ball> generatedBalls, int beginIdx) {
		for (Ball ball : generatedBalls) {
			this.balls.set(beginIdx, ball);
			beginIdx++;
		}
	}

	/**
	 * 티켓의 당첨 여부 판단
	 *
	 * @param winNumbers  당첨 번호
	 * @param bonusNumber 보너스 당첨 번호
	 * @return 당첨 순위
	 */
	public Rank confirmWinning(int[] winNumbers, int bonusNumber) {
		int hit = 0;

		Set<Integer> ticketNumberSet = this.balls.stream().map(Ball::getNumber).collect(Collectors.toSet());
		for (int number : winNumbers) {
			if (ticketNumberSet.contains(number))
				hit++;
		}
		Rank rank = new Rank(hit);

		if (rank.getRanking() == RankType.THIRD && ticketNumberSet.contains(bonusNumber))
			rank.bonusHit();

		return rank;
	}

	public Integer[] getTicketNumbers() {
		return this.balls.stream().map(Ball::getNumber).toArray(Integer[]::new);
	}
}
