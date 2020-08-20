name := "Pblog-analysis"

version := "0.1"

scalaVersion := "2.11.12"
libraryDependencies += "com.alibaba" % "fastjson" % "1.2.62"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.3" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.3" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.4.3" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.3" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-hive" % "2.4.3" % "provided"

assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
}