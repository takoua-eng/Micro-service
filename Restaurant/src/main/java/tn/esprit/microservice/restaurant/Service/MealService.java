package tn.esprit.microservice.restaurant.Service;


import tn.esprit.microservice.restaurant.Entity.Meal;

import java.util.List;

public interface MealService {

    Meal createMeal(Meal meal);
    Meal getMealById(Long id);
    List<Meal> getAllMeals();
    List<Meal> getAvailableMeals();
    Meal updateMeal(Long id, Meal meal);
    void deleteMeal(Long id);
}
