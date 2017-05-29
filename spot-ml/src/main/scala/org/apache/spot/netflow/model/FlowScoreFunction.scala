/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spot.netflow.model

import org.apache.spark.broadcast.Broadcast
import org.apache.spot.SuspiciousConnectsScoreFunction
import org.apache.spot.netflow.{FlowWordCreator, FlowWords}
import org.apache.spot.utilities.transformation.PrecisionUtility


/**
  * Estimate the probabilities of network events using a [[FlowSuspiciousConnectsModel]]
  *
  * @param timeCuts Quantile cut-offs for binning time-of-day values when forming words from netflow records.
  * @param ibytCuts Quantile cut-offs for binning ibyt values when forming words from netflow records.
  * @param ipktCuts Quantile cut-offs for binning ipkt values when forming words from netflow records.
  * @param topicCount Number of topics used in the topic modelling analysis.
  * @param wordToPerTopicProbBC Broadcast map assigning to each word it's per-topic probabilities.
  *                           Ie. Prob [word | t ] for t = 0 to topicCount -1
  */


class FlowScoreFunction(timeCuts: Array[Double],
                        ibytCuts: Array[Double],
                        ipktCuts: Array[Double],
                        topicCount: Int,
                        wordToPerTopicProbBC: Broadcast[Map[String, Array[Double]]]) extends Serializable {


  val flowWordCreator = new FlowWordCreator(timeCuts, ibytCuts, ipktCuts)

  val suspiciousConnectsScoreFunction =
    new SuspiciousConnectsScoreFunction(topicCount, wordToPerTopicProbBC)

  /**
    * Estimate the probability of a netflow connection as distributed from the source IP and from the destination IP
    * and assign it the least of these two values.
    *
    * @param hour Hour of flow record.
    * @param minute Minute of flow record.
    * @param second Second of flow record.
    * @param srcIP Source IP of flow record.
    * @param dstIP Destination IP of flow record.
    * @param srcPort Source port of flow record.
    * @param dstPort Destination port of flow record.
    * @param ipkt ipkt entry of flow record
    * @param ibyt ibyt entry of flow record
    * @param srcTopicMix topic mix assigned of source IP
    * @param dstTopicMix topic mix assigned of destination IP
    * @return Minium of probability of this word from the source IP and probability of this word from the dest IP.
    */
  def score[P <: PrecisionUtility](precisionUtility: P)(hour: Int,
                                                        minute: Int,
                                                        second: Int,
                                                        srcIP: String,
                                                        dstIP: String,
                                                        srcPort: Int,
                                                        dstPort: Int,
                                                        ipkt: Long,
                                                        ibyt: Long,
                                                        srcTopicMix: Seq[precisionUtility.TargetType],
                                                        dstTopicMix: Seq[precisionUtility.TargetType]): Double = {


    val FlowWords(srcWord, dstWord) = flowWordCreator.flowWords(hour: Int, minute: Int, second: Int,
      srcPort: Int, dstPort: Int, ipkt: Long, ibyt: Long)

    val srcIPScore = suspiciousConnectsScoreFunction.score(precisionUtility)(srcTopicMix, srcWord)
    val dstIPScore = suspiciousConnectsScoreFunction.score(precisionUtility)(dstTopicMix, dstWord)

    Math.min(srcIPScore, dstIPScore)
  }
}
