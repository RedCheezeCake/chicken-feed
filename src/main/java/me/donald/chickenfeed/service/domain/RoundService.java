package me.donald.chickenfeed.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.donald.chickenfeed.domain.win.Win;
import me.donald.chickenfeed.domain.win.repository.WinRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoundService {

	private final WinRepository winRepository;

	public int getLastRound() {
		return winRepository.findLastRound()
				.orElse(new Win(0))
				.getRound();
	}
}
