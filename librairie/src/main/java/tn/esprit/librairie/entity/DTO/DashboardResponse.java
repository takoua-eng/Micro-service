package tn.esprit.librairie.entity.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {

    private long totalBooks;
    private long availableBooks;
    private long borrowedBooks;

    private long totalBorrowings;
    private long activeBorrowings;
    private long lateBorrowings;

    private double totalPenalties;
}
