package tn.esprit.microservice.microfoyer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tn.esprit.microservice.microfoyer.entity.Bloc;
import tn.esprit.microservice.microfoyer.service.bloc.IBlocService;

import java.util.List;

@RestController
@RequestMapping("/bloc")
public class BlocController {
    @Autowired
    private IBlocService blocService;
    @GetMapping("/all")
    public List<Bloc> getBlocs() {
        return blocService.getAll();
    }
    @GetMapping("/{bloc-id}")
    public Bloc retrieveBloc(@PathVariable("bloc-id") Long id) {
        return blocService.getById(id);
    }
    @PostMapping("/add")
    public Bloc addBloc(@RequestBody Bloc b) {
        return blocService.add(b);
    }
    @DeleteMapping("/delete/{bloc-id}")
    public void removeBloc(@PathVariable("bloc-id") Long id) {
        Bloc b = blocService.getById(id);
        if (b != null) blocService.delete(b);
    }
    @PutMapping("/update")
    public Bloc modifyBloc(@RequestBody Bloc b) {
        return blocService.update(b);
    }

    @Value("${welcome.message}")
    private String welcomeMessage;
    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }
}
