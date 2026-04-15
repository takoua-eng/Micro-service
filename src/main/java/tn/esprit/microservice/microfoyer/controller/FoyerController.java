package tn.esprit.microservice.microfoyer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tn.esprit.microservice.microfoyer.entity.Foyer;
import tn.esprit.microservice.microfoyer.service.foyer.IFoyerService;

import java.util.List;

@RestController
@RequestMapping("/foyer")
public class FoyerController {
    @Autowired
    private IFoyerService foyerService;
    @GetMapping("/all")
    public List<Foyer> getFoyers() {
        return foyerService.getAll();
    }
    @GetMapping("/{foyer-id}")
    public Foyer retrieveFoyer(@PathVariable("foyer-id") Long id) {
        return foyerService.getById(id);
    }
    @PostMapping("/add")
    public Foyer addFoyer(@RequestBody Foyer f) {
        return foyerService.add(f);
    }
    @DeleteMapping("/delete/{foyer-id}")
    public void removeFoyer(@PathVariable("foyer-id") Long id) {
        Foyer f = foyerService.getById(id);
        if (f != null) foyerService.delete(f);
    }
    @PutMapping("/update")
    public Foyer modifyFoyer(@RequestBody Foyer f) {
        return foyerService.update(f);
    }

    @Value("${welcome.message}")
    private String welcomeMessage;
    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }
}
