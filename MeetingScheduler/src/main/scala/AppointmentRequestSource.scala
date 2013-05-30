package com.pragmasoft.tests.meeting

import java.text.SimpleDateFormat
import org.joda.time.DateTime

trait AppointmentRequestSource extends Iterable[AppointmentRequest] {
}

class AppointmentRequestStringIteratorSource(fileLinesIterator: Iterator[String]) extends AppointmentRequestSource {
  val _iterator = new AppointmentRequestFileIterator(fileLinesIterator)

  def iterator: Iterator[AppointmentRequest] =  _iterator
}

class AppointmentRequestFileIterator(fileLinesIterator: Iterator[String]) extends Iterator[AppointmentRequest] {
  private var nextElem = readNextRequest

  def readNextRequest : Option[AppointmentRequest] = {
    def getLastToken(line: String): String = {
      line.substring(line.lastIndexOf(' ') + 1)
    }

    //Format:
    //      [request submission time, in the format YYYY-MM-DD HH:MM:SS] [ARCH:employee id]
    //      [meeting start time, in the format YYYY-MM-DD HH:MM] [ARCH:meeting duration in hours]
    if (!fileLinesIterator.hasNext) {
      return None
    }
    val firstLine = fileLinesIterator.next().trim

    if (!fileLinesIterator.hasNext) {
      return None
    }

    val secondLine = fileLinesIterator.next().trim

    try {
      val submissionTime = readSubmissionDate(firstLine)
      val employeeId = readId(getLastToken(firstLine))
      val startTime = readStartDate(secondLine)
      val duration = readDuration(getLastToken(secondLine))

      Some(new AppointmentRequest(employeeId, submissionTime, startTime, duration))
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
        None
    }
  }

  def hasNext: Boolean = !nextElem.isEmpty

  def next(): AppointmentRequest = {
    if (hasNext) {
      val result = nextElem
      nextElem = readNextRequest
      result.get
    }
    else {
      null
    }
  }

  def readSubmissionDate(input: String): DateTime = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    new DateTime(dateFormat.parse(input).getTime)
  }

  def readStartDate(input: String): DateTime = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm")

    new DateTime(dateFormat.parse(input).getTime)
  }

  def readId(input: String): String = {
    return input
  }

  def readDuration(input: String): Int = {
    return Integer.parseInt(input)
  }
}

