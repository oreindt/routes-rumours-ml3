package org.sessl

import java.text.SimpleDateFormat
import java.util.Calendar

import src.main.java.org.jamesii.ml3.{FourCity, ThreeCity}

object ExitDistribution extends App {

  import sessl._
  import sessl.ml3._

  execute(new Standard3)
  execute(new FullExplore3)
  execute(new NoCommunication3)
  execute(new Both3)
  execute(new Standard4)
  execute(new FullExplore4)
  execute(new NoCommunication4)
  execute(new Both4)

  class Standard4 extends RRExperiment {
    initializeWith(() => new FourCity())

    // std scenario
    set("p_find_links" <~ 0.5) // 0.5
    // set("p_find_dests" <~ 0.3)
    set("speed_expl_stay" <~ 1.0)
    set("speed_expl_move" <~ 1.0)
    set("p_keep_contact" <~ 0.1) // 0.1
    set("p_info_mingle" <~ 0.1)
    set("p_info_contacts" <~ 0.1) // 0.1
    // set("p_transfer_info" <~ 0.0) // 0.1
    set("p_know_target" <~ 0.0) // none

    csvOutputDirectory(() => "results-std-4")
    withRunResult { results =>
      writeCSV(results)
    }
  }

  class FullExplore4 extends RRExperiment {
    initializeWith(() => new FourCity())

    // full explore scenario
    set("p_find_links" <~ 1.0) // 0.5
    // set("p_find_dests" <~ 0.3)
    set("speed_expl_stay" <~ 1.0)
    set("speed_expl_move" <~ 1.0)
    set("p_keep_contact" <~ 0.1) // 0.1
    set("p_info_mingle" <~ 0.1)
    set("p_info_contacts" <~ 0.1) // 0.1
    // set("p_transfer_info" <~ 0.0) // 0.1
    set("p_know_target" <~ 1.0) // none

    withRunResult { results =>
      csvOutputDirectory(() => "results-full-expl-4")
      writeCSV(results)
    }
  }

  class NoCommunication4 extends RRExperiment {
    initializeWith(() => new FourCity())

    // full explore scenario
    set("p_find_links" <~ 0.5) // 0.5
    // set("p_find_dests" <~ 0.3)
    set("speed_expl_stay" <~ 1.0)
    set("speed_expl_move" <~ 1.0)
    set("p_keep_contact" <~ 0.0) // 0.1
    set("p_info_mingle" <~ 0.1)
    set("p_info_contacts" <~ 0.0) // 0.1
    // set("p_transfer_info" <~ 0.0) // 0.1
    set("p_know_target" <~ 0.0) // none

    withRunResult { results =>
      csvOutputDirectory(() => "results-no-comm-4")
      writeCSV(results)
    }
  }

  class Both4 extends RRExperiment {
    initializeWith(() => new FourCity())

    // full explore and no comm (perfect information)
    set("p_find_links" <~ 1.0) // 0.5
    // set("p_find_dests" <~ 0.3)
    set("speed_expl_stay" <~ 1.0)
    set("speed_expl_move" <~ 1.0)
    set("p_keep_contact" <~ 0.0) // 0.1
    set("p_info_mingle" <~ 0.1)
    set("p_info_contacts" <~ 0.0) // 0.1
    // set("p_transfer_info" <~ 0.0) // 0.1
    set("p_know_target" <~ 1.0) // none

    withRunResult { results =>
      csvOutputDirectory(() => "results-both-4")
      writeCSV(results)
    }
  }

  class Standard3 extends RRExperiment {
    initializeWith(() => new ThreeCity())

    // std scenario
    set("p_find_links" <~ 0.5) // 0.5
    // set("p_find_dests" <~ 0.3)
    set("speed_expl_stay" <~ 1.0)
    set("speed_expl_move" <~ 1.0)
    set("p_keep_contact" <~ 0.1) // 0.1
    set("p_info_mingle" <~ 0.1)
    set("p_info_contacts" <~ 0.1) // 0.1
    // set("p_transfer_info" <~ 0.0) // 0.1
    set("p_know_target" <~ 0.0) // none

    csvOutputDirectory(() => "results-std-3")
    withRunResult { results =>
      writeCSV(results)
    }
  }

  class FullExplore3 extends RRExperiment {
    initializeWith(() => new ThreeCity())

    // full explore scenario
    set("p_find_links" <~ 1.0) // 0.5
    // set("p_find_dests" <~ 0.3)
    set("speed_expl_stay" <~ 1.0)
    set("speed_expl_move" <~ 1.0)
    set("p_keep_contact" <~ 0.1) // 0.1
    set("p_info_mingle" <~ 0.1)
    set("p_info_contacts" <~ 0.1) // 0.1
    // set("p_transfer_info" <~ 0.0) // 0.1
    set("p_know_target" <~ 1.0) // none

    withRunResult { results =>
      csvOutputDirectory(() => "results-full-expl-3")
      writeCSV(results)
    }
  }

  class NoCommunication3 extends RRExperiment {
    initializeWith(() => new ThreeCity())

