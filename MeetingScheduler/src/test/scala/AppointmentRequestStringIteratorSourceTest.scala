
package com.pragmasoft.tests.meeting

import org.scalatest.{GivenWhenThen, FlatSpec}
import org.scalatest.matchers.ShouldMatchers
import collection.immutable.Stream.Empty
import org.joda.time.DateTime

class AppointmentRequestStringIteratorSourceTest extends FlatSpec with ShouldMatchers with GivenWhenThen {

  behavior of "An AppointmentRequestStringIteratorSource"

  it should "declare EOF if no input" in {
    (new AppointmentRequestStringIteratorSource(Empty.iterator).iterator.hasNext) should equal(false)
  }

  it should "declare EOF if not enough input" in {
    (new AppointmentRequestStringIteratorSource((List("2011-03-17 10:17:06 EMP001").iterator)).iterator.hasNext) should equal(false)
  }

  it should "not declare EOF if enough input" in {
    val appointmentLines: List[String] = List("2011-03-17 10:17:06 EMP001", "2011-03-21 09:00 2")

    (new AppointmentRequestStringIteratorSource(appointmentLines.iterator).iterator.hasNext) should equal(true)
  }

  it should "read appointment" in {
    val appointmentLines: List[String] = List("2011-03-17 10:17:06 EMP001", "2011-03-21 09:00 2")
    val nextAppointment = (new AppointmentRequestStringIteratorSource(appointmentLines.iterator)).iterator.next

    nextAppointment should equal(
      new AppointmentRequest(
        "EMP001",
        new DateTime(0).withYear(2011).withMonthOfYear(3).withDayOfMonth(17).withHourOfDay(10).withMinuteOfHour(17).withSecondOfMinute(6),
        new DateTime(0).withYear(2011).withMonthOfYear(3).withDayOfMonth(21).withHourOfDay(9).withMinuteOfHour(0).withSecondOfMinute(0),
        2
      )
    )
  }

  it should "read more than one appointment" in {
    val appointmentLines: List[String] = List(
      "2011-03-17 10:17:06 EMP001", "2011-03-21 09:00 2",
      "2011-03-16 12:34:56 EMP002", "2011-03-21 09:00 2"
    )

    val appointment1: AppointmentRequest = new AppointmentRequest(
      "EMP001",
      new DateTime(0).withYear(2011).withMonthOfYear(3).withDayOfMonth(17).withHourOfDay(10).withMinuteOfHour(17).withSecondOfMinute(6),
      new DateTime(0).withYear(2011).withMonthOfYear(3).withDayOfMonth(21).withHourOfDay(9).withMinuteOfHour(0).withSecondOfMinute(0),
      2
    )

    val appointment2: AppointmentRequest = new AppointmentRequest(
      "EMP002",
      new DateTime(0).withYear(2011).withMonthOfYear(3).withDayOfMonth(16).withHourOfDay(12).withMinuteOfHour(34).withSecondOfMinute(56),
      new DateTime(0).withYear(2011).withMonthOfYear(3).withDayOfMonth(21).withHourOfDay(9).withMinuteOfHour(0).withSecondOfMinute(0),
      2
    )

    val appointmentSource = (new AppointmentRequestStringIteratorSource(appointmentLines.iterator))

    appointmentSource.toSeq should equal (List(appointment1, appointment2).toSeq)

  }
}
