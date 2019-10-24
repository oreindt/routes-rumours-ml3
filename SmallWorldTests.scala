import org.jamesii.ml3.experiment.init.IInitialStateBuilder
import src.main.java.org.jamesii.ml3.{BuildState, FourCity, ThreeCity}

object SmallWorldTests extends App {

  import sessl._
  import sessl.ml3._

  execute(new Standard(new BuildState.ThreeCity, "std-3"))
  execute(new Standard(new BuildState.FourCity, "std-4"))
  execute(new FullExplore(new BuildState.ThreeCity, "full-expl-3"))
  execute(new FullExplore(new BuildState.FourCity, "full-expl-4"))
  execute(new NoCommunication(new BuildState.ThreeCity, "no-comm-3"))
  execute(new NoCommunication(new BuildState.FourCity, "no-comm-4"))
  execute(new Both(new BuildState.ThreeCity, "both-3"))
  execute(new Both(new BuildState.FourCity, "both-4"))
  execute(new ImperfectExplore(new BuildState.ThreeCity, "half-expl-3"))
  execute(new ImperfectExplore(new BuildState.FourCity, "half-expl-4"))
  execute(new NoContacts(new BuildState.ThreeCity, "no-contacts-3"))
  execute(new NoContacts(new BuildState.FourCity, "no-contacts-4"))


  class Standard(init : IInitialStateBuilder, name : String) extends RRExperiment(init, name) {
  }

  class FullExplore(init : IInitialStateBuilder, name : String) extends RRExperiment(init, name) {
    set("p_find_links" <~ 1.0) // 0.5
    set("p_know_target" <~ 1.0) // none
  }

  class NoCommunication(init : IInitialStateBuilder, name : String) extends RRExperiment(init, name) {
    set("p_keep_contact" <~ 0.0) // 0.1
    set("p_info_contacts" <~ 0.0) // 0.1
  }

  class Both(init : IInitialStateBuilder, name : String) extends RRExperiment(init, name) {
    set("p_find_links" <~ 1.0) // 0.5
    set("p_keep_contact" <~ 0.0) // 0.1
    set("p_info_contacts" <~ 0.0) // 0.1
    set("p_know_target" <~ 1.0) // none
  }

  class ImperfectExplore(init : IInitialStateBuilder, name : String) extends RRExperiment(init, name) {
    set("p_find_links" <~ 0.5) // 0.5
    set("speed_expl_stay" <~ 0.5)
    set("speed_expl_move" <~ 0.5)
    set("p_keep_contact" <~ 0.0) // 0.1
    set("p_info_contacts" <~ 0.0) // 0.1
    set("p_know_target" <~ 0.5) // none
  }

  class NoContacts(init : IInitialStateBuilder, name : String) extends RRExperiment(init, name) {
    set("speed_expl_stay" <~ 0.5)
    set("speed_expl_move" <~ 0.5)
    set("p_keep_contact" <~ 0.0) // 0.1
    set("p_info_contacts" <~ 0.1) // 0.1
  }

  class RRExperiment(init : IInitialStateBuilder, name : String) extends Experiment with Observation with ParallelExecution with CSVOutput with TimeMeasurement {
    model = "routes.ml3"
    simulator = NextReactionMethod()
    parallelThreads = -1
    replications = 50 // 10
    startTime = 0
    stopTime = 100 // 500

    // initializeWith(() => new JsonStateBuilder("init50_1.json"))
    initializeWith(() => init)

    set("rate_dep" <~ 10) // 20 // number of departures per time step

    set("dist_scale_slow" <~ 10.0) // scale >= 1.0 required, otherwise path finding breaks
    set("dist_scale_fast" <~ 1.0)
    set("frict_range" <~ 0.0) // stochastic range of friction

    set("n_ini_contacts" <~ 10)
    set("ini_capital" <~ 2000.0)
    set("p_know_target" <~ 0.0) // none

    set("res_exp" <~ 0.5)
    set("qual_exp" <~ 0.5)
    set("frict_exp_fast" <~ 1.25)
    set("frict_exp_slow" <~ 12.5)
    set("p_find_links" <~ 0.5) // 0.5
    // set("trust_found_links" <~ 0.5)
    // set("p_find_dests" <~ 0.3)
    // set("trust_travelled" <~ 0.8)
    set("speed_expl_stay" <~ 1.0)
    set("speed_expl_move" <~ 1.0)

    set("costs_stay" <~ 1.0)
    set("ben_resources" <~ 5.0)
    set("costs_move" <~ 2.0)

    set("qual_weight_x" <~ 0.5)
    set("qual_weight_res" <~ 0.1)
    set("qual_weight_frict" <~ 0.1)

    set("p_keep_contact" <~ 0.1) // 0.1
    // set("p_info_mingle" <~ 0.3)
    set("p_info_contacts" <~ 0.1) // 0.1
    // set("p_transfer_info" <~ 0.3)
    set("n_contacts_max" <~ 50)
    // set("arr_learn" <~ 0.0)
    set("convince" <~ 0.5) // change doubt into belief
    set("convert" <~ 0.1) // change belief into other belief
    set("confuse" <~ 0.3) // change belief into doubt
    set("error" <~ 0.1) // stochastic error when transmitting information
    set("weight_arr" <~ 1.0)

    set("expl_rate" <~ 1)
    set("cost_rate" <~ 1)
    set("transit_rate" <~ 1)

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

    observe("city_out_moves" ~ expressionDistribution(agentType = "Location", expression = "ego.moves"))
    observe("city_comm" ~ expressionDistribution(agentType = "Location", expression = "ego.communication"))

    observe("init_contacts" ~ expressionDistribution(agentType = "World", expression = "ego.contacts_made"))

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

    csvOutputDirectory(() => "results-" + name)
    withRunResult { results =>
      writeCSV(results)
    }
  }

}
