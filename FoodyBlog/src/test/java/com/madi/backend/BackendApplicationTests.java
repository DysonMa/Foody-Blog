package com.madi.backend;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madi.backend.comment.Comment;
import com.madi.backend.comment.CommentService;
import com.madi.backend.restaurant.Restaurant;
import com.madi.backend.restaurant.RestaurantService;
import com.madi.backend.user.User;
import com.madi.backend.user.UserService;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@TestMethodOrder(OrderAnnotation.class)
public class BackendApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private ObjectMapper objectMapper;

	private static Cookie cookie;

	@Test
	@Order(1)
	public void testWithoutLogin() throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/restaurants").headers(httpHeaders);

		MockHttpServletResponse response = mockMvc.perform(requestBuilder)
				.andDo(MockMvcResultHandlers.print())
				.andReturn()
				.getResponse();

		assertTrue(response.getStatus() == 401);
	}

	@Test
	@Order(2)
	public void testLogin() throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		JSONObject requestBody = new JSONObject()
				.put("username", "madi")
				.put("password", "HelloThisIsTestPassword");

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/login").headers(httpHeaders)
				.content(requestBody.toString());

		MockHttpServletResponse response = mockMvc.perform(requestBuilder)
				.andDo(MockMvcResultHandlers.print())
				.andReturn()
				.getResponse();

		// Add cookie
		cookie = response.getCookie("AuthToken");

		assertTrue(response.getStatus() == 200);
	}

	@Test
	@Order(3)
	public void testCreateRestaurant() throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		JSONArray categories = new JSONArray();
		categories.put("Fast Food");
		categories.put("Fried");

		JSONObject requestBody = new JSONObject()
				.put("name", "MacDonald")
				.put("address", "Taiwan, Taipei")
				.put("longitude", 121.6739)
				.put("latitude", 24.91571)
				.put("stars", 1)
				.put("phone", "0950123465")
				.put("description", "Awesome restaurant in Taiwan!")
				.put("categories", categories);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/restaurants").headers(httpHeaders)
				.cookie(cookie)
				.content(requestBody.toString());

		String response = mockMvc.perform(requestBuilder)
				.andDo(MockMvcResultHandlers.print())
				.andReturn()
				.getResponse()
				.getContentAsString();

		Restaurant restaurant = objectMapper.readValue(response, Restaurant.class);

		assertTrue(restaurant.getName().equals("MacDonald"));
		assertTrue(restaurant.getAddress().equals("Taiwan, Taipei"));
		assertTrue(restaurant.getLongitude() == 121.6739);
		assertTrue(restaurant.getLatitude() == 24.91571);
		assertTrue(restaurant.getStars() == 1);
		assertTrue(restaurant.getPhone().equals("0950123465"));
		assertTrue(restaurant.getDescription().equals("Awesome restaurant in Taiwan!"));
		assertTrue(restaurant.getCategories().equals(new ArrayList<String>(List.of("Fast Food", "Fried"))));
	}

	@Test
	@Order(4)
	public void testUpdateRestaurant() throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		Restaurant existedRestaurant = restaurantService.listAllRestaurants().get(0);

		JSONArray categories = new JSONArray();
		categories.put("Fast Food");
		categories.put("Fried Chicken");

		JSONObject requestBody = new JSONObject()
				.put("name", "KFC")
				.put("address", "Taiwan, Hsinchu")
				.put("longitude", 120.9647)
				.put("latitude", 24.80395)
				.put("stars", 3)
				.put("phone", "0912345678")
				.put("description", "Chicken Chicken!")
				.put("categories", categories);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.patch(String.format("/restaurants/%s", existedRestaurant.getId().toString())).headers(httpHeaders)
				.cookie(cookie)
				.content(requestBody.toString());

		String response = mockMvc.perform(requestBuilder)
				.andDo(MockMvcResultHandlers.print())
				.andReturn()
				.getResponse()
				.getContentAsString();

		Restaurant restaurant = objectMapper.readValue(response, Restaurant.class);

		assertTrue(restaurant.getName().equals("KFC"));
		assertTrue(restaurant.getAddress().equals("Taiwan, Hsinchu"));
		assertTrue(restaurant.getLongitude() == 120.9647);
		assertTrue(restaurant.getLatitude() == 24.80395);
		assertTrue(restaurant.getStars() == 3);
		assertTrue(restaurant.getPhone().equals("0912345678"));
		assertTrue(restaurant.getDescription().equals("Chicken Chicken!"));
		assertTrue(restaurant.getCategories().equals(new ArrayList<String>(List.of("Fast Food", "Fried Chicken"))));
	}

	@Test
	@Order(5)
	public void testCreateUser() throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		JSONObject requestBody = new JSONObject()
				.put("displayName", "Madi")
				.put("email", "madi@gmail.com")
				.put("password", "xhfadil12aha")
				.put("introduction", "Hello, my name is Madi");

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users").headers(httpHeaders).cookie(cookie)
				.content(requestBody.toString());

		String response = mockMvc.perform(requestBuilder)
				.andDo(MockMvcResultHandlers.print())
				.andReturn()
				.getResponse()
				.getContentAsString();

		User user = objectMapper.readValue(response, User.class);

		assertTrue(user.getDisplayName().equals("Madi"));
		assertTrue(user.getEmail().equals("madi@gmail.com"));
		assertTrue(user.getPassword().equals("xhfadil12aha"));
		assertTrue(user.getIntroduction().equals("Hello, my name is Madi"));
	}

	@Test
	@Order(6)
	public void testCreateUserComment() throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		User existedUser = userService.listAllUsers().get(0);

		JSONObject requestBody = new JSONObject()
				.put("content", "This restaurant is great and the food is delicious!")
				.put("tweetCount", 125)
				.put("isDraft", true);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(String.format("/users/%s/comments", existedUser.getId().toString())).headers(httpHeaders)
				.cookie(cookie)
				.content(requestBody.toString());

		String response = mockMvc.perform(requestBuilder)
				.andDo(MockMvcResultHandlers.print())
				.andReturn()
				.getResponse()
				.getContentAsString();

		Comment comment = objectMapper.readValue(response, Comment.class);

		assertTrue(comment.getContent().equals("This restaurant is great and the food is delicious!"));
		assertTrue(comment.getTweetCount() == 125);
		assertTrue(comment.getIsDraft() == true);
	}

	@Test
	@Order(7)
	public void testRetrieveDraftComment() throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		Comment existedComment = commentService.listAllComments().get(0);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/comments/drafts").headers(httpHeaders)
				.cookie(cookie);

		String response = mockMvc.perform(requestBuilder)
				.andDo(MockMvcResultHandlers.print())
				.andReturn()
				.getResponse()
				.getContentAsString();

		List<Comment> comments = objectMapper.readValue(response, new TypeReference<List<Comment>>() {
		});

		assertTrue(comments.get(0).getId().equals(existedComment.getId()));
	}

	@Test
	@Order(8)
	public void testUpdateUserComment() throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		User existedUser = userService.listAllUsers().get(0);
		UUID userId = existedUser.getId();
		UUID commentId = existedUser.getComment().get(0).getId();

		JSONObject requestBody = new JSONObject()
				.put("content", "This restaurant is great and the food is delicious! You must try it.")
				.put("tweetCount", 516)
				.put("isDraft", false);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.patch(String.format("/users/%s/comments/%s", userId.toString(), commentId.toString()))
				.headers(httpHeaders)
				.cookie(cookie)
				.content(requestBody.toString());

		String response = mockMvc.perform(requestBuilder)
				.andDo(MockMvcResultHandlers.print())
				.andReturn()
				.getResponse()
				.getContentAsString();

		Comment comment = objectMapper.readValue(response, Comment.class);

		assertTrue(comment.getContent().equals("This restaurant is great and the food is delicious! You must try it."));
		assertTrue(comment.getTweetCount() == 516);
		assertTrue(comment.getIsDraft() == false);
	}

	@Test
	@Order(9)
	public void testRetrievePublishedComment() throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		Comment existedComment = commentService.listAllComments().get(0);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/comments/published").headers(httpHeaders).cookie(cookie);

		String response = mockMvc.perform(requestBuilder)
				.andDo(MockMvcResultHandlers.print())
				.andReturn()
				.getResponse()
				.getContentAsString();

		List<Comment> comments = objectMapper.readValue(response, new TypeReference<List<Comment>>() {
		});

		assertTrue(comments.get(0).getId().equals(existedComment.getId()));
	}

}
