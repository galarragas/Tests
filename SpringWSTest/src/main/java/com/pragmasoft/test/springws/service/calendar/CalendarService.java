package com.pragmasoft.test.springws.service.calendar;

import com.pragmasoft.test.springws.exceptions.ResourceNotFoundException;
import com.pragmasoft.test.springws.model.Appointment;
import com.pragmasoft.test.springws.persistence.CalendarStorage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/appointments")
public class CalendarService {
    @Resource
    private CalendarStorage calendarStorage;

    @PostConstruct
    protected void initStubData() {
        create(new Appointment(new Date(10000000L), new Date(10000100L), "test 1"));
        create(new Appointment(new Date(20000000L), new Date(20000100L), "test 2"));
        create(new Appointment(new Date(23000000L), new Date(23000100), "test 3"));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Appointment> getAll() {
        return calendarStorage.getAllAppointments();
    }

    @RequestMapping( value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Appointment getById(@PathVariable( "id" ) final int id) throws ResourceNotFoundException {
        Appointment result = calendarStorage.getAppointment(id);

        if (result == null) {
            throw new ResourceNotFoundException();
        }

        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public int create(@RequestBody Appointment theAppointment) {
        return calendarStorage.storeNew(theAppointment);
    }

    @RequestMapping( value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable( "id" ) final int id) throws ResourceNotFoundException {
        Appointment deleted = calendarStorage.delete(id);
        if (deleted == null) {
            throw new ResourceNotFoundException();
        }
    }
}
