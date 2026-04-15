package tn.esprit.microservice.microfoyer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tn.esprit.microservice.microfoyer.entity.Chambre;
import tn.esprit.microservice.microfoyer.service.chambre.IChambreService;

import java.util.List;

@RestController
@RequestMapping("/chambre")
public class ChambreController {
    @Autowired
    private IChambreService chambreService;
    @GetMapping("/all")
    public List<Chambre> getChambres() {
        return chambreService.getAll();
    }
    @GetMapping("/{chambre-id}")
    public Chambre retrieveChambre(@PathVariable("chambre-id") Long id) {
        return chambreService.getById(id);
    }
    @PostMapping("/add")
    public Chambre addChambre(@RequestBody Chambre c) {
        return chambreService.add(c);
    }
    @DeleteMapping("/delete/{chambre-id}")
    public void removeChambre(@PathVariable("chambre-id") Long id) {
        Chambre c = chambreService.getById(id);
        if (c != null) chambreService.delete(c);
    }
    @PutMapping("/update")
    public Chambre modifyChambre(@RequestBody Chambre c) {
        return chambreService.update(c);
    }

    @Value("${welcome.message}")
    private String welcomeMessage;
    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }
}
