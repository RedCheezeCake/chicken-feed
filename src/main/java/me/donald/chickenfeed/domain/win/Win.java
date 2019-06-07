package me.donald.chickenfeed.domain.win;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Win {

	private static final int WIN_NUMBER_SIZE = 6;
	private static final int WIN_NUMBER_SIZE_WITH_BONUS = 7;
	private static final int TOTAL_PRIZABLE_RANK_SIZE = 5;
	private static final int PERCENTAGE_PRIZABLE_RANK_SIZE = 3;
	private static final long FOURTH_RANK_PRIZE = 50000L;
	private static final long FIFTH_RANK_PRIZE = 5000L;

	@Id
	@Column(name = "round")
	private int round;

	@Column(name = "numbers")
	private Integer[] numbers;

	@Column(name = "totalPrize")
	private long totalPrize;

	@Column(name = "bonus_number")
	private int bonusNumber;

	@Column(name = "draw_date")
	private LocalDate drawDate;

	@ElementCollection
	@CollectionTable(name = "prizes",
			joinColumns = @JoinColumn(name = "round"))
	@OrderColumn(name = "rank")
	private List<Long> prizes;

	protected Win() {
	}

	/**
	 * 스케줄러를 통해 매주 일요일에 생성 예정
	 *
	 * @param round 다음 회차 번호
	 */
	public Win(int round) {
		this.round = round;
		this.drawDate = findNextDrawDate();
		this.totalPrize = 0;
	}

	private LocalDate findNextDrawDate() {
		return LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
	}

	/**
	 * 이번주 당첨금 추가 갱신
	 *
	 * @param prize 추가될 당첨금
	 */
	public void addTotalPrize(long prize) {
		this.totalPrize += prize;
	}

	/**
	 * 이번주 당첨 번호 입력
	 *
	 * @param numbers 이번주 당첨 번호
	 */
	public void setWinNumbers(List<Integer> numbers) {
		if (numbers.size() != WIN_NUMBER_SIZE_WITH_BONUS)
			throw new IllegalArgumentException("입력 값의 크기가 잘못되었습니다.");

		this.numbers = numbers.subList(0, WIN_NUMBER_SIZE).toArray(new Integer[0]);
		this.bonusNumber = numbers.get(WIN_NUMBER_SIZE_WITH_BONUS - 1);
	}

	/**
	 * 1, 2, 3 등에 해당하는 당첨금 입력
	 */
	public void setRankPrizes(List<Long> prizes) {
		if (prizes.size() != PERCENTAGE_PRIZABLE_RANK_SIZE)
			throw new IllegalArgumentException("범위를 벗어난 입력 값입니다.");

		this.prizes = new ArrayList<>();
		this.prizes.add(FOURTH_RANK_PRIZE);
		this.prizes.add(FIFTH_RANK_PRIZE);
		this.prizes.addAll(0, prizes);
	}

	/**
	 * 등수에 따른 당첨금 조회
	 *
	 * @param rank 등수
	 * @return 당첨금
	 */
	public long getRankPrize(int rank) {
		if (rank > TOTAL_PRIZABLE_RANK_SIZE || rank < 1)
			throw new IllegalArgumentException("범위를 벗어난 입력 값입니다.");

		if (this.prizes == null)
			throw new IllegalStateException("아직 당첨금이 모두 입력되지 않았습니다.");

		if (rank == 4)
			return FOURTH_RANK_PRIZE;
		else if (rank == 5)
			return FIFTH_RANK_PRIZE;
		else
			return this.prizes.get(rank - 1);
	}

	public int getRound() {
		return this.round;
	}
}
