package com.hs.samples.provider;

import com.hs.samples.model.dto.StatisticDTO;

public class StatisticDTOProvider {
	
	public StatisticDTO createStatisticDTO() {
		return new StatisticDTO(12.5, 12.5, 12.5, 12.5, 1);
	}
	
}
