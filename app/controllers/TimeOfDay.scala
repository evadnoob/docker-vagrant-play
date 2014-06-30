package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import org.joda.time._

import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.{ISODateTimeFormat, DateTimeFormat}

import scala.collection.JavaConversions._

/**
  * Time REST end points
  */
object TimeOfDay extends Controller {

  val fmtIso = ISODateTimeFormat.dateTime
  val fmt = DateTimeFormat.longDateTime

  /** 
    * A lookup table to help determine timezones from abbreviations. Limited to the us, for now so that the ambiguous abbreviations are not a problem
    */
  lazy val supportedTimeZonesByLongId = DateTimeZone.getAvailableIDs.map( (id) => {
    val dtz = DateTimeZone.forID(id);
    (id, dtz.getShortName(DateTime.now.getMillis))
  }).toMap.filter({case (k, v) => { k.startsWith("US")  }})

   /** the same data as supportedTimeZonesByLongId but grouped by the values*/ 
  lazy val supportedTimeZones = supportedTimeZonesByLongId.groupBy({case(k, v) =>  v })


  /**
    * determine current time for given timezone or default to system
    *  timezone. 
    * @return a json object with a formatted as an iso date time string.
    */
  def now(timezone: Option[String]) = Action { implicit request =>
    println("you are here %sXXX".format(request.body))
    println("timezone: %s".format(timezone))
    import scala.util.control.Exception._

    /** 
      * This method attempts to determine a valid timezone
      *  for the abbreviation that is optionally passed as timezone.
      *  However, many of the common abbreviations like PST are used by
      *  many timezones, for this reason we limit the timezones to US.
      */ 
    def timezoneStringToTimeZone(str: String): Option[DateTimeZone] = {
      import scala.util.control.Exception._
      println(s"str: $str")
      
      if (str.length <= 3) {
        val longTimeZone = supportedTimeZones.get(str).map(_.head)
        println(s"longTimeZone: $longTimeZone")
        
        catching(classOf[IllegalArgumentException]) opt DateTimeZone.forID(longTimeZone.map(_._1).getOrElse(""))
      }
      else {
        catching(classOf[IllegalArgumentException]) opt DateTimeZone.forID(str)
      }
    }

    // Make sure that the timezone passed as a request parameter is a valid timezone.
    val tz: Option[DateTimeZone] = timezone match {      
      case Some(timezone) => {
        val validatedTimeZone = timezoneStringToTimeZone(timezone)
        println(s"validated TimeZone $validatedTimeZone")
        validatedTimeZone
      }
      case _ => Some(DateTimeZone.getDefault)
    }

    println("tz %s".format(tz))
    // if the request parameter timezone contained a parsable timezone
    // string, use that otherwise use the current time/timezone of
    // this machine.
    tz match {
      case Some(tz) => {
        val now = DateTime.now(tz)
        Ok(Json.obj(
          "time" -> fmt.print(now),
          "timeIso" -> fmtIso.print(now)
        ))
      }
      case _ => {
        val now = DateTime.now
        Ok(Json.obj(
          "time" -> fmt.print(now),
          "timeIso" -> fmtIso.print(now),
          "warning" -> "Unable to determine timezone %s".format(timezone.getOrElse(""))
        ))
      }
    }
  }
}

