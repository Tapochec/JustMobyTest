package com.example.justmobytest.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import com.example.justmobytest.Entity.GeoCoordinates;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
		"api.url=https://api.openweathermap.org/data/2.5/weather?lat={LAT}&lon={LON}&units=metric",
		"api.url.geo=https://example.com/geocode?cityName={cityName}"
})
class GeoCodeServiceTest {

	@Mock
	private RestTemplate restTemplate;

	@Value("${api.url.geo}")
	private String API_GEO = "https://example.com/geocode?cityName={cityName}";

	@InjectMocks
	private GeoCodeService geoCodeService;


	@Test
	void getCoordinates_ValidCity_ReturnsGeoCoordinates() {
		// Arrange
		String cityName = "TestCity";
		String expectedUrl = "https://example.com/geocode?cityName=TestCity";
		geoCodeService.setAPI_GEO(API_GEO);

		String jsonResponse = "[{\"lat\": 123, \"lon\": 456}]";
		ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		//when
		when(restTemplate.getForEntity(expectedUrl, String.class)).thenReturn(responseEntity);

		//then
		GeoCoordinates geoCoordinates = geoCodeService.getCoordinates(cityName);

		//validation
		assertNotNull(geoCoordinates);
		assertEquals(123, geoCoordinates.getLat());
		assertEquals(456, geoCoordinates.getLon());
	}

	@Test
	void getCoordinates_Error() {
		// Arrange
		String cityName = "ErrorCity";
		String expectedUrl = "https://example.com/geocode?cityName=ErrorCity";
		geoCodeService.setAPI_GEO(API_GEO);

		ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		//when
		when(restTemplate.getForEntity(expectedUrl, String.class))
				.thenReturn(responseEntity);

		//then
		RuntimeException exception = assertThrows(RuntimeException.class, () -> geoCodeService.getCoordinates(cityName));

		//validation
		assertEquals("400 BAD_REQUEST", exception.getMessage());
	}

}
