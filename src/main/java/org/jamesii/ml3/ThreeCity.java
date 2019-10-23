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

public class ThreeCity implements IInitialStateBuilder {
    public IState buildInitialState(Model model, IStateFactory sf, IAgentFactory af, RandomGenerator rng, Parameters par) {
        IState s = sf.create();
        AgentDeclaration location = model.getAgentDeclaration("Location");
        AgentDeclaration link = model.getAgentDeclaration("Link");
        AgentDeclaration world = model.getAgentDeclaration("World");

        IAgent entry = af.createAgent(location, 0);
        IAgent exit1 = af.createAgent(location, 0);
        IAgent exit2 = af.createAgent(location, 0);

        IAgent link1 = af.createAgent(link, 0);
        IAgent link2 = af.createAgent(link, 0);

        entry.setAttributeValue("type", new StringValue("entry"));
        entry.setAttributeValue("quality", new RealValue(0));
        entry.setAttributeValue("resources", new RealValue(0));
        entry.setAttributeValue("x", new RealValue(0));
        entry.setAttributeValue("y", new RealValue(0.5));

        exit1.setAttributeValue("type", new StringValue("exit"));
        exit1.setAttributeValue("quality", new RealValue(0.5));
        exit1.setAttributeValue("resources", new RealValue(0.5));
        exit1.setAttributeValue("x", new RealValue(0.99));
        exit1.setAttributeValue("y", new RealValue(0));

        exit2.setAttributeValue("type", new StringValue("exit"));
        exit2.setAttributeValue("quality", new RealValue(1));
        exit2.setAttributeValue("resources", new RealValue(1));
        exit2.setAttributeValue("x", new RealValue(0.99));
        exit2.setAttributeValue("y", new RealValue(1));

        link1.setAttributeValue("type", new StringValue("fast"));
        double dist1 = Math.sqrt(sqDist(entry, exit1));
        link1.setAttributeValue("distance", new RealValue(dist1));
        double frict1 = frict(dist1, par, rng);
        link1.setAttributeValue("friction", new RealValue(frict1));

        link2.setAttributeValue("type", new StringValue("fast"));
        double dist2 = Math.sqrt(sqDist(entry, exit2));
        link2.setAttributeValue("distance", new RealValue(dist2));
        double frict2 = frict(dist2, par, rng);
        link2.setAttributeValue("friction", new RealValue(frict2));


        entry.addLink("links", link1);
        link1.addLink("endpoints", entry);

        entry.addLink("links", link2);
        link2.addLink("endpoints", entry);

        link1.addLink("endpoints", exit1);
        exit1.addLink("links", link1);

        link2.addLink("endpoints", exit2);
        exit2.addLink("links", link2);

        s.addAgent(entry);
        s.addAgent(exit1);
        s.addAgent(exit2);
        s.addAgent(link1);
        s.addAgent(link2);

        IAgent aWorld = af.createAgent(world, 0);
        s.addAgent(aWorld);

        return s;
    }

    private double sqDist(IAgent loc1, IAgent loc2) {
        double x1 = ((RealValue) loc1.getAttributeValue("x")).getValue();
        double y1 = ((RealValue) loc1.getAttributeValue("y")).getValue();
        double x2 = ((RealValue) loc2.getAttributeValue("x")).getValue();
        double y2 = ((RealValue) loc2.getAttributeValue("y")).getValue();
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    private double frict(double dist, Parameters par, RandomGenerator rng) {
        double distScale = ((RealValue) par.getValue("dist_scale_fast")).getValue();
        double frictRange = ((RealValue) par.getValue("frict_range")).getValue();
        return distScale * dist * (1 + rng.nextDouble() * frictRange);
    }
}
