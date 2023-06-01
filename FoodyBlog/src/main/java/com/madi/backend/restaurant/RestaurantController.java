package com.madi.backend.restaurant;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    @Autowired
    private RestaurantServiceImpl restSvc;

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable("id") UUID id) {
        Restaurant restaurant = restSvc.findRestaurant(id);
        if (restaurant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(restaurant);
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> searchRestaurant(
            @RequestParam(name = "filter", required = false) String filter) {
        List<Restaurant> restaurants = new ArrayList<>();
        if (filter == null) {
            restaurants = restSvc.listAllRestaurants();
        } else {
            restaurants = restSvc.filterRestaurant(filter);
        }
        return ResponseEntity.ok().body(restaurants);
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant newRestaurant) {
        Restaurant restaurant = restSvc.createRestaurant(newRestaurant);

        // Send this entity location back to user in Location header
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(restaurant.getId())
                .toUri();

        return ResponseEntity.created(location).body(restaurant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Restaurant> deleteRestaurant(@PathVariable("id") UUID id) {
        restSvc.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable("id") UUID id,
            @RequestBody Restaurant newRestaurant) {
        Restaurant restaurant = restSvc.updateRestaurant(id, newRestaurant);
        return ResponseEntity.ok().body(restaurant);
    }

}
