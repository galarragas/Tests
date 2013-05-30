package com.pragmasoft.tests.meeting

import org.scalatest.{GivenWhenThen, FunSpec}
import org.scalamock.scalatest.MockFactory
import org.joda.time.DateTime

class BookingSystemTest extends FunSpec with MockFactory with GivenWhenThen {

  def createFixture = {
    val meetingValidator = stub[MeetingValidator]
    val bookingRegistry = stub[BookingRegistry]

    (meetingValidator,
      bookingRegistry,
      new BookingSystemImpl(meetingValidator, bookingRegistry),
      new AppointmentRequest("employeeID", DateTime.now, DateTime.now, 2)
      )
  }

  describe("A booking system") {
    it("Should validate appointment request") {
      Given("An appointment request")
      val (meetingValidator, bookingRegistry, bookingSystem, request) = createFixture

      When("The request is submitted")
      bookingSystem.registerAppointmentRequest(request)

      Then("The request is passed to the validator")
      meetingValidator.isValid _ verify request
    }

    it("Should not submit to the registry an invalid appointment request") {
      val (meetingValidator, bookingRegistry, bookingSystem, request) = createFixture

      Given("An invalid appointment request")
      (meetingValidator.isValid _).when(*).returns(false)

      When("The request is submitted")
      val result = bookingSystem.registerAppointmentRequest(request)

      Then("The request is not passed to the registry")
      (bookingRegistry.addBooking _) verify (*) never()
      And("the method returns false")
      assert(result === false)
    }

    it("Should submit to the registry a valid appointment request") {
      val (meetingValidator, bookingRegistry, bookingSystem, request) = createFixture
      val addBookingResult = true

      Given("An valid appointment request")
      (meetingValidator.isValid _).when(*).returns(true)
      (bookingRegistry.addBooking _).when(*).returns(addBookingResult)

      When("The request is submitted")
      val result = bookingSystem.registerAppointmentRequest(request)

      Then("The request is passed to the validator")
      (bookingRegistry.addBooking _) verify (request)
      And("the registry.addBooking result is returned")
      assert(result === addBookingResult)
    }
  }

}
