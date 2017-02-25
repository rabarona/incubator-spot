package org.apache.spot.ingest.proxy

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType, LongType}

/**
  * Created by rabarona on 2/20/17.
  */

object ProxySchema {

  val Date = "p_date"
  val Time = "p_time"
  val Duration = "duration"
  val ClientIP = "clientip"
  val UserName = "username"
  val AuthorizationGroup = "authgroup"
  val ExceptionId = "exceptionid"
  val FilteredResult = "filterresult"
  val WebCast = "webcat"
  val Referer = "referer"
  val ResponseCode = "respcode"
  val Action = "action"
  val RequestMethod = "reqmethod"
  val ResponseContentType = "resconttype"
  val UriSchema = "urischeme"
  val Host = "host"
  val UriPort = "uriport"
  val UriPath = "uripath"
  val UriQuery = "uriquery"
  val UriExtension = "uriextension"
  val UserAgent = "useragent"
  val ServerIP = "serverip"
  val ScBytes = "scbytes"
  val CsBytes = "csbytes"
  val VirusId = "virusid"
  val BcAppName = "bcappname"
  val BcAppOper = "bcappoper"
  val FullURI = "fulluri"
  val Year = "y"
  val Month = "m"
  val Day = "d"
  val Hour = "h"

  val inputSchema: StructType = new StructType(
    Array(StructField(Date, StringType, true), // 0
      StructField(Time, StringType, true), // 1
      StructField(Duration, IntegerType, true), // 2
      StructField(ClientIP, StringType, true), // 3
      StructField(UserName, StringType, true), // 4
      StructField(AuthorizationGroup, StringType, true), // 5
      StructField(ExceptionId, StringType, true), // 6
      StructField(FilteredResult, StringType, true), // 7
      StructField(WebCast, StringType, true), // 8
      StructField(Referer, StringType, true), // 9
      StructField(ResponseCode, StringType, true), // 10
      StructField(Action, StringType, true), // 11
      StructField(RequestMethod, StringType, true), // 12
      StructField(ResponseContentType, StringType, true), // 13
      StructField(UriSchema, StringType, true), // 14
      StructField(Host, StringType, true), // 15
      StructField(UriPort, StringType, true), // 16
      StructField(UriPath, StringType, true), // 17
      StructField(UriQuery, StringType, true), // 18
      StructField(UriExtension, StringType, true), // 19
      StructField(UserAgent, StringType, true), // 20
      StructField(ServerIP, StringType, true), // 21
      StructField(ScBytes, LongType, true), // 22
      StructField(CsBytes, LongType, true), // 23
      StructField(VirusId, StringType, true), // 24
      StructField(BcAppName, StringType, true), // 25
      StructField(BcAppOper, StringType, true), // 26
      StructField(Year, StringType, true),
      StructField(Month, StringType, true),
      StructField(Day, StringType, true),
      StructField(Hour, StringType, true),
      StructField(FullURI, StringType, true)))

  val outputSelect = Seq(Date, Time, ClientIP, Host, RequestMethod, UserAgent, ResponseContentType, Duration, UserName,
    AuthorizationGroup, ExceptionId, FilteredResult, WebCast, Referer, ResponseCode, Action, UriSchema, UriPort,
    UriPath, UriQuery, UriExtension, ServerIP, ScBytes, CsBytes, VirusId, BcAppName, BcAppOper, FullURI, Year, Month,
    Day, Hour)
}
