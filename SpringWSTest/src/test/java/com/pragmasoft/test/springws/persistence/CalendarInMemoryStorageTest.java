package com.pragmasoft.test.springws.persistence;

import com.pragmasoft.test.springws.model.Appointment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CalendarInMemoryStorageTest {
    @Mock
    private IdGenerator idGenerator;

    private CalendarInMemoryStorage storage;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        storage = new CalendarInMemoryStorage(idGenerator);
    }

    @Test
    public void shouldStoreNewResourceWithGeneratedId() {
        when(idGenerator.getNewId()).thenReturn(10);
        Appointment newAppointment = new Appointment(new Date(10000), new Date(23000), "Party");
        assertEquals(10, storage.storeNew(newAppointment));
        verify(idGenerator).getNewId();
    }

    @Test
    public void shouldRetrieveNewStoredResource() {
        Appointment newAppointment = new Appointment(new Date(10000), new Date(23000), "Party");
        Appointment expectedReturnedAppointment = new Appointment(10, new Date(10000), new Date(23000), "Party");
        when(idGenerator.getNewId()).thenReturn(10);
        assertEquals(expectedReturnedAppointment, storage.getAppointment(storage.storeNew(newAppointment)));
    }

    @Test
    public void shouldFoundNewStoredResourceWhenDeletingIt() {
        Appointment newAppointment = new Appointment(new Date(10000), new Date(23000), "Party");
        Appointment expectedReturnedAppointment = new Appointment(10, new Date(10000), new Date(23000), "Party");
        when(idGenerator.getNewId()).thenReturn(10);
        assertEquals(expectedReturnedAppointment, storage.delete(storage.storeNew(newAppointment)));
    }

    @Test
    public void shouldFoundAllStoredAppointmentsSortedByID() {
        List<Appointment> newAppointments = Arrays.asList(
                new Appointment(1, new Date(10000), new Date(23000), "Party"),
                new Appointment(2, new Date(20000), new Date(33000), "another Party"),
                new Appointment(3, new Date(30000), new Date(43000), "final Party")
        );

        for(Appointment appointment : newAppointments) {
            when(idGenerator.getNewId()).thenReturn(appointment.getId());
            storage.storeNew(appointment);
        }

        assertEquals(newAppointments, storage.getAllAppointments());
    }
}
