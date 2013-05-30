package com.pragmasoft.tests.meeting

class BookingApp(officeOpenHour : Int, officeCloseHour : Int, providedBookingSystem : Option[BookingSystem] = None) {

  val bookingSystem = providedBookingSystem match {
    case Some(bookingSyst) => bookingSyst
    case _ =>  new BookingSystemImpl(new MeetingValidatorImpl(officeOpenHour, officeCloseHour))
  }

  def bookAppointments(requestsSource : AppointmentRequestSource ) : Unit = {
    requestsSource.map(request => bookingSystem.registerAppointmentRequest(request))
  }

  def appointmentSchedule = bookingSystem.appointmentSchedule
}
