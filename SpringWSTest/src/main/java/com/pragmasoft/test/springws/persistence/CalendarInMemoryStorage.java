package com.pragmasoft.test.springws.persistence;

import com.pragmasoft.test.springws.model.Appointment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CalendarInMemoryStorage implements CalendarStorage {
    private final IdGenerator idGenerator;
    private final Map<Integer, Appointment> storage = new ConcurrentHashMap<Integer, Appointment>();

    public CalendarInMemoryStorage() {
        this(new IdGenerator());
    }

//    @VisibleForTesting
    CalendarInMemoryStorage(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        List<Appointment> result = new ArrayList<Appointment>();

        for(Map.Entry<Integer, Appointment> appointmentEntry : storage.entrySet()) {
            result.add(appointmentEntry.getValue());
        }

        Collections.sort(result, new Comparator<Appointment>() {
            @Override
            public int compare(Appointment appointment, Appointment appointment1) {
                return new Integer(appointment.getId()).compareTo(appointment1.getId());
            }
        });

        return result;
    }

    @Override
    public Appointment getAppointment(int id) {
        return storage.get(id);
    }

    @Override
    public int storeNew(Appointment appointment) {
        int newID = idGenerator.getNewId();
        storage.put(newID, new Appointment(newID, appointment));
        return newID;
    }

    @Override
    public Appointment delete(int id) {
        return storage.remove(id);
    }
}
