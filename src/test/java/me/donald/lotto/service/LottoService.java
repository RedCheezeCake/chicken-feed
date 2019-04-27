package me.donald.lotto.service;

import lombok.extern.slf4j.Slf4j;
import me.donald.lotto.entity.LottoNumber;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class LottoService {

	private final int LOTTO_NUMBER_RANGE = 45;
	private final int LOTTO_NUMBER_COUNT = 6;

	public LottoNumber generateLottoNumber() {
		Random random = new Random(System.currentTimeMillis());
		Set<Integer> numberSet = new HashSet<>();

		while (numberSet.size() < LOTTO_NUMBER_COUNT) {
			int number = random.nextInt(LOTTO_NUMBER_RANGE) + 1;
			numberSet.add(number);
		}
		log.debug("New Generated Lotto Numbers : '{}'", numberSet);

		LottoNumber lottoNumber = new LottoNumber();
		numberSet.forEach(lottoNumber::addNumber);

		return lottoNumber;
	}
}
