package com.esprit.microservice.cours.service;

import com.esprit.microservice.cours.client.UserClient;
import com.esprit.microservice.cours.dto.UserDTO;
import com.esprit.microservice.cours.entity.Course;
import com.esprit.microservice.cours.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserClient userClient;

    public CourseService(CourseRepository courseRepository, UserClient userClient) {
        this.courseRepository = courseRepository;
        this.userClient = userClient;
    }

    @Transactional
    public Course createCourse(Course course) {
        course.setId(null);
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    @Transactional
    public Course updateCourse(Long id, Course course) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        existing.setTitle(course.getTitle());
        existing.setDescription(course.getDescription());
        existing.setCredits(course.getCredits());
        existing.setSemester(course.getSemester());
        return courseRepository.save(existing);
    }

    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    // -------- OpenFeign - user-ms communication --------

    public List<UserDTO> getUsers() {
        return userClient.getAllUsers();
    }

    public UserDTO getUserById(int id) {
        return userClient.getUserById(id);
    }

    // -------- Favorites scenario --------

    @Transactional
    public void saveFavoriteUser(int coursId, int userId) {
        Course course = courseRepository.findById((long) coursId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + coursId));

        Set<Integer> favorites = course.getFavoriteUsers();
        if (favorites == null) {
            favorites = new HashSet<>();
            course.setFavoriteUsers(favorites);
        }
        favorites.add(userId);
        courseRepository.save(course);
    }

    public List<UserDTO> getFavoriteUsers(int coursId) {
        Course course = courseRepository.findById((long) coursId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + coursId));

        Set<Integer> favorites = course.getFavoriteUsers();
        List<UserDTO> users = new ArrayList<>();
        if (favorites != null) {
            for (Integer userId : favorites) {
                users.add(userClient.getUserById(userId));
            }
        }
        return users;
    }
}
