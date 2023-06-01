package com.madi.backend.restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madi.backend.utils.queryParams.CustomSpecificationBulider;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    private RestaurantRepository restRepo;

    @Transactional(readOnly = true)
    @Override
    public Restaurant findRestaurant(UUID id) {
        Optional<Restaurant> restaurant = restRepo.findById(id);
        return restaurant.isPresent() ? restaurant.get() : null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Restaurant> filterRestaurant(String filter) {
        CustomSpecificationBulider builder = new CustomSpecificationBulider();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(filter + ",");
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3));
        }
        Specification<Restaurant> spec = builder.build();
        return restRepo.findAll(spec);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Restaurant> listAllRestaurants() {
        return restRepo.findAll();
    }

    @Transactional
    @Override
    public Restaurant createRestaurant(Restaurant requestBody) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(requestBody.getName());
        restaurant.setAddress(requestBody.getAddress());
        restaurant.setLongitude(requestBody.getLongitude());
        restaurant.setLatitude(requestBody.getLatitude());
        restaurant.setStars(requestBody.getStars());
        restaurant.setPhone(requestBody.getPhone());
        restaurant.setDescription(requestBody.getDescription());
        restaurant.setCategories(requestBody.getCategories());

        return restRepo.save(restaurant);
    }

    @Transactional
    @Override
    public void deleteRestaurant(UUID id) {
        restRepo.deleteById(id);
    }

    @Transactional
    @Override
    public Restaurant updateRestaurant(UUID id, Restaurant newRestaurant) {
        Restaurant existedRestaurant = findRestaurant(id);
        if (existedRestaurant == null) {
            throw new ResourceNotFoundException("Restaurant " + id + " does not exist");
        }

        existedRestaurant.setName(newRestaurant.getName());
        existedRestaurant.setAddress(newRestaurant.getAddress());
        existedRestaurant.setLongitude(newRestaurant.getLongitude());
        existedRestaurant.setLatitude(newRestaurant.getLatitude());
        existedRestaurant.setStars(newRestaurant.getStars());
        existedRestaurant.setPhone(newRestaurant.getPhone());
        existedRestaurant.setDescription(newRestaurant.getDescription());
        existedRestaurant.setCategories(newRestaurant.getCategories());

        return restRepo.save(existedRestaurant);
    }
}
