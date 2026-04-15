package tn.esprit.microservice.microfoyer.service.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.microservice.microfoyer.UserClient;
import tn.esprit.microservice.microfoyer.dto.UserDto;
import tn.esprit.microservice.microfoyer.entity.Reservation;
import tn.esprit.microservice.microfoyer.repository.IReservationRepository;

import java.util.List;

@Service
public class ReservationService implements IReservationService {

    @Autowired
    private IReservationRepository reservationRepository;

    @Override
    public Reservation add(Reservation reservation) { return reservationRepository.save(reservation); }

    @Override
    public Reservation update(Reservation reservation) { return reservationRepository.save(reservation); }

    @Override
    public void delete(Reservation reservation) { reservationRepository.delete(reservation); }

    @Override
    public List<Reservation> getAll() { return reservationRepository.findAll(); }

    @Override
    public Reservation getById(String id) { return reservationRepository.findById(id).orElse(null); }

    @Autowired
    private UserClient userServiceClient;

    /*@Override
    public List<UserDto> getUsers() {
        return userServiceClient.getAllUsers();
    }*/

    @Override
    public UserDto getUserById(String id) {
        return userServiceClient.getUserById(id);
    }
}
