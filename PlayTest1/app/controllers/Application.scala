package controllers

import models._

import play.api.mvc._
import play.api.data.Forms._
import play.api.data._
import org.bson.types.ObjectId
import play.api.libs.concurrent.Execution.Implicits._

trait NotesComponent {
  def notes: NoteDAO
}

trait NotesController extends Controller with NotesComponent {
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def listNotes = Action {
    Async {
      notes.asyncList.map( notes => Ok(views.html.notes(notes, noteForm, searchForm))  )
    }
  }

  def getNote(id: ObjectId) = Action {
    notes.get(id).map {
      note => Ok(views.html.note(note))
    }.getOrElse(NotFound)
  }

  def findNoteByPartialTitle =  Action {
    implicit request =>
      searchForm.bindFromRequest().fold(
        formWithErrors => BadRequest(views.html.notes(List.empty, noteForm, formWithErrors)),
        partialTitle => Async {
          notes.findByPartialTitle(partialTitle).map( notes => Ok(views.html.notes(notes, noteForm, searchForm)) )
        }
      )
  }

  def deleteNote(id: ObjectId) = Action {
    notes.delete(id)
    Redirect(routes.Application.listNotes)
  }

  val searchForm = Form[String] (
    "filter" -> text
  )

  val noteForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "body" -> text
    )
      ((title, body) => Note(new ObjectId(), title, body))
      (note => Some(note.title, note.body))
  )

  def newNote = Action {
    implicit request =>  {
      noteForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.notes(List.empty, formWithErrors, searchForm)),
        newNote => {
          val createdNote = notes.create(newNote)
          createdNote match {
            case Some(id) => Redirect( routes.Application.getNote(id) )
            case None => InsufficientStorage
          }
        }
      )
    }
  }
}


object Application extends NotesController {
  def notes: NoteDAO = new SalatNoteDAO {}
}

