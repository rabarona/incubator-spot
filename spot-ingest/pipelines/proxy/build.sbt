name := "spot-ingest"

version := "1.0"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.2.6"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.6.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.6.0"
libraryDependencies += "org.apache.spark" %% "spark-hive" % "1.6.0" % "provided"