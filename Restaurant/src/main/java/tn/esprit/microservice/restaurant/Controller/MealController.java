package tn.esprit.microservice.restaurant.Controller;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.microservice.restaurant.Entity.Meal;
import tn.esprit.microservice.restaurant.Service.MealService;

import java.util.List;


@RestController
@RequestMapping("/meals")
@RequiredArgsConstructor
@RefreshScope
public class MealController {

    private final MealService mealService;

    // ✅ Créer un repas
    @PostMapping
    public ResponseEntity<Meal> create(@RequestBody Meal meal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mealService.createMeal(meal));
    }

    // ✅ Récupérer un repas par ID
    @GetMapping("/{id}")
    public ResponseEntity<Meal> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mealService.getMealById(id));
    }

    // ✅ Récupérer tous les repas
    @GetMapping
    public ResponseEntity<List<Meal>> getAll() {
        return ResponseEntity.ok(mealService.getAllMeals());
    }

    // ✅ Récupérer uniquement les repas disponibles
    @GetMapping("/available")
    public ResponseEntity<List<Meal>> getAvailable() {
        return ResponseEntity.ok(mealService.getAvailableMeals());
    }

    // ✅ Modifier un repas
    @PutMapping("/{id}")
    public ResponseEntity<Meal> update(@PathVariable Long id, @RequestBody Meal meal) {
        return ResponseEntity.ok(mealService.updateMeal(id, meal));
    }

    // ✅ Supprimer un repas
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return ResponseEntity.ok("Meal deleted successfully");
    }

    // ✅ Changer la disponibilité d'un repas
    @PatchMapping("/{id}/availability")
    public ResponseEntity<Meal> toggleAvailability(@PathVariable Long id, @RequestParam Boolean available) {
        Meal meal = mealService.getMealById(id);
        meal.setAvailable(available);
        return ResponseEntity.ok(mealService.updateMeal(id, meal));
    }




    //de configuration (config server)

    @Value("${welcome.message}")
    private String welcomeMessage;

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }

}