package src.main.java.org.jamesii.ml3;

import org.apache.commons.math3.random.RandomGenerator;
import org.jamesii.ml3.experiment.init.IInitialStateBuilder;
import org.jamesii.ml3.model.Model;
import org.jamesii.ml3.model.Parameters;
import org.jamesii.ml3.model.agents.AgentDeclaration;
import org.jamesii.ml3.model.agents.IAgent;
import org.jamesii.ml3.model.agents.IAgentFactory;
import org.jamesii.ml3.model.state.IState;
import org.jamesii.ml3.model.state.IStateFactory;
import org.jamesii.ml3.model.values.RealValue;
import org.jamesii.ml3.model.values.StringValue;

public class BuildState {

    public class FourCity implements IInitialStateBuilder {

        public IState buildInitialState(Model model, IStateFactory sf, IAgentFactory af, RandomGenerator rng, Parameters par) {
            IState s = sf.create();

            IAgent entry = createLocation(s, af, model, rng, .0, 0.5, "entry", 0, 0);
            IAgent mid = createLocation(s, af, model, rng, 0.5, 0.5, "std", 0.1, 0.1);
            IAgent exit1 = createLocation(s, af, model, rng, 0.99, 0.0, "exit", 0.5, 0.5);
            IAgent exit2 = createLocation(s, af, model, rng, 0.99, 1.0, "exit", 1, 1);

            IAgent linkMid = createLink(s, af, model, rng, par, entry, mid, "fast");
            IAgent link1 = createLink(s, af, model, rng, par, mid, exit1, "fast");
            IAgent link2 = createLink(s, af, model, rng, par, mid, exit2, "fast");

            return s;
        }
    }

    public class ThreeCity implements IInitialStateBuilder {

        public IState buildInitialState(Model model, IStateFactory sf, IAgentFactory af, RandomGenerator rng, Parameters par) {
            IState s = sf.create();

            IAgent entry = createLocation(s, af, model, rng, .0, 0.5, "entry", 0, 0);
            IAgent exit1 = createLocation(s, af, model, rng, 0.99, 0.0, "exit", 0.5, 0.5);
            IAgent exit2 = createLocation(s, af, model, rng, 0.99, 1.0, "exit", 1, 1);

            IAgent link1 = createLink(s, af, model, rng, par, entry, exit1, "fast");
            IAgent link2 = createLink(s, af, model, rng, par, entry, exit2, "fast");

            return s;
        }
    }

    private IAgent createLink(IState s, IAgentFactory af,
                              Model model,
                              RandomGenerator rng,
                              Parameters par,
                              IAgent loc1,
                              IAgent loc2,
                              String typ) {
        AgentDeclaration ad = model.getAgentDeclaration("Link");
        IAgent newLink = af.createAgent(ad, 0);

        double distScaleSlow = ((RealValue) par.getValue("dist_scale_slow")).getValue();
        double distSclaeFast = ((RealValue) par.getValue("dist_scale_fast")).getValue();
        double frictRange = ((RealValue) par.getValue("frict_range")).getValue();

        double distance = Math.sqrt(sqDist(loc1, loc2));
        double distanceImpact = (typ == "slow") ? distScaleSlow * distance : distSclaeFast * distance;
        double friction = distanceImpact * (1 + rng.nextDouble() * frictRange);

        newLink.setAttributeValue("friction", new RealValue(friction));
        newLink.setAttributeValue("distance", new RealValue(distance));
        newLink.setAttributeValue("type", new StringValue(typ));

        loc1.addLink("links", newLink);
        newLink.addLink("endpoints", loc1);
        loc2.addLink("links", newLink);
        newLink.addLink("endpoints", loc2);

        s.addAgent(newLink);
        return newLink;
    }

    private IAgent createLocation(IState s, IAgentFactory af, Model model, RandomGenerator rng,
                               Double x, Double y,
                               String typ,
                               double quality, double resources) {
        AgentDeclaration ad = model.getAgentDeclaration("Location");
        IAgent newNode = af.createAgent(ad, 0);

        newNode.setAttributeValue("x", new RealValue(x));
        newNode.setAttributeValue("y", new RealValue(y));
        newNode.setAttributeValue("type", new StringValue(typ));
        newNode.setAttributeValue("quality", new RealValue(quality));
        newNode.setAttributeValue("resources", new RealValue(resources));

        s.addAgent(newNode);
        return newNode;
    }

    private double sqDist(IAgent loc1, IAgent loc2) {
        double x1 = ((RealValue) loc1.getAttributeValue("x")).getValue();
        double y1 = ((RealValue) loc1.getAttributeValue("y")).getValue();
        double x2 = ((RealValue) loc2.getAttributeValue("x")).getValue();
        double y2 = ((RealValue) loc2.getAttributeValue("y")).getValue();
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }
}
