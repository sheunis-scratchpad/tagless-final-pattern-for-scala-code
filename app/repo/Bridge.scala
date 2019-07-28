package repo

import javax.inject.{Inject, Singleton}
import models.Person

@Singleton
class Bridge @Inject()() {

  def getPeople = new ScalaToLanguageBridge[Seq[Person]] {
    override def apply[Wrapper[_]](
        implicit L: Language[Wrapper]): Wrapper[Seq[Person]] = {
      val base = L.people()
      L.getPeople(base)
    }
  }

  def createPerson(name: String, year: Int) =
    new ScalaToLanguageBridge[Person] {
      override def apply[Wrapper[_]](
          implicit L: Language[Wrapper]): Wrapper[Person] = {
        val base = L.people()
        L.createPerson(base, name, year)
      }
    }
}
