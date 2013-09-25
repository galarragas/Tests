package test

import controllers.NotesController
import models.{Note, NoteDAO}
import org.bson.types.ObjectId
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import play.api.libs.concurrent.Akka.future
import play.api.test.Helpers._
import play.api.test._

class NotesControllerSpec extends Specification with Mockito {
  implicit val request = FakeRequest()

  trait MockedNotesControllerFixture extends Scope {
    val mockNoteDao = mock[NoteDAO]

    val notesController = new NotesController {
      def notes: NoteDAO = mockNoteDao
    }
  }


  "A NotesController" should {
    "return the list of notes retrieved from DAO" in {
      running(FakeApplication()) {
        println("Hello world")
        new MockedNotesControllerFixture {
          val expectedNotes = List(Note(new ObjectId(), "title", "body"), Note(new ObjectId(), "title1", "body1"))

          //http://code.google.com/p/specs/wiki/UsingMockito
          mockNoteDao.asyncList returns future(expectedNotes)(FakeApplication())

          val listNotesResult = notesController.listNotes()(request)

          status(listNotesResult) must equalTo(OK)

          contentAsString(listNotesResult) must equalTo(contentAsString(views.html.notes(expectedNotes, notesController.noteForm, notesController.searchForm)))

          there was one(mockNoteDao).asyncList
        }
      }
    }

    "return the single note retrieved from DAO" in {
      running(FakeApplication()) {
        println("Hello world")
        new MockedNotesControllerFixture {
          val expectedNote = Note(new ObjectId(), "title", "body")
          //          val storedNote = StoredNote(expectedNote)

          //http://code.google.com/p/specs/wiki/UsingMockito
          mockNoteDao.get(any[ObjectId]) returns Some(expectedNote)

          val objectId = new ObjectId

          val getNotesResult = notesController.getNote(objectId)(request)

          status(getNotesResult) must equalTo(OK)
          contentAsString(getNotesResult) must equalTo(contentAsString(views.html.note(expectedNote)))

          there was one(mockNoteDao).get(objectId)
        }
      }
    }
  }
}

