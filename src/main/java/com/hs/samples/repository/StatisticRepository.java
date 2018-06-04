package com.hs.samples.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hs.samples.model.Statistic;


@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long>{
	
}
