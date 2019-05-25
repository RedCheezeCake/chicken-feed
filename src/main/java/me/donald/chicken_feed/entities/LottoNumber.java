package me.donald.chicken_feed.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class LottoNumber {
	private List<Integer> numbers = new ArrayList<>();

	public void addNumber(int number) {
		numbers.add(number);
	}
}
