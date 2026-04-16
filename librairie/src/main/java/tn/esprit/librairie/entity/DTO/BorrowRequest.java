package tn.esprit.librairie.entity.DTO;
import lombok.Data;

@Data
public class BorrowRequest {
    private Long bookId;
    private String userId;
}
