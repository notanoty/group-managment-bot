package com.notanoty.demo.ChatSettings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSettingsRepository extends JpaRepository<ChatSettings, Long> {
}
