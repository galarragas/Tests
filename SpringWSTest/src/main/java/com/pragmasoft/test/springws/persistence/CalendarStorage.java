package com.pragmasoft.test.springws.persistence;

import com.pragmasoft.test.springws.model.Appointment;

import java.util.List;

public interface CalendarStorage {
    List<Appointment> getAllAppointments();

    Appointment getAppointment(int id);

    int storeNew(Appointment appointment);

    Appointment delete(int id);
}
