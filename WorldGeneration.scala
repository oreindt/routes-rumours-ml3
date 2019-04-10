import org.apache.commons.math3.random.RandomGenerator
import org.jamesii.ml3.experiment.init.IInitialStateBuilder
import org.jamesii.ml3.model.agents.{AgentDeclaration, IAgent, IAgentFactory}
import org.jamesii.ml3.model.state.{IState, IStateFactory}
import org.jamesii.ml3.model.values.{RealValue, StringValue}
import org.jamesii.ml3.model.{Model, Parameters}

import scala.collection.JavaConverters._

trait WorldGeneration {

  object WorldGenerator extends IInitialStateBuilder {
    override def buildInitialState(model: Model,
                                   sf: IStateFactory,
                                   af: IAgentFactory,
                                   rng: RandomGenerator,
                                   par: Parameters): IState = {

      val state = createNodes(model, sf, af, rng, par)
      createEntries(model, sf, af, rng, par, state)


      state
    }

    private def createNodes(model: Model,
                            sf: IStateFactory,
                            af: IAgentFactory,
                            rng: RandomGenerator,
                            par: Parameters) = {
      val state = sf.create()
      val ad = model.getAgentDeclaration("Location")

      val nNodes = par.getValue("n_cities").getValue.asInstanceOf[Int]
      val thresh = par.getValue("link_thresh").getValue.asInstanceOf[Double]
      val sqThresh = thresh * thresh

      for (i <- 1 to nNodes) {
        val newNode = createLocation(af, ad, rng, rng.nextDouble(), rng.nextDouble(), "std", rng.nextDouble(), rng.nextDouble())
        for (otherNode <- state.getAgents.asScala) {
          if (sqDist(newNode, otherNode) < sqThresh) {
            createLink(newNode, otherNode, "fast")
          }
        }

        state.addAgent(newNode)
      }
      state
    }

    private def sqDist(loc1: IAgent, loc2: IAgent): Double = {
      val x1 = loc1.getAttributeValue("x").getValue.asInstanceOf[Double]
      val y1 = loc1.getAttributeValue("y").getValue.asInstanceOf[Double]
      val x2 = loc2.getAttributeValue("x").getValue.asInstanceOf[Double]
      val y2 = loc2.getAttributeValue("y").getValue.asInstanceOf[Double]
      (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)
    }

    private def createEntries(model: Model,
                              sf: IStateFactory,
                              af: IAgentFactory,
                              rng: RandomGenerator,
                              par: Parameters,
                              state: IState): Unit = {
      val ad = model.getAgentDeclaration("Location")

      val nEntries = par.getValue("n_entries").getValue.asInstanceOf[Int]
      val threshDist = par.getValue("entry_dist").getValue.asInstanceOf[Double]
      val qual = par.getValue("qual_entry").getValue.asInstanceOf[Double]
      val res = par.getValue("res_entry").getValue.asInstanceOf[Double]

      for (i <- 1 to nEntries) {
        val newNode = createLocation(af, ad, rng, 0, rng.nextDouble(), "entry", qual, res)
        for (otherNode <- state.getAgents.asScala.filter(n => !n.getAttributeValue("type").getValue.equals("entry"))) {
          val otherX = otherNode.getAttributeValue("x").getValue.asInstanceOf[Double]
          if (otherX < threshDist) {
            createLink(newNode, otherNode, "slow")
          }
        }

        state.addAgent(newNode)
      }
    }

    private def createExits(model: Model,
                            sf: IStateFactory,
                            af: IAgentFactory,
                            rng: RandomGenerator,
                            par: Parameters,
                            state: IState): Unit = {
      val ad = model.getAgentDeclaration("Location")

      val nEntries = par.getValue("n_exits").getValue.asInstanceOf[Int]
      val threshDist = par.getValue("entry_dist").getValue.asInstanceOf[Double]
      val qual = par.getValue("qual_exit").getValue.asInstanceOf[Double]
      val res= par.getValue("res_exit").getValue.asInstanceOf[Double]

      for (i <- 1 to nEntries) {
        val newNode = createLocation(af, ad, rng, 0, rng.nextDouble(), "entry", qual, res)
        for (otherNode <- state.getAgents.asScala.filter(n => !n.getAttributeValue("type").getValue.equals("exit"))) {
          val otherX = otherNode.getAttributeValue("x").getValue.asInstanceOf[Double]
          if (otherX > threshDist) {
            createLink(newNode, otherNode, "slow")
          }
        }

        state.addAgent(newNode)
      }
    }

    private def createLink(loc1: IAgent, loc2: IAgent, typ: String): Unit = {
      loc1.addLink("neighbors", loc2)
      loc2.addLink("neighbors", loc1)
    }

    private def createLocation(af: IAgentFactory, ad: AgentDeclaration, rng: RandomGenerator,
                               x: Double, y: Double,
                               typ: String,
                               quality: Double, resources: Double): IAgent = {
      val newNode = af.createAgent(ad, 0)
      newNode.setAttributeValue("x", new RealValue(x))
      newNode.setAttributeValue("y", new RealValue(y))
      newNode.setAttributeValue("type", new StringValue(typ))
      newNode.setAttributeValue("quality", new RealValue(quality))
      newNode.setAttributeValue("resources", new RealValue(resources))
      newNode
    }

  }

}
