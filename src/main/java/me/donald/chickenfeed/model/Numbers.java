package me.donald.chickenfeed.model;

import lombok.Getter;

@Getter
public class Numbers {

	private Integer[] numbers;

	protected Numbers() {};

	public Numbers(Integer[] numbers) {
		this.numbers = numbers;
	}

	public boolean validateLength() {
		return numbers.length == 6;
	}
}
