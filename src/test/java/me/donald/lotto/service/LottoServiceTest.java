package me.donald.lotto.service;

import me.donald.lotto.entity.LottoNumber;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class LottoServiceTest {

	@Autowired
	LottoService lottoService;

	@Test
	public void 중복없는_6자리_로또번호를_생성() {
		// given

		// when
		LottoNumber lottoNumber = lottoService.generateLottoNumber();
		List<Integer> numbers = lottoNumber.getNumbers();
		HashSet<Integer> numberSet = Sets.newHashSet(numbers);

		// then
		assertThat(numbers.size()).isEqualTo(6);
		assertThat(numberSet.size()).isEqualTo(6);
	}
}
