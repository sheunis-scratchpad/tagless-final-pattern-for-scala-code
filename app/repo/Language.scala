package repo

import models.Person

trait Language[Wrapper[_]] {
  type QueryObj
  case class Raw(q: QueryObj)

  def people(): Wrapper[Raw]

  def getPeople(query: Wrapper[Raw]): Wrapper[Seq[Person]]

  def createPerson(query: Wrapper[Raw],
                   name: String,
                   year: Int): Wrapper[Person]
}
