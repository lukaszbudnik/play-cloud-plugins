import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play-cloud-plugins"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "net.databinder" %% "dispatch-http" % "0.8.7",
      "net.databinder" %% "dispatch-mime" % "0.8.7",
      "net.databinder" %% "dispatch-json" % "0.8.7"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    )

}
