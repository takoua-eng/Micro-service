package tn.esprit.librairie.entity.DTO;

import lombok.Data;

@Data
public class ReservationRequest {
    private Long bookId;
    private String userId;
}