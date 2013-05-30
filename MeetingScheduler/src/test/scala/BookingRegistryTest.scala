package com.pragmasoft.tests.meeting

import org.scalatest.{BeforeAndAfter, GivenWhenThen, FunSuite, FlatSpec}
import org.scalatest.matchers.ShouldMatchers
import org.joda.time.{DateTimeConstants, DateTime}
import org.scalamock.scalatest.MockFactory
import org.scalamock.StubFunction0

class BookingRegistryTest extends FunSuite with MockFactory {

  def createFixture = {
    val dailyMap = stub[DailyRegistryMap]
    val dailyRegistry = stub[DailyBookingRegistry]
    val bookingRegistry = new BookingRegistry(dailyMap)
    val request: AppointmentRequest = new AppointmentRequest(employeeId = "employeeID", submissionTime = DateTime.now, startTime = DateTime.now, duration = 2)

    (dailyMap.getBookingRegistryForDay _).when(*).returns(dailyRegistry)

    (dailyMap, bookingRegistry, dailyRegistry, request)
  }

  test("Should retrieve daily registry from map") {
    val (dailyMap, bookingRegistry, dailyRegistry, request) = createFixture

    bookingRegistry.addBooking(request)

    (dailyMap.getBookingRegistryForDay _).verify(request.startDay)
  }

  test("Should add to retrieved daily registry") {
    val (_, bookingRegistry, dailyRegistry, request) = createFixture

    bookingRegistry.addBooking(request)

    (dailyRegistry.addBooking _).verify(request)
  }

  test("Should return daily registry result") {
    val (_, bookingRegistry, dailyRegistry, request) = createFixture

    (dailyRegistry.addBooking _).when(*).returns(true)

    assert(bookingRegistry.addBooking(request))
  }

//  test("Should return daily schedule") {
//    val (dailyMap, bookingRegistry, dailyRegistry, request) = createStubbedFixture
//
//    val schedule = List((request.startDay, List(request)))
//
//    (dailyMap.appointmentSchedule).asInstanceOf[StubFunction0[List[(DateTime, List[AppointmentRequest])]]].when().returns(schedule)
//
//    assert(bookingRegistry.appointmentSchedule sameElements schedule)
//  }
}

class DailyBookingRegistryImplTest extends FlatSpec with ShouldMatchers with GivenWhenThen {

  def prepopulatedListFixture = {

  }

  "A DailyBookingRegistry" should "return success if adding a request when empty" in {
    Given("an empty registry")
    val bookingRegistry = new DailyBookingRegistryImpl(DateTime.now)

    When("adding a new appointment")
    Then("shoudl return true")
    bookingRegistry.addBooking(new AppointmentRequest("employeeID", DateTime.now, DateTime.now, 2)) should equal(true)
  }

  it should "add a request when empty" in {
    Given("an empty registry")
    val bookingRegistry = new DailyBookingRegistryImpl(DateTime.now)

    When("adding a new appointment")
    val request: AppointmentRequest = new AppointmentRequest("employeeID", DateTime.now, DateTime.now, 2)
    bookingRegistry.addBooking(request)

    Then("it should be in the appointment list")
    bookingRegistry.appointments should contain(request)
  }

  it should "fail if adding the a lower priority request for a busy time" in {
    val bookingRegistry = new DailyBookingRegistryImpl(DateTime.now)

    val meetingStart = DateTime.now.withHourOfDay(10).withMinuteOfHour(0)

    val request: AppointmentRequest = new AppointmentRequest(employeeId = "employeeID", submissionTime = DateTime.now, startTime = meetingStart, duration = 2)
    val overlapping: AppointmentRequest = new AppointmentRequest(employeeId = "employeeID", submissionTime = DateTime.now.plusSeconds(1), startTime = meetingStart, duration = 2)

    bookingRegistry.addBooking(request)
    bookingRegistry.addBooking(overlapping) should equal(false)

    bookingRegistry.appointments should not contain(overlapping)
    bookingRegistry.appointments should contain(request)
  }

  it should "fail if adding the a lower priority request overlapping a busy time" in {
    val bookingRegistry = new DailyBookingRegistryImpl(DateTime.now)

    val meetingStart = new DateTime().withYear(2013).withMonthOfYear(DateTimeConstants.FEBRUARY).withDayOfMonth(10)

    val request: AppointmentRequest = new AppointmentRequest(employeeId = "employeeID", submissionTime = DateTime.now, startTime = meetingStart, duration = 2)
    val overlapping: AppointmentRequest = new AppointmentRequest(employeeId = "employeeID", submissionTime = DateTime.now.plusSeconds(1),
      startTime = meetingStart.minusHours(1), duration = 2)

    bookingRegistry.addBooking(request)
    bookingRegistry.addBooking(overlapping) should equal(false)

    bookingRegistry.appointments should not contain(overlapping)
    bookingRegistry.appointments should contain(request)
  }

  it should "succeed if adding a higher priority for a busy time" in {
    val bookingRegistry = new DailyBookingRegistryImpl(DateTime.now)

    val meetingStart = DateTime.now.withHourOfDay(10).withMinuteOfHour(0)

    val request: AppointmentRequest = new AppointmentRequest(employeeId = "employeeID", submissionTime = DateTime.now, startTime = meetingStart, duration = 2)
    val overlapping: AppointmentRequest = new AppointmentRequest(employeeId = "employeeID", submissionTime = request.submissionTime.minusSeconds(1), startTime = meetingStart, duration = 2)

    bookingRegistry.addBooking(request)
    bookingRegistry.addBooking(overlapping) should equal(true)

    bookingRegistry.appointments should contain(overlapping)
    bookingRegistry.appointments should not contain(request)
  }

  it should "succeed if adding a higher priority for a partially overlapping time" in {
    val bookingRegistry = new DailyBookingRegistryImpl(DateTime.now)

    val meetingStart = DateTime.now.withHourOfDay(10).withMinuteOfHour(0)

    val request: AppointmentRequest = new AppointmentRequest(employeeId = "employeeID", submissionTime = DateTime.now, startTime = meetingStart, duration = 2)
    val overlapping: AppointmentRequest = new AppointmentRequest(employeeId = "employeeID", submissionTime = request.submissionTime.minusSeconds(1),
      startTime = meetingStart.minusHours(1), duration = 2)

    bookingRegistry.addBooking(request)
    bookingRegistry.addBooking(overlapping) should equal(true)

    bookingRegistry.appointments should contain(overlapping)
    bookingRegistry.appointments should not contain(request)
  }
}
