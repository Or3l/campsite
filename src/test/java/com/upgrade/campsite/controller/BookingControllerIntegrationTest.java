package com.upgrade.campsite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.campsite.dto.BookingPostRequest;
import com.upgrade.campsite.dto.BookingResponse;
import com.upgrade.campsite.utils.UtilsDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    private static final String ENDPOINT = "/bookings";

    private ObjectMapper objectMapper = new ObjectMapper();

//    @BeforeAll
//    @Sql({"classpath:data.sql"})
//    public static void beforeAll(){
//
//    }

    @Test
    void getAvailability() throws Exception {
        MvcResult result = mvc.perform(get("/bookings/availabilities")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        List<String> localDateList = objectMapper.readValue(result.getResponse().getContentAsString(),objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
        assertNotNull(localDateList);
        assertFalse(localDateList.isEmpty());
    }

    @Test
    @Sql(scripts = {"classpath:reset.sql"}, executionPhase = AFTER_TEST_METHOD)
    void createBooking_when_OK() throws Exception {
        String email = "test@email.com";
        String fullName = "fullName";
        LocalDate arrival = UtilsDate.getLocalDateUTC().plusDays(2);
        LocalDate departure = UtilsDate.getLocalDateUTC().plusDays(4);
        BookingPostRequest bookingPostRequest = BookingPostRequest.builder()
                .arrival(arrival.toString())
                .departure(departure.toString())
                .email(email)
                .fullName(fullName).build();

        MvcResult result = mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingPostRequest)))
                .andExpect(status().isCreated()).andReturn();

        BookingResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), BookingResponse.class);

        assertEquals(email, response.getEmail());
        assertEquals(fullName, response.getFullName());
        assertEquals(arrival.toString(), response.getArrival());
        assertEquals(departure.toString(), response.getDeparture());
        assertNotNull(response.getBookingId());
    }

    @Test
    @Sql(scripts = {"classpath:reset.sql"}, executionPhase = AFTER_TEST_METHOD)
    void createBooking_when_not_available() throws Exception {
        String email = "test@email.com";
        String fullName = "fullName";
        LocalDate arrival = UtilsDate.getLocalDateUTC().plusDays(2);
        LocalDate departure = UtilsDate.getLocalDateUTC().plusDays(4);
        BookingPostRequest bookingPostRequest = BookingPostRequest.builder()
                .arrival(arrival.toString())
                .departure(departure.toString())
                .email(email)
                .fullName(fullName).build();

        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingPostRequest)))
                .andExpect(status().isCreated()).andReturn();

        MvcResult result = mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingPostRequest)))
                .andExpect(status().is4xxClientError()).andReturn();
    }

    @Test
    @Sql(scripts = {"classpath:reset.sql"}, executionPhase = AFTER_TEST_METHOD)
    void deleteBooking() throws Exception {
        String email = "test@email.com";
        String fullName = "fullName";
        LocalDate arrival = UtilsDate.getLocalDateUTC().plusDays(2);
        LocalDate departure = UtilsDate.getLocalDateUTC().plusDays(4);
        BookingPostRequest bookingPostRequest = BookingPostRequest.builder()
                .arrival(arrival.toString())
                .departure(departure.toString())
                .email(email)
                .fullName(fullName).build();

        MvcResult result = mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingPostRequest)))
                .andExpect(status().isCreated()).andReturn();


        BookingResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), BookingResponse.class);

        mvc.perform(delete(ENDPOINT+"/"+response.getBookingId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingPostRequest)))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Sql(scripts = {"classpath:reset.sql"}, executionPhase = AFTER_TEST_METHOD)
    void updateBooking() throws Exception {
        String email = "test@email.com";
        String fullName = "fullName";
        LocalDate arrival = UtilsDate.getLocalDateUTC().plusDays(2);
        LocalDate departure = UtilsDate.getLocalDateUTC().plusDays(4);
        BookingPostRequest bookingPostRequest = BookingPostRequest.builder()
                .arrival(arrival.toString())
                .departure(departure.toString())
                .email(email)
                .fullName(fullName).build();

        MvcResult result = mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingPostRequest)))
                .andExpect(status().isCreated()).andReturn();

        BookingResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), BookingResponse.class);

        bookingPostRequest.setEmail("newEmail@email.com");
        bookingPostRequest.setFullName("newFullName");
        bookingPostRequest.setArrival(arrival.plusDays(1).toString());

        MvcResult updatedResult = mvc.perform(patch(ENDPOINT+"/"+response.getBookingId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingPostRequest)))
                .andExpect(status().isOk()).andReturn();

        BookingResponse updateResponse = objectMapper.readValue(updatedResult.getResponse().getContentAsString(), BookingResponse.class);

        assertEquals("newEmail@email.com", updateResponse.getEmail());
        assertEquals("newFullName", updateResponse.getFullName());
        assertEquals(arrival.plusDays(1).toString(), updateResponse.getArrival());
        assertEquals(departure.toString(), updateResponse.getDeparture());

    }
}