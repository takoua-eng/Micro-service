package tn.esprit.microservice.microfoyer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tn.esprit.microservice.microfoyer.dto.UserDto;
import tn.esprit.microservice.microfoyer.entity.Reservation;
import tn.esprit.microservice.microfoyer.service.reservation.IReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private IReservationService reservationService;
    @GetMapping("/all")
    public List<Reservation> getReservations() {
        return reservationService.getAll();
    }
    @GetMapping("/{reservation-id}")
    public Reservation retrieveReservation(@PathVariable("reservation-id") String id) {
        return reservationService.getById(id);
    }
    @PostMapping("/add")
    public Reservation addReservation(@RequestBody Reservation r) {
        return reservationService.add(r);
    }
    @DeleteMapping("/delete/{reservation-id}")
    public void removeReservation(@PathVariable("reservation-id") String id) {
        Reservation r = reservationService.getById(id);
        if (r != null) reservationService.delete(r);
    }
    @PutMapping("/update")
    public Reservation modifyReservation(@RequestBody Reservation r) {
        return reservationService.update(r);
    }

    /*@RequestMapping("/users")
    public List<UserDto> getAllUsers() {
        return reservationService.getUsers();
    }*/
    @RequestMapping("users/{id}")
    public UserDto getUserById(@PathVariable String id) {
        return reservationService.getUserById(id);
    }

    @Value("${welcome.message}")
    private String welcomeMessage;
    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }
}