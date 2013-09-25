package models

import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import play.api.Play.current
import mongoContext._
import play.api.libs.concurrent.Akka
import Akka.future
import com.mongodb.casbah.Imports._


case class Note(
                 id: ObjectId,
                 title: String,
                 body: String
                 )

case class StoredNote(id: ObjectId,
                      title: String,
                      body: String,
                      caseInsensitiveTitle: Option[String],
                      caseInsensitiveBody: Option[String]
                     )
object StoredNote {
  def apply(note: Note): StoredNote = StoredNote(note.id, note.title, note.body, Some(note.title.toLowerCase), Some(note.body.toLowerCase))
  def toNote(storedNote: StoredNote): Note = Note(storedNote.id, storedNote.title, storedNote.body)
}

trait MongoDaoComponent[Type <: AnyRef, IDType] {
  def dao: SalatDAO[Type, IDType]
}

trait NoteDAO extends MongoDaoComponent[StoredNote, ObjectId] {
  def get(id: ObjectId): Option[Note] = dao.findOneById(id).map(StoredNote.toNote)

  def list: List[Note] = dao.find(MongoDBObject.empty).map(StoredNote.toNote).toList

  def create(note: Note): Option[ObjectId] = dao.insert(StoredNote(note), WriteConcern.Safe)

  def delete(id: ObjectId) = dao.removeById(id)

  def asyncCreate(note: Note) = future { create(note) }

  def asyncList  = future { list }

  def findByPartialTitle(partialTitle: String) =
    future( dao.find( MongoDBObject("title" -> (".*" + partialTitle + ".*").r) ).map(StoredNote.toNote).toList )

  def migrateToCaseInsensitive(notes: List[Note]) = {
    notes.foreach( toMigrate =>
          dao.update(MongoDBObject.empty, MongoDBObject("caseInsensitiveTitle" -> toMigrate.title.toLowerCase, "caseInsensitiveBody" -> toMigrate.body.toLowerCase) )
    )
  }
}

trait FakeNoteDAO extends MongoDaoComponent[StoredNote, ObjectId] {
  def get(id: ObjectId): Option[Note] = Some(Note(new ObjectId(), "title", "body"))

  def list: List[Note] = List.empty

  def create(note: Note): Option[ObjectId] = None

  def delete(id: ObjectId) = dao.removeById(id)

  def asyncCreate(note: Note) = future { create(note) }

  def asyncList  = future { list }

  def findByPartialTitle(partialTitle: String) =
    future( dao.find( MongoDBObject("title" -> (".*" + partialTitle + ".*").r) ).map(StoredNote.toNote).toList )

  def migrateToCaseInsensitive(notes: List[Note]) = {
    notes.foreach( toMigrate =>
      dao.update(MongoDBObject.empty, MongoDBObject("caseInsensitiveTitle" -> toMigrate.title.toLowerCase, "caseInsensitiveBody" -> toMigrate.body.toLowerCase) )
    )
  }
}


trait SalatNoteDAO extends NoteDAO {
  val dao = new SalatDAO[StoredNote, ObjectId](collection = mongoCollection("notes")) {}
}