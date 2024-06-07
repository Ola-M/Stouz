package com.example.stouz;

import android.location.Location;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;

import com.example.stouz.models.Restaurant;
import com.example.stouz.repositories.RestaurantRepository;
import com.example.stouz.ui.home.HomeFragment;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class HomeFragmentTest {

    @Mock
    private FusedLocationProviderClient fusedLocationClientMock;
    @Mock
    private RestaurantRepository restaurantRepositoryMock;

    private FragmentScenario<HomeFragment> scenario;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        scenario = FragmentScenario.launchInContainer(HomeFragment.class);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onFragment(fragment -> {
            fragment.fusedLocationClient = fusedLocationClientMock;
        });
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void testFilterRestaurants() {
        Restaurant restaurant1 = new Restaurant("1", "Restaurant 1", "8 AM - 10 PM", 4.5, "url1", 0, 0, 0, Collections.emptyList(), null, Collections.emptyList(), 0);
        Restaurant restaurant2 = new Restaurant("2", "Restaurant 2", "8 AM - 10 PM", 4.0, "url2", 0, 0, 0, Collections.emptyList(), null, Collections.emptyList(), 0);
        Restaurant restaurant3 = new Restaurant("3", "Another Restaurant", "8 AM - 10 PM", 3.5, "url3", 0, 0, 0, Collections.emptyList(), null, Collections.emptyList(), 0);
        scenario.onFragment(fragment -> {
            fragment.restaurantList.addAll(Arrays.asList(restaurant1, restaurant2, restaurant3));
            fragment.filter("Restaurant");
            List<Restaurant> filteredList = fragment.filteredList;
            assertEquals(2, filteredList.size());
            assertTrue(filteredList.contains(restaurant1));
            assertTrue(filteredList.contains(restaurant2));
        });
    }

    @Test
    public void testFetchRestaurants() {
        Location mockLocation = mock(Location.class);
        when(mockLocation.getLatitude()).thenReturn(52.0);
        when(mockLocation.getLongitude()).thenReturn(21.0);

        Restaurant restaurant1 = new Restaurant("1", "Restaurant 1", "8 AM - 10 PM", 4.5, "url1", 0, 0, 0, Collections.emptyList(), null, Collections.emptyList(), 0);
        Restaurant restaurant2 = new Restaurant("2", "Restaurant 2", "8 AM - 10 PM", 4.0, "url2", 0, 0, 0, Collections.emptyList(), null, Collections.emptyList(), 0);
        Restaurant restaurant3 = new Restaurant("3", "Another Restaurant", "8 AM - 10 PM", 3.5, "url3", 0, 0, 0, Collections.emptyList(), null, Collections.emptyList(), 0);

        doAnswer(invocation -> {
            RestaurantRepository.DataStatus callback = invocation.getArgument(0);
            callback.DataIsLoaded(Arrays.asList(restaurant1, restaurant2));
            return null;
        }).when(restaurantRepositoryMock).getRestaurantsList(any());

        scenario.onFragment(fragment -> fragment.fetchRestaurants(mockLocation));

        scenario.onFragment(fragment -> {
            assertEquals(2, fragment.restaurantList.size());
            assertEquals(restaurant1, fragment.restaurantList.get(0));
        });
    }

    @Test
    public void testSortRestaurantsByProximity() {
        Location userLocation = mock(Location.class);
        when(userLocation.getLatitude()).thenReturn(52.0);
        when(userLocation.getLongitude()).thenReturn(21.0);

        Restaurant restaurant1 = new Restaurant("1", "Restaurant 1", "8 AM - 10 PM", 4.5, "url1", 0, 0, 0, Collections.emptyList(), null, Collections.emptyList(), 0);
        Restaurant restaurant2 = new Restaurant("2", "Restaurant 2", "8 AM - 10 PM", 4.0, "url2", 0, 0, 0, Collections.emptyList(), null, Collections.emptyList(), 0);
        Restaurant restaurant3 = new Restaurant("3", "Another Restaurant", "8 AM - 10 PM", 3.5, "url3", 0, 0, 0, Collections.emptyList(), null, Collections.emptyList(), 0);
        scenario.onFragment(fragment -> {
            fragment.restaurantList.addAll(Arrays.asList(restaurant1, restaurant2));
            fragment.sortRestaurantsByProximity(userLocation);

            assertEquals(restaurant1, fragment.restaurantList.get(0));
            assertEquals(restaurant2, fragment.restaurantList.get(1));
        });
    }
}
