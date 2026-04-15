package tn.esprit.librairie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.librairie.entity.Borrowing;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    List<Borrowing> findByReturnDateIsNull();

    List<Borrowing> findByDueDateBeforeAndReturnDateIsNull(LocalDate date);

    List<Borrowing> findByUserId(String userId);
}
