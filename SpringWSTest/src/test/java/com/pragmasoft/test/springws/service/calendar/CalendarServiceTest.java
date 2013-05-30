package com.pragmasoft.test.springws.service.calendar;

import com.pragmasoft.test.springws.exceptions.ResourceNotFoundException;
import com.pragmasoft.test.springws.model.Appointment;
import com.pragmasoft.test.springws.persistence.CalendarStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CalendarServiceTest {
    @Mock
    private CalendarStorage calendarStorage;

    @InjectMocks
    private CalendarService calendarService = new CalendarService();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnAllAvailableCalendars() {
        List<Appointment> appointments = Arrays.asList(new Appointment(1, new Date(1000L), new Date(1100L), "Buy bread"),
                new Appointment(2, new Date(2000L), new Date(2100L), "Buy bread"));
        when(calendarStorage.getAllAppointments()).thenReturn(appointments);
        assertEquals(appointments, calendarService.getAll());
    }

    @Test
    public void shouldRetrieveCalendarById() throws ResourceNotFoundException {
        Appointment theAppointment = new Appointment(new Date(1000L), new Date(1200L), "Buy bread");
        when(calendarStorage.getAppointment(anyInt())).thenReturn(theAppointment);
        assertEquals(theAppointment, calendarService.getById(10));
        verify(calendarStorage).getAppointment(eq(10));
    }

    @Test
    public void shouldCreateAnAppointment() {
        Appointment theAppointment = new Appointment(new Date(1000L), new Date(1100L), "Buy bread");
        when(calendarStorage.storeNew(any(Appointment.class))).thenReturn(100);
        assertEquals(100, calendarService.create(theAppointment));
        verify(calendarStorage).storeNew(eq(theAppointment));
    }

    @Test
    public void shouldDeleteAnAppointment() throws ResourceNotFoundException {
        when(calendarStorage.delete(anyInt())).thenReturn(new Appointment(100, new Date(1000L), new Date(1100L), "Buy bread"));
        calendarService.delete(100);
        verify(calendarStorage).delete(eq(100));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldFailDeleteIfAppointmentDoesNotExists() throws ResourceNotFoundException {
        when(calendarStorage.delete(anyInt())).thenReturn(null);
        calendarService.delete(100);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldFailRetrieveIfAppointmentDoesNotExists() throws ResourceNotFoundException {
        when(calendarStorage.getAppointment(anyInt())).thenReturn(null);
        calendarService.getById(100);
    }
}
