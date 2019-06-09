package me.donald.chickenfeed.model.response;

import lombok.Getter;
import me.donald.chickenfeed.model.Numbers;

import java.time.LocalDateTime;

@Getter
public class TicketStreamElement extends Numbers {

	private LocalDateTime issueTime;

	public TicketStreamElement(Integer[] numbers, LocalDateTime issueTime) {
		super(numbers);
		this.issueTime = issueTime;
	}
}
