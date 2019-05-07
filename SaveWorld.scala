package org.sessl

import org.apache.commons.math3.random.MersenneTwister
import org.jamesii.ml3.model.Parameters
import org.jamesii.ml3.model.build.ModelBuilder
import org.jamesii.ml3.model.state.SimpleStateFactory
import org.jamesii.ml3.model.state.writers.JsonStateWriter
import org.jamesii.ml3.parser.ParserUtil
import org.jamesii.ml3.simulator.factory.NextReactionMethodSimulatorFactory


object SaveWorld extends App with WorldGeneration {
  
  val t = ParserUtil.parseFile("m1-2.ml3")
  val mb = new ModelBuilder
  val model = mb.build(t)


  val af = new NextReactionMethodSimulatorFactory().createAgentFactory()
  val sf = new SimpleStateFactory(af)
  val rng = new MersenneTwister(System.currentTimeMillis)

  val par = new Parameters

  par.add("n_cities", 50) // 100?
  par.add("link_thresh", 0.3) // 0.1

  par.add("n_entries", 3) // 3
  par.add("entry_dist", 0.1) // 0.1
  par.add("qual_entry", 0.0)
  par.add("res_entry", 0.0)

  par.add("n_exits", 10)
  par.add("exit_dist", 0.5) // 0.5
  par.add("qual_exit", 1.0)
  par.add("res_exit", 1.0)

  par.add("dist_scale_slow", 10.0)
  par.add("dist_scale_fast", 1.0)
  par.add("frict_range", 0.5)

  val state = WorldGenerator.buildInitialState(model, sf, af, rng, par)

  val writer = new JsonStateWriter("init.json", model)
  writer.write(state, 0)
}
