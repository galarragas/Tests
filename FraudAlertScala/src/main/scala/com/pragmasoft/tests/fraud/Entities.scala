package com.pragmasoft.tests.fraud

import com.github.nscala_time.time.Imports._

case class Transaction(time: DateTime, cardNumber: String, amount: BigDecimal)
case class Alert(transaction: Transaction, message: String)