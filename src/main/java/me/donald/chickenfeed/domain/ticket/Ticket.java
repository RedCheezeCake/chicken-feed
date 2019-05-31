package me.donald.chickenfeed.domain.ticket;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ticket {

	@Id
	@Column(name = "ticket_no")
	@GeneratedValue(strategy = GenerationType.TABLE)
	@TableGenerator(name = "gen_ticket_no")
	private long ticketNo;

	@Column(name = "round")
	private int round;

	@Column(name = "rank")
	private int rank;

	@Column(name = "ticket_type")
	@Enumerated(EnumType.STRING)
	private TicketType type;

	@Column(name = "issue_time")
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime issueTime;

	@ElementCollection
	@CollectionTable(name = "ball",
			joinColumns = @JoinColumn(name = "ticket_no"))
	private List<Ball> balls;

	public Ticket(int[] requestBalls) {
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
}
