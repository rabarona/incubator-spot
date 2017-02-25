package org.apache.spot.ingest.proxy

/***********************************************************************************************************************
  * Created by Ricardo Barona 02/24/2017
  * Main proxy ingest program.
  *
  * Execution: spark-submit --class "org.apache.spot.ingest.proxy.ProxyIngest" spot-ingest_2.10-1.0.jar <hdfs folder>
  *   <hive database.table name>
  * Where: hdfs folder = a HDFS folder where new Blue Coat Proxy logs are going to be uploaded
  * hive database and table name = table usually called "proxy", database as user specified during installation (spot
  * .conf)
  *
  * Dependencies: spark-core, spark-streaming, spark-sql and spark-hive
  *
  * Proxy Ingest application that runs a Spark Streaming job that listens to a HDFS folder for new files. Parses and
  * saves the file into Hive table.
  *
  *********************************************************************************************************************/

object ProxyIngest {

  def main(args: Array[String]): Unit = {
    // TODO: Implement argument parser for ProxyIngest Application
    BlueCoat.run(args(0), args(1))
  }

}
