package com.pragmasoft.tests.meeting

import com.github.nscala_time.time.Imports._
import org.joda.time.DateTimeConstants

trait MeetingValidator {
  def isValid(request: AppointmentRequest): Boolean
}

class MeetingValidatorImpl(val startWorkHour: Int, val endWorkHour: Int) extends MeetingValidator {
  require(isValidHourOfDay(startWorkHour))
  require(isValidHourOfDay(endWorkHour))
  require(startWorkHour < endWorkHour)

  private def isValidHourOfDay(hour: Int): Boolean = hour > 0 && hour < 24

  private def isWorkingDay(date: DateTime) = date.getDayOfWeek() >= DateTimeConstants.MONDAY && date.getDayOfWeek() <= DateTimeConstants.FRIDAY

  private def isSameDay(aDate: DateTime, anotherDate: DateTime) = aDate.getDayOfYear == anotherDate.getDayOfYear

  private def isWorkHour(aDate: DateTime) = aDate.getHourOfDay >= startWorkHour && aDate.getHourOfDay < endWorkHour

  override def isValid(request: AppointmentRequest): Boolean =
    isWorkingDay(request.startTime) && isSameDay(request.startTime, request.endTime) && isWorkHour(request.startTime) && isWorkHour(request.endTime)

}

class AppointmentRequest(val employeeId: String, val submissionTime: DateTime, val startTime: DateTime, val duration: Int) {
  def isHigherOrSamePriority(other: AppointmentRequest) = submissionTime <= other.submissionTime

  def overlaps(other: AppointmentRequest) = {
    def isInMyRange(time: DateTime): Boolean = {
      (time >= this.startTime) && (other.startTime <= this.endTime)
    }

    (startDay == other.startDay) && (isInMyRange(other.startTime) || isInMyRange(other.endTime))
  }

  require(duration > 0)
  require(duration < 24)

  def endTime = startTime.plusHours(duration)

  def startDay = new DateTime().withYear(startTime.getYear).withDayOfYear(startTime.getDayOfYear).withTimeAtStartOfDay()

  override def equals(that: Any): Boolean = that match {
    case that: AppointmentRequest =>
      (this.employeeId == that.employeeId) &&
        (this.submissionTime == that.submissionTime) &&
        (this.startTime == that.startTime) &&
        (this.duration == duration)
    case _ => false
  }

  override def hashCode(): Int =
    41 * (
         41 *
           (
             41 * (
                  41 + duration
               ) + startTime.hashCode()
           )  + submissionTime.hashCode()
      )  + employeeId.hashCode

  override def toString() : String =  "AppointmentRequest [employeeId: " + employeeId + " submissionTime: " + submissionTime + " startTime: " + startTime + " duration: " + duration + "]"
}
