package org.apache.spot.ingest.proxy

import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spot.ingest.IngestSave
import org.apache.spot.ingest.proxy.ProxySchema._

/**
  * Created by Vartika Singh, Everardo Lopez & Ricardo Barona on 2/20/17
  *
  * Spark Streaming application for Proxy pipeline.
  * Parses and saves Proxy Logs from Blue Coat logs.
  *
  */
object BlueCoat {

  def run(dataDirectory: String, tableName: String): Unit = {

    val sparkConf = new SparkConf().setAppName("Spot Ingest Proxy - Scala")
    val sparkContext = new SparkContext(sparkConf)
    val streamingContext = new StreamingContext(sparkContext, Seconds(40))

    val splitPattern = """[ ]+(?=([^"]*"[^"]*")*[^"]*$)"""

    val dataRdd = streamingContext.textFileStream(dataDirectory).filter(isValid).map(_.split(splitPattern))

    // See ProxySchema for mapping column -> id
    val dataRow = dataRdd.map(line => Row.fromSeq(Seq(line(0), line(1), line(2).toInt, line(3), line(4), line(5), line(6),
      line(7), line(8), line(9), line(10), line(11), line(12), line(13), line(14), line(15), line(16), line(17), line(18),
      line(19), line(20), line(21), line(22).toLong, line(23).toLong, line(24), line(25), line(26)) ++ getDateParts(line(0),
      line(1)) :+ getFullURI(line(15), line(17), line(18))))

    dataRow.foreachRDD( singleRDD => {
      val sqlContext = SQLContext.getOrCreate(singleRDD.sparkContext)

      val proxyDF = sqlContext.createDataFrame(singleRDD, inputSchema)

      IngestSave.save(proxyDF.selectExpr(outputSelect: _*), outputSelect, tableName)

    })

    streamingContext.start()
    streamingContext.awaitTermination()

  }

  def isValid(text: String): Boolean = {
    val dater = """^\d{4}-\d{2}-\d{2}""".r

    dater findFirstIn text match {
      case Some(_) => true
      case None => false
    }
  }


  def getFullURI(host: String, uriPath: String, uriQuery: String): String = {
    val uriPathValidated = {
      if (uriPath.length > 1) {
        uriPath
      } else {
        ""
      }
    }

    val uriValidated = {
      if (uriQuery.length > 1) {
        uriQuery
      } else {
        ""
      }
    }
    s"$host$uriPathValidated$uriValidated"
  }

  def getDateParts(date: String, time: String): Seq[String] = {

    val parts = date.split("-")
    Seq(parts(0), f"${parts(1).toInt}%02d", f"${parts(2).toInt}%02d", f"${time.split(":")(0).toInt}%02d")
  }

}
