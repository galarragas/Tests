package com.pragmasoft.tests.meeting

import org.scalatest._
import com.github.nscala_time.time.Imports._
import org.joda.time.DateTimeConstants

class MeetingValidatorTest extends FlatSpec {

  val validator = new MeetingValidatorImpl(8, 17)
  val workingDay = new DateTime().withYear(2013).withMonthOfYear(DateTimeConstants.FEBRUARY).withDayOfMonth(7)
  val notWorkingDay = new DateTime().withYear(2013).withMonthOfYear(DateTimeConstants.FEBRUARY).withDayOfMonth(9)

  "A MeetingValidator" should "accept a meeting in a working day during the working hours" in {
    assert(
      validator.isValid(new AppointmentRequest(employeeId = "STE", submissionTime = DateTime.now, startTime = workingDay.withHour(8), duration = 2))
    )
  }

  it should "refuse a meeting starting after working hours" in {
    assert(
      !validator.isValid(new AppointmentRequest(employeeId = "STE", submissionTime = DateTime.now, startTime = workingDay.withHour(18), duration = 2))
    )
  }

  it should "refuse a meeting starting before working hours" in {
    assert(
      !validator.isValid(new AppointmentRequest(employeeId = "STE", submissionTime = DateTime.now, startTime = workingDay.withHour(7), duration = 2))
    )
  }

  it should "refuse a meeting ending after working hours" in {
    assert(
      !validator.isValid(new AppointmentRequest(employeeId = "STE", submissionTime = DateTime.now, startTime = workingDay.withHour(16), duration = 2))
    )
  }

  it should "refuse a meeting ending not in the same day it starts" in {
    assert(
      !validator.isValid(new AppointmentRequest(employeeId = "STE", submissionTime = DateTime.now, startTime = workingDay.withHour(16), duration = 10))
    )
  }

  it should "refuse a meeting in a not working day" in {
    assert(
      !validator.isValid(new AppointmentRequest(employeeId = "STE", submissionTime = DateTime.now, startTime = notWorkingDay.withHour(9), duration = 1))
    )
  }
}
