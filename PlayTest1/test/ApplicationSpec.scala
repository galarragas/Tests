package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import controllers.routes


/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification with FakeSalatApp {
  "Application" should {

    "send 404 on a bad request" in {
      running(localMongoDbApplication) {
        route(FakeRequest(GET, "/boum")) must beNone
      }
    }

    "render the index page" in {
      running(localMongoDbApplication) {
        val home = route(FakeRequest(GET, "/")).get

        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html")
        contentAsString(home) must contain("Your new application is ready.")
      }
    }

    "render notes list" in {
      running(localMongoDbApplication) {
        val notesList = route(FakeRequest(GET, routes.Application.listNotes.url)).get

        status(notesList) must equalTo(OK)
      }
    }
  }
}