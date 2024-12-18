package com.notanoty.demo.Strike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrikeRepository extends JpaRepository<Strike, Long> {
}
