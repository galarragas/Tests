package test

import org.specs2.mutable.Around
import com.mongodb.casbah.MongoConnection
import de.flapdoodle.embed.mongo.{MongodProcess, MongodExecutable, MongodStarter}
import de.flapdoodle.embed.mongo.config.MongodConfig
import de.flapdoodle.embed.mongo.distribution.Version

trait EmbedMongoDB extends Around {
  def embedConnectionURL: String = { "localhost" }
  def embedConnectionPort: Int = { 12345 }
  def embedMongoDBVersion: Version = { Version.V2_2_1 }
  def embedDB: String = { "test" }

  lazy val runtime: MongodStarter = MongodStarter.getDefaultInstance
  lazy val mongodExe: MongodExecutable = runtime.prepare(new MongodConfig(embedMongoDBVersion, embedConnectionPort, true))
  lazy val mongod: MongodProcess = mongodExe.start()

  override def around[T](t : => T)(implicit evidence : org.specs2.execute.AsResult[T]) : org.specs2.execute.Result = {
    mongod

    try {
      evidence.asResult(t)
    } finally {
      mongod.stop()
      mongodExe.stop()
    }
  }

  lazy val mongoDB = MongoConnection(embedConnectionURL, embedConnectionPort)(embedDB)
}