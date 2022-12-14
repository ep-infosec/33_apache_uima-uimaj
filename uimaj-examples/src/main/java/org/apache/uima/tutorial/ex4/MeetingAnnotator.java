/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.uima.tutorial.ex4;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.AnalysisComponent;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.tutorial.DateAnnot;
import org.apache.uima.tutorial.Meeting;
import org.apache.uima.tutorial.RoomNumber;
import org.apache.uima.tutorial.TimeAnnot;

/**
 * Example annotator that detects meetings from the co-occurrence of a RoomNumber, a Date, and two
 * Times (start and end), within a specified "window" size.
 */
public class MeetingAnnotator extends JCasAnnotator_ImplBase {
  /**
   * Size in characters of window within which a RoomNumber, a Date, and two Times must occur in
   * order for a meeting annotation to be created.
   */
  private int mWindowSize;

  /**
   * @see AnalysisComponent#initialize(UimaContext)
   */
  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    // Get config. parameter value
    mWindowSize = (Integer) aContext.getConfigParameterValue("WindowSize");
  }

  /**
   * @see JCasAnnotator_ImplBase#process(JCas)
   */
  @Override
  public void process(JCas aJCas) {
    // get annotation indexes
    FSIndex<RoomNumber> roomNumberIndex = aJCas.getAnnotationIndex(RoomNumber.class);
    FSIndex<DateAnnot> dateIndex = aJCas.getAnnotationIndex(DateAnnot.class);
    FSIndex<TimeAnnot> timeIndex = aJCas.getAnnotationIndex(TimeAnnot.class);

    // store end position of last meeting we identified, to prevent multiple
    // annotations over same span
    int lastMeetingEnd = -1;

    for (RoomNumber room : roomNumberIndex.select()) {
      for (DateAnnot date : dateIndex.select()) {
        for (TimeAnnot time1 : timeIndex.select()) {
          for (TimeAnnot time2 : timeIndex.select()) {

            // times must be different annotations
            if (time1 != time2) {
              // compute the begin and end of the span
              int minBegin = Math.min(Math.min(time1.getBegin(), time2.getBegin()),
                      Math.min(date.getBegin(), room.getBegin()));
              int maxEnd = Math.max(Math.max(time1.getEnd(), time2.getEnd()),
                      Math.max(date.getEnd(), room.getEnd()));

              // span must be smaller than the window size?
              if (maxEnd - minBegin < mWindowSize &&
              // span must not overlap the last annotation we made
                      minBegin > lastMeetingEnd) {
                // annotate
                Meeting mtg = new Meeting(aJCas, minBegin, maxEnd, room, date, time1, time2);
                mtg.addToIndexes();
                lastMeetingEnd = maxEnd;
              }
            }
          }
        }
      }
    }
  }
}
