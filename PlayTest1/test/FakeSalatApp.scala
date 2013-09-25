package test

import play.api.test.FakeApplication
import com.github.athieriot.EmbedConnection
import de.flapdoodle.embed.mongo.distribution.Version

trait FakeSalatApp extends EmbedConnection {

  val EmbeddedMongoTestPort: Int = 12345

  override def embedConnectionPort() = {
    EmbeddedMongoTestPort
  }

  override def embedMongoDBVersion(): Version = {
    Version.V2_4_3
  }

  def inMemoryMongoDatabase(name: String = "default"): Map[String, String] = {
    val dbname: String = "testdb"
    Map(
      ("mongodb." + name + ".db" -> dbname),
      ("mongodb." + name + ".port" -> EmbeddedMongoTestPort.toString),
      ("mongodb." + name + ".replicaset.host1.host" -> "localhost"),
      ("mongodb." + name + ".replicaset.host1.port" -> EmbeddedMongoTestPort.toString),
      ("mongodb." + name + ".replicaset.host2.host" -> "localhost"),
      ("mongodb." + name + ".replicaset.host2.port" -> (EmbeddedMongoTestPort + 1).toString),
      ("mongodb." + name + ".replicaset.host3.host" -> "localhost"),
      ("mongodb." + name + ".replicaset.host3.port" -> (EmbeddedMongoTestPort + 2).toString)
    )
  }

  lazy val localMongoDbApplication =
    FakeApplication(additionalConfiguration = inMemoryMongoDatabase(), additionalPlugins = Seq("se.radley.plugin.salat.SalatPlugin"))
}