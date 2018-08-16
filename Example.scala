package org.sessl

object Example extends App {

  import sessl._
  import sessl.ml3._
  import sessl.verification._
  import sessl.verification.mitl._

  execute {
    new Experiment with Observation with ParallelExecution with StatisticalModelChecking {

      model = "sir.ml3"
      simulator = NextReactionMethod()

      parallelThreads = -1

      set("a" <~ 0.03)
      set("b" <~ 0.05)

      stopTime = 200

      val s = 950 * "new Person(status := 'susceptible')"
      val i = 50 * "new Person(status := 'infected')"
      initializeWith(s + "," + i) += BarabasiAlbert("Person", "network", 5)

      observeAt(range(0, 1, 200))
      observe("inf" ~ agentCount("Person", "ego.status = 'infected'"))

      test = SequentialProbabilityRatioTest(
        p = 0.8,
        alpha = 0.05,
        beta = 0.05,
        delta = 0.05)

      prop = MITL(
        // the number of infected people is above 400 at least once between times 20 and 30,
        // but reaches 0 between 120 and 180 time units after that
        F(20, 30)(
          (OutVar[Int]("inf") > Constant(400)).U(0, 0)(F(120, 180)(OutVar[Int]("inf") === Constant(0)))
        )
      )

      withCheckResult { result =>
        println(result.satisfied)
      }

    }
  }
}
