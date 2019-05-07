package org.sessl

import org.apache.commons.math3.random.RandomGenerator
import org.jamesii.ml3.experiment.init.IInitialStateBuilder
import org.jamesii.ml3.model.agents.{IAgent, IAgentFactory}
import org.jamesii.ml3.model.state.{IState, IStateFactory}
import org.jamesii.ml3.model.values.{IntValue, RealValue, StringValue}
import org.jamesii.ml3.model.{Model, Parameters}

import scala.collection.JavaConverters._

trait WorldGeneration {
  implicit def double2RealValue(value: Double): RealValue = new RealValue(value)

  implicit def string2StringValue(value: String): StringValue = new StringValue(value)

  implicit def int2IntValue(value: Int): IntValue = new IntValue(value)

  object WorldGenerator extends IInitialStateBuilder {
    override def buildInitialState(model: Model,
                                   sf: IStateFactory,
                                   af: IAgentFactory,
                                   rng: RandomGenerator,
                                   par: Parameters): IState = {

      val state = createNodes(model, sf, af, rng, par)
      createEntries(model, sf, af, rng, par, state)
      createExits(model, sf, af, rng, par, state)
      val worldType = model.getAgentDeclaration("World")
      val world = af.createAgent(worldType, 0.0)
      state.addAgent(world)

      state
    }

    private def createNodes(model: Model,
                            sf: IStateFactory,
                            af: IAgentFactory,
                            rng: RandomGenerator,
                            par: Parameters) = {
      val state = sf.create()

      val nNodes = par.getValue("n_cities").getValue.asInstanceOf[Int]
      val thresh = par.getValue("link_thresh").getValue.asInstanceOf[Double]
      val sqThresh = thresh * thresh

      for (i <- 1 to nNodes) {
        val newNode = createLocation(af, model, rng, rng.nextDouble(), rng.nextDouble(), "std", rng.nextDouble(), rng.nextDouble())
        for (otherNode <- state.getAgentsByType("Location").asScala) {
          if (sqDist(newNode, otherNode) < sqThresh) {
            state.addAgent(createLink(af, model, rng, par, newNode, otherNode, "fast"))
          }
        }

        state.addAgent(newNode)
      }
      state
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
        val newNode = createLocation(af, model, rng, 0, rng.nextDouble(), "entry", qual, res)
        for (otherNode <- state.getAgentsByType("Location").asScala.filter(n => n.getAttributeValue("type").getValue.equals("std"))) {
          val otherX = otherNode.getAttributeValue("x").getValue.asInstanceOf[Double]
          if (otherX < threshDist) {
            state.addAgent(createLink(af, model, rng, par, newNode, otherNode, "slow"))
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

      val nExits = par.getValue("n_exits").getValue.asInstanceOf[Int]
      val threshDist = par.getValue("exit_dist").getValue.asInstanceOf[Double]
      val qual = par.getValue("qual_exit").getValue.asInstanceOf[Double]
      val res = par.getValue("res_exit").getValue.asInstanceOf[Double]

      for (i <- 1 to nExits) {
        val newNode = createLocation(af, model, rng, 1, rng.nextDouble(), "exit", qual, res)
        for (otherNode <- state.getAgentsByType("Location").asScala.filter(n => n.getAttributeValue("type").getValue.equals("std"))) {
          val otherX = otherNode.getAttributeValue("x").getValue.asInstanceOf[Double]
          if (otherX > threshDist) {
            state.addAgent(createLink(af, model, rng, par, newNode, otherNode, "slow"))
          }
        }

        state.addAgent(newNode)
      }
    }

    private def createLink(af: IAgentFactory,
                           model: Model,
                           rng: RandomGenerator,
                           par: Parameters,
                           loc1: IAgent,
                           loc2: IAgent,
                           typ: String): IAgent = {
      val ad = model.getAgentDeclaration("Link")
      val newLink = af.createAgent(ad, 0)

      val distScaleSlow = par.getValue("dist_scale_slow").getValue.asInstanceOf[Double]
      val distSclaeFast = par.getValue("dist_scale_fast").getValue.asInstanceOf[Double]
      val frictRange = par.getValue("frict_range").getValue.asInstanceOf[Double]

      val distance = Math.sqrt(sqDist(loc1, loc2))
      val distanceImpact = if (typ == "slow") distScaleSlow * distance else distSclaeFast * distance
      val friction = distanceImpact * (1 + rng.nextDouble() + frictRange)

      newLink.setAttributeValue("friction", friction)
      newLink.setAttributeValue("distance", distance)
      newLink.setAttributeValue("type", typ)

      loc1.addLink("links", newLink)
      newLink.addLink("endpoints", loc1)
      loc2.addLink("links", newLink)
      newLink.addLink("endpoints", loc2)

      newLink
    }

    private def sqDist(loc1: IAgent, loc2: IAgent): Double = {
      val x1 = loc1.getAttributeValue("x").getValue.asInstanceOf[Double]
      val y1 = loc1.getAttributeValue("y").getValue.asInstanceOf[Double]
      val x2 = loc2.getAttributeValue("x").getValue.asInstanceOf[Double]
      val y2 = loc2.getAttributeValue("y").getValue.asInstanceOf[Double]
      (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)
    }

    private def createLocation(af: IAgentFactory, model: Model, rng: RandomGenerator,
                               x: Double, y: Double,
                               typ: String,
                               quality: Double, resources: Double): IAgent = {
      val ad = model.getAgentDeclaration("Location")
      val newNode = af.createAgent(ad, 0)

      newNode.setAttributeValue("x", x)
      newNode.setAttributeValue("y", y)
      newNode.setAttributeValue("type", typ)
      newNode.setAttributeValue("quality", quality)
      newNode.setAttributeValue("resources", resources)

      newNode
    }

  }

}
