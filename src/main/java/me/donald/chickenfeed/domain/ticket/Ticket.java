package me.donald.chickenfeed.domain.ticket;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

	public List<Ball> getBalls() {
		return balls;
	}

	public Ticket(int round, int[] requestBalls) {
		this.round = round;

		this.balls = new ArrayList<>();
		this.type = TicketType.AUTO;

		for (int number : requestBalls) {
			if (number != 0)
				this.type = TicketType.MANUAL;

			this.balls.add(new Ball(number));
		}
	}

	public boolean checkIssuable() {
		if (this.type.equals(TicketType.AUTO))
			return true;

		return checkSuitableBlank() && checkACSSorted();
	}

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

	public void issueTicket() {
		int beforeNumber = 0;
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

	private List<Ball> generateRandomBalls(int beforeNumber, int number, int size) {
		List<Integer> randomNumberList = new ArrayList<>();
		Random random = new Random();

		for (int i = 0; i < size; i++)
			randomNumberList.add(random.nextInt(number - beforeNumber) + beforeNumber);

		randomNumberList.sort(Integer::compareTo);

		return randomNumberList.stream().map(Ball::new).collect(Collectors.toList());
	}

	private void fillRandomBalls(List<Ball> generatedBalls, int beginIdx) {
		for (Ball ball : generatedBalls) {
			this.balls.set(beginIdx, ball);
			beginIdx++;
		}
	}

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
}
