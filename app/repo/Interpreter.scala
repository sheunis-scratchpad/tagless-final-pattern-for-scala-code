package repo

import javax.inject.{Inject, Singleton}
import models.Person
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Interpreter @Inject()(dbConfigProvider: DatabaseConfigProvider)(
    implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class PeopleTable(tag: Tag) extends Table[Person](tag, "people") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def year = column[Int]("year")

    def * = (id, name, year) <> ((Person.apply _).tupled, Person.unapply)
  }

  val slickInterpreter = new Language[Future] {
    private val slickPersonQuery = TableQuery[PeopleTable]

    override type QueryObj = Query[PeopleTable, Person, Seq]

    override def people(): Future[Raw] = {
      Future.successful(Raw(slickPersonQuery))
    }

    override def getPeople(rawQ: Future[Raw]): Future[Seq[Person]] = {
      rawQ.flatMap(query => db.run(query.q.result))
    }

    override def createPerson(rawQ: Future[Raw],
                              name: String,
                              year: Int): Future[Person] = {
      rawQ.flatMap(query =>
        db.run {
          (query.q.map(p => (p.name, p.year))
            returning slickPersonQuery.map(_.id)
            into ((nameYear, id) => Person(id, nameYear._1, nameYear._2))) += (name, year)
      })
    }
  }
}
