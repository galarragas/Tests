package com.pragmasoft.tests.meeting

import org.scalatest.FunSuite
import org.scalamock.scalatest.MockFactory
import org.joda.time.{DateTimeConstants, DateTime}

class BookingAppTest extends FunSuite with MockFactory {

  def createStubbedFixture(appointments: List[AppointmentRequest]) = {
    val bookingSystem = stub[BookingSystem]
    val appointmentRequestSource = new AppointmentRequestSource {
      def iterator: Iterator[AppointmentRequest] = appointments.iterator
    }

    (appointmentRequestSource, bookingSystem)
  }

  test("Should try to register all appointments") {
    val baseDate: DateTime = new DateTime(0).withYear(2013).withMonthOfYear(DateTimeConstants.FEBRUARY)
    val appointmentRequest1 = new AppointmentRequest(
      "EMP1",
      baseDate.withDayOfMonth(12).withHourOfDay(10).withMinuteOfHour(30),
      baseDate.withDayOfMonth(13).withHourOfDay(12).withMinuteOfHour(30),
      2
    )
    val appointmentRequest2 = new AppointmentRequest(
      "EMP1",
      baseDate.withDayOfMonth(12).withHourOfDay(10).withMinuteOfHour(30),
      baseDate.withDayOfMonth(14).withHourOfDay(12).withMinuteOfHour(30),
      2
    )

    val (appointmentRequestSource, bookingSystem) = createStubbedFixture(List(appointmentRequest1, appointmentRequest2))

    val bookingApp = new BookingApp(9, 18, Some(bookingSystem))

    bookingApp.bookAppointments(appointmentRequestSource)

    inSequence {
      (bookingSystem.registerAppointmentRequest _).verify(appointmentRequest1)
      (bookingSystem.registerAppointmentRequest _).verify(appointmentRequest2)
    }
  }

  test("should retrieve appointment schedule") {
    val baseDate: DateTime = new DateTime(0).withYear(2013).withMonthOfYear(DateTimeConstants.FEBRUARY)
    val appointmentRequest1 = new AppointmentRequest(
      employeeId = "EMP1",
      submissionTime = baseDate.withDayOfMonth(12).withHourOfDay(10).withMinuteOfHour(30),
      startTime = baseDate.withDayOfMonth(13).withHourOfDay(12).withMinuteOfHour(30),
      duration = 2
    )
    val appointmentRequest2 = new AppointmentRequest(
      employeeId = "EMP1",
      submissionTime = baseDate.withDayOfMonth(12).withHourOfDay(10).withMinuteOfHour(30),
      startTime = baseDate.withDayOfMonth(14).withHourOfDay(12).withMinuteOfHour(30),
      duration = 2
    )

    val appointmentRequestSource = new AppointmentRequestSource {
      def iterator: Iterator[AppointmentRequest] = List(appointmentRequest1, appointmentRequest2).iterator
    }

    val bookingApp = new BookingApp(9,16)

    bookingApp.bookAppointments(appointmentRequestSource)

    val expectedSchedule = List((baseDate.withDayOfMonth(13).withHourOfDay(0), List(appointmentRequest1)), (baseDate.withDayOfMonth(14).withHourOfDay(0), List(appointmentRequest2)))

    assert( bookingApp.appointmentSchedule === expectedSchedule )
  }


}
