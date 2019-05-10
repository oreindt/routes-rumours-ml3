package org.sessl

import org.jamesii.ml3.experiment.init.JsonStateBuilder

object ExitDistribution extends App {

  import sessl._
  import sessl.ml3._

  val experiment = new Experiment with Observation with ParallelExecution with WorldGeneration with CSVOutput with TimeMeasurement {

      model = "routes.ml3"
      simulator = NextReactionMethod()
      parallelThreads = -1
      replications = 10 // 10
      startTime = 0
      stopTime = 100 // 500

      initializeWith(() => new JsonStateBuilder("init50.json"))


    set("p_find_links" <~ 0.5)
    set("p_find_dests" <~ 0.3)
    set("speed_expl_stay" <~ 1.0)
    set("speed_expl_move" <~ 1.0)
    set("p_keep_contact" <~ 0.1)
    set("p_info_mingle" <~ 0.1)
    set("p_info_contacts" <~ 0.1)
    set("p_transfer_info" <~ 0.1)

    observeAt(stopTime)
    observe("migrants" ~ agentCount(agentType = "Migrant")) // this only counts agents who are alive, i.e., have not arrived at an exit
    observe("info_loc" ~ agentCount(agentType = "Information"))
    observe("info_link" ~ agentCount(agentType = "InfoLink"))
    observe("cities" ~ agentCount(agentType = "Location"))
    observe("links" ~ agentCount(agentType = "Link"))
    observe("exiting" ~ expressionDistribution(agentType = "Location", filter = "ego.type = 'exit'", expression = "ego.migrants.size()"))
    observe("y" ~ expressionDistribution(agentType = "Location", filter = "ego.type = 'exit'", expression = "ego.y"))
    withRunResult(writeCSV)
    }
  execute(experiment)
  System.out.println(experiment.executionTime.get / (1000 * 60) + " min")
}
