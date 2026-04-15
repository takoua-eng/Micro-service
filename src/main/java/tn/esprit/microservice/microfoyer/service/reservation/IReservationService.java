package tn.esprit.microservice.microfoyer.service.reservation;

import tn.esprit.microservice.microfoyer.dto.UserDto;
import tn.esprit.microservice.microfoyer.entity.Reservation;

import java.util.List;

public interface IReservationService {

    Reservation add(Reservation reservation);
    Reservation update(Reservation reservation);
    void delete(Reservation reservation);
    List<Reservation> getAll();
    Reservation getById(String id);

    //List<UserDto> getUsers();
    UserDto getUserById(String id);

}
