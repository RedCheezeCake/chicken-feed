package me.donald.chickenfeed.domain.win.repository;

import me.donald.chickenfeed.domain.win.Win;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WinRepository extends JpaRepository<Win, Integer> {

	@Query("select max(w.round) from Win w")
	Optional<Win> findLastRound();
}
