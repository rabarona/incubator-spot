package org.apache.spot.ingest

import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.hive.HiveContext


/**
  * Created by rabarona on 2/24/17.
  */
object IngestSave {

  def save(data: DataFrame, partitions: Seq[String], tableName: String): Unit ={

    val hiveContext = new HiveContext(data.sqlContext.sparkContext)
    // TODO: test this implementation
    hiveContext.setConf("hive.exec.dynamic.partition", "true")
    hiveContext.setConf("hive.exec.dynamic.partition.mode", "nonstrict")
    data.write.mode(SaveMode.Append).partitionBy(partitions: _*).saveAsTable(tableName)
  }

}
