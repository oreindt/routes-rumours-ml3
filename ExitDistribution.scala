package org.sessl

import org.jamesii.ml3.experiment.init.JsonStateBuilder

object ExitDistribution extends App {

  import sessl._
  import sessl.ml3._

  val experiment = new Experiment with Observation with ParallelExecution with WorldGeneration with CSVOutput with TimeMeasurement {

      model = "m1-2.ml3"
      simulator = NextReactionMethod()
      parallelThreads = -1
      replications = 1 // 10
      startTime = 0
      stopTime = 10 // 500

      initializeWith(() => new JsonStateBuilder("init50.json"))


      scan("p_find_links" <~ (0.1,0.5,1.0)
        and "p_find_dests" <~ (0.1,0.3,1.0)
        and "speed_expl_stay" <~ (0.3,0.5,1.0)
        and "speed_expl_move" <~ (0.3,0.5,1.0),
        "p_keep_contact" <~ (0.1,0.3,0.6)
        and "p_info_mingle" <~ (0.1,0.3,0.6)
        and "p_info_contacts" <~ (0.1,0.3,0.6)
        and "p_transfer_info" <~ (0.1,0.3,0.6))


      observeAt(stopTime)
      //observe("fired" ~ expressionDistribution(agentType = "World", expression = "ego.fired"))
      observe("migrants" ~ agentCount(agentType = "Migrant")) // this only counts agents who are alive, i.e., have not arrived at an exit
      observe("info_loc" ~ agentCount(agentType = "Information"))
      observe("info_link" ~ agentCount(agentType = "InfoLink"))
      observe("cities" ~ agentCount(agentType = "Location"))
      observe("links" ~ agentCount(agentType = "Link"))
      //observe("exits" ~ agentCount(agentType = "Location", filter = "ego.type = 'exit'"))
      //observe("entries" ~ agentCount(agentType = "Location", filter = "ego.type = 'entry'"))
      //observe("cities" ~ agentCount(agentType = "Location", filter = "ego.type = 'std'"))
      //observe("entering" ~ expressionDistribution(agentType = "Location", filter = "ego.type = 'entry'", expression = "ego.migrants.size()"))
      observe("exiting" ~ expressionDistribution(agentType = "Location", filter = "ego.type = 'exit'", expression = "ego.migrants.size()"))
      observe("y" ~ expressionDistribution(agentType = "Location", filter = "ego.type = 'exit'", expression = "ego.y"))
      withRunResult(writeCSV)
    }
  execute(experiment)
  System.out.println(experiment.executionTime.get / (1000 * 60) + " min")
}
