package com.madi.backend.restaurant;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    Restaurant findRestaurant(UUID id);

    List<Restaurant> filterRestaurant(String filter);

    List<Restaurant> listAllRestaurants();

    Restaurant createRestaurant(Restaurant requestBody);

    void deleteRestaurant(UUID id);

    Restaurant updateRestaurant(UUID id, Restaurant newRestaurant);
}
