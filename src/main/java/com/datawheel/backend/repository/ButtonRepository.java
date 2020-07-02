package com.datawheel.backend.repository;

import com.datawheel.backend.domain.Button;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ButtonRepository extends JpaRepository<Button, Long> {
    @Query(value = "SELECT button_counter FROM button where id = (SELECT max(id) FROM button)", nativeQuery = true)
    Optional<Long> getRecentCounterValue();

    @Query(value = "TRUNCATE table button", nativeQuery = true)
    @Modifying
    void countFromZero();

    @Query(value = "SELECT button_click_time_average_in_second FROM button where id = (SELECT max(id) FROM button)", nativeQuery = true)
    Optional<Long> getPreviousClickTimeAverage();

    @Query(value = "SELECT button_clicked_time FROM button where id = (SELECT max(id) FROM button)", nativeQuery = true)
    Optional<String> getPreviousClickTime();

    @Query(value = "SELECT * FROM button ORDER BY id DESC", nativeQuery = true)
    List<Button> findAllDESC();
}