    // full explore scenario
    set("p_find_links" <~ 0.5) // 0.5
    // set("p_find_dests" <~ 0.3)
    set("speed_expl_stay" <~ 1.0)
    set("speed_expl_move" <~ 1.0)
    set("p_keep_contact" <~ 0.0) // 0.1
    set("p_info_mingle" <~ 0.1)
    set("p_info_contacts" <~ 0.0) // 0.1
    // set("p_transfer_info" <~ 0.0) // 0.1
    set("p_know_target" <~ 0.0) // none

    withRunResult { results =>
      csvOutputDirectory(() => "results-no-comm-3")
      writeCSV(results)
    }
  }

  class Both3 extends RRExperiment {
    initializeWith(() => new ThreeCity())

    // full explore and no comm (perfect information)
    set("p_find_links" <~ 1.0) // 0.5
    // set("p_find_dests" <~ 0.3)
    set("speed_expl_stay" <~ 1.0)
    set("speed_expl_move" <~ 1.0)
    set("p_keep_contact" <~ 0.0) // 0.1
    set("p_info_mingle" <~ 0.1)
    set("p_info_contacts" <~ 0.0) // 0.1
    // set("p_transfer_info" <~ 0.0) // 0.1
    set("p_know_target" <~ 1.0) // none

    withRunResult { results =>
      csvOutputDirectory(() => "results-both-3")
      writeCSV(results)
    }
  }

  class RRExperiment extends Experiment with Observation with ParallelExecution with WorldGeneration with CSVOutput with TimeMeasurement {
    model = "routes.ml3"
    simulator = NextReactionMethod()
    parallelThreads = -1
    replications = 20 // 10
    startTime = 0
    stopTime = 100 // 500

    // initializeWith(() => new JsonStateBuilder("init50_1.json"))
    initializeWith(() => new FourCity())


    set("dist_scale_fast" <~ 1.0)
    set("frict_range" <~ 0.0)

    // observeAt(range(0,1,stopTime))

    observeAt(stopTime)
    /*
        observe("migrants" ~ agentCount(agentType = "Migrant")) // this only counts agents who are alive, i.e., have not arrived at an exit
        observe("info_loc" ~ agentCount(agentType = "Information"))
        observe("info_link" ~ agentCount(agentType = "InfoLink"))
        observe("cities" ~ agentCount(agentType = "Location"))
        observe("links" ~ agentCount(agentType = "Link"))
        observe("exiting" ~ expressionDistribution(agentType = "Location", filter = "ego.type = 'exit'", expression = "ego.migrants.size()"))
    */

    // city properties
    observe("city_x" ~ expressionDistribution(agentType = "Location", expression = "ego.x"))
    observe("city_y" ~ expressionDistribution(agentType = "Location", expression = "ego.y"))
    observe("city_type" ~ expressionDistribution(agentType = "Location", expression = "ego.type"))
    observe("city_qual" ~ expressionDistribution(agentType = "Location", expression = "ego.quality"))
    observe("city_N" ~ expressionDistribution(agentType = "Location", expression = "ego.migrants.size()"))
    observe("city_links" ~ expressionDistribution(agentType = "Location", expression = "ego.links.size()"))
    observe("city_count" ~ expressionDistribution(agentType = "Location", expression = "ego.visits"))
    observe("city_accuracy" ~ expressionDistribution(agentType = "Location", expression = "ego.accuracy_about()"))
    observe("city_n_info" ~ expressionDistribution(agentType = "Location", expression = "ego.information.size()"))

    // link properties
    observe("link_type" ~ expressionDistribution(agentType = "Link", expression = "ego.type"))
    observe("link_friction" ~ expressionDistribution(agentType = "Link", expression = "ego.friction"))
    observe("link_count" ~ expressionDistribution(agentType = "Link", expression = "ego.visits"))
    observe("link_accuracy" ~ expressionDistribution(agentType = "Link", expression = "ego.accuracy_about()"))
    observe("link_n_info" ~ expressionDistribution(agentType = "Link", expression = "ego.information.size()"))


    /*
    // counts
    observe("n_migrant" ~ agentCount("Migrant"))
    observe("n_city" ~ agentCount("Location"))
    observe("n_link" ~ agentCount("Link"))
*/

    /*
    // knowledge accuracy
    observe("accuracy" ~ expressionDistribution(agentType = "World", expression = "ego.average_accuracy()"))
    observe("accuracy_link" ~ expressionDistribution(agentType = "World", expression = "ego.average_accuracy_link()"))
    */


    /*
    // knowledge items
    observe("knowledge_acc" ~ expressionDistribution(agentType = "Information", expression = "ego.accuracy()"))
    observe("knowledge_quality" ~ expressionDistribution(agentType = "Information", expression = "ego.quality"))
    observe("knowledge_resources" ~ expressionDistribution(agentType = "Information", expression = "ego.resources"))
    observe("knowledge_real_quality" ~ expressionDistribution(agentType = "Information", expression = "ego.subject.quality"))
    observe("knowledge_real_resources" ~ expressionDistribution(agentType = "Information", expression = "ego.subject.resources"))
    observe("knowledge_acc_link" ~ expressionDistribution(agentType = "InfoLink", expression = "ego.accuracy()"))
    observe("knowledge_friction" ~ expressionDistribution(agentType = "InfoLink", expression = "ego.friction"))
    observe("knowledge_real_friction" ~ expressionDistribution(agentType = "InfoLink", expression = "ego.subject.friction"))
    */
  }

}
