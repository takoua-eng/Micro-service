package tn.esprit.microservice.restaurant.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.microservice.restaurant.Entity.Meal;
import tn.esprit.microservice.restaurant.Repository.MealRepository;
import tn.esprit.microservice.restaurant.Service.MealService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;

    @Override
    public Meal createMeal(Meal meal) {
        return mealRepository.save(meal);
    }

    @Override
    public Meal getMealById(Long id) {
        return mealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meal not found: " + id));
    }

    @Override
    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    @Override
    public List<Meal> getAvailableMeals() {
        return mealRepository.findByAvailableTrue();
    }

    @Override
    public Meal updateMeal(Long id, Meal updatedMeal) {
        Meal meal = getMealById(id);
        meal.setName(updatedMeal.getName());
        meal.setPrice(updatedMeal.getPrice());
        meal.setAvailable(updatedMeal.isAvailable());
        return mealRepository.save(meal);
    }

    @Override
    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }


}