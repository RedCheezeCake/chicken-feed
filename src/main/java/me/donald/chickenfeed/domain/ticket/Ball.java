package me.donald.chickenfeed.domain.ticket;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Ball {

	@Column(name = "number")
	private int number;

	@Column(name = "manual")
	private boolean manual;

	protected Ball() {}

	Ball(int number) {
		this.number = number;

		if (number != 0)
			manual = true;
	}

	@Override
	public String toString() {
		return "Ball{" +
				"number=" + number +
				", manual=" + manual +
				'}';
	}
}
