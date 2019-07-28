package controllers

import javax.inject._
import play.api.libs.json.{JsArray, JsNumber, JsObject, JsString, JsValue}
import play.api.mvc._
import repo.{Bridge, Interpreter}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(interpreter: Interpreter,
                               repo: Bridge,
                               cc: ControllerComponents)
    extends AbstractController(cc) {

  def addPerson = Action.async(parse.json) { request: Request[JsValue] =>
    val name = Try((request.body \ "name").as[String]).toOption
    val year = Try((request.body \ "year").as[Int]).toOption

    val result = for {
      n <- valueOrError(name,
                        ErrorResponse(400, "Could not find name in request"))
      y <- valueOrError(
        year,
        ErrorResponse(400, "Could not find valid year in request"))
    } yield (n, y)

    result match {
      case Right((v1, v2)) =>
        repo
          .createPerson(v1, v2)
          .apply(interpreter.slickInterpreter)
          .map(p => Ok(p.id.toString))
      case Left(e) => Future.successful(Status(e.code)(e.message))
    }
  }

  def getPeople = Action.async {
    repo.getPeople.apply(interpreter.slickInterpreter).map { people =>
      val response = JsArray(
        people.map(
          p =>
            JsObject(Seq("id" -> JsNumber(p.id),
                         "name" -> JsString(p.name),
                         "year" -> JsNumber(p.year)))))
      Ok(response)
    }
  }

  private def valueOrError[A](value: Option[A],
                              error: ErrorResponse): Either[ErrorResponse, A] =
    value match {
      case Some(v) => Right(v)
      case None    => Left(error)
    }
}
