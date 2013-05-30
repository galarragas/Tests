package com.pragmasoft.tests.meeting

import org.joda.time.DateTime
import scala.collection.mutable._

trait BookingSystem {
  /**
   * This method is used to register an appointment requests.
   * It returns a bolean value representing a TEMPORARY acceptance/refusal response
   * A refusal response is a definitive one since the appointment was impossible to book because invalid or overlapping
   * to an existent one with higher priority.
   * A positive response is TEMPORARY since the appointment can be cancelled if a new one with higher priority is
   * submitted.
   *
   * The priority of the appointment is given by the submission date. Appointments can be registered out of
   * submission order
   *
   * @param request The request to submit
   * @return  True if the appointment was added to the bookings, False if it was impossible
   */
  def registerAppointmentRequest(request: AppointmentRequest) : Boolean

  def appointmentSchedule : List[(DateTime, List[AppointmentRequest])]
}

class BookingRegistry(dailyRegistryMap : DailyRegistryMap = new DailyRegistryMap) {
  def appointmentSchedule: List[(DateTime, List[AppointmentRequest])] = dailyRegistryMap.appointmentSchedule

  def addBooking(request: AppointmentRequest): Boolean = dailyRegistryMap.getBookingRegistryForDay(request.startDay).addBooking(request)
}

trait DailyBookingRegistry {
  def addBooking(request: AppointmentRequest) : Boolean

  def appointments : List[AppointmentRequest]
}

class BookingSystemImpl(meetingValidator : MeetingValidator, bookingRegistry : BookingRegistry = new BookingRegistry ) extends BookingSystem {
  def registerAppointmentRequest(request: AppointmentRequest): Boolean = {
    if(meetingValidator.isValid(request)) bookingRegistry.addBooking(request)
    else false
  }

  def appointmentSchedule : List[(DateTime, List[AppointmentRequest])] = bookingRegistry.appointmentSchedule
}

class DailyBookingRegistryImpl(val day: DateTime) extends DailyBookingRegistry {
  private var _appointments : List[AppointmentRequest] = Nil

  def appointments = _appointments

  def addBooking(request: AppointmentRequest) : Boolean = {
    val overlapping = _appointments.takeWhile(app => app overlaps request)

    if( overlapping.exists(app => app isHigherOrSamePriority request) ) {
      return false
    }

    val before = _appointments.takeWhile(app => (app.endTime isBefore request.startTime) || (app.endTime isEqual request.startTime))
    val after  = _appointments.dropWhile(app => app.startTime isBefore  request.endTime)

    _appointments = before ::: request :: after

    true
  }
}

class DailyRegistryMap {
  def appointmentSchedule: List[(DateTime, List[AppointmentRequest])] =
    dailyRegistries.map {case (day : DateTime, registry : DailyBookingRegistry) => (day, registry.appointments) }.toList.sortWith {(a, b) => (a._1 isBefore b._1)}

  private val dailyRegistries = new HashMap[DateTime, DailyBookingRegistry]

  def getBookingRegistryForDay(day: DateTime) : DailyBookingRegistry = {
    // Need to rewrite if we want to support parallel requests
    dailyRegistries.get(day) match {
      case Some(result) => result
      case None => {
        val result = new DailyBookingRegistryImpl(day)
        dailyRegistries += (day -> result)
        result
      }
    }
  }
}

