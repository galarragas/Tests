package com.pragmasoft.test.scalding

import com.twitter.scalding._
import com.twitter.scalding.Args
import com.twitter.scalding.Csv
import com.twitter.scalding.Job
import com.twitter.scalding.Tsv
import TDsl._

class JoinJob(args: Args) extends Job(args) {

  object PhoneBookSchema extends Enumeration {
    val first, last, phone, age, country = Value // arbitrary number of fields
  }

  object AddressesSchema extends Enumeration {
    val last, country, address = Value // arbitrary number of fields
  }

  case class AddressEntry(last: String, country: String, address: String)

  object AddressEntry {
    def fromAddressTuple(tuple: (String, String, String)): AddressEntry = AddressEntry(tuple._1, tuple._2, tuple._3)
  }

  case class PhoneBookEntry(first: String, last: String, phone: String, age: Int, country: String) {
    def toTuple = (first, last, phone, age, country)
  }

  object PhoneBookEntry {
    def fromPhoneBookTuple(tuple: (String, String, String, Int, String)): PhoneBookEntry = PhoneBookEntry(tuple._1, tuple._2, tuple._3, tuple._4, tuple._5)
  }

  case class FamilyEntryWithAddress(familyName: String, country: String, address: String, count: Long)

  import PhoneBookEntry._
  import AddressEntry._

  val groupedAddresses = Csv("addresses.txt", separator = "\t", fields = AddressesSchema)
    .read
    .toTypedPipe[(String, String, String)](AddressesSchema)
    .map(fromAddressTuple(_))
    .groupBy(addressEntry => (addressEntry.address.toLowerCase, addressEntry.country.toLowerCase))

  val phonesForFamilyWithoutFullAddress = Csv("phones.txt", separator = " ", fields = PhoneBookSchema)
    .read
    .toTypedPipe[(String, String, String, Int, String)](PhoneBookSchema)
    .map(fromPhoneBookTuple(_))
    .groupBy(phoneEntry => (phoneEntry.last.toLowerCase, phoneEntry.country.toLowerCase))


  val phoneCountForFamilyWithoutFullAddress = phonesForFamilyWithoutFullAddress.count( _ => true)

  phoneCountForFamilyWithoutFullAddress
    .groupBy( {case ((address, country), count) => (address, country)})
    .mapValues({case ((address, country), count) => count})
    .join(groupedAddresses)
    .mapValues({
      case (count, addressEntry) => FamilyEntryWithAddress(addressEntry.last, addressEntry.country, addressEntry.address, count)
    })
    .values
    .toPipe(('last, 'country, 'address, 'count))
    .write(Tsv(args("output")))
}