/*
 * Copyright 2009-2015 Aarhus University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.lattice.AnalysisLatticeElement;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.SpecialVars;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.solver.IAnalysis;
import dk.brics.tajs.solver.IEdgeTransfer;
import dk.brics.tajs.solver.IWorkListStrategy;
import dk.brics.tajs.solver.SolverSynchronizer;

/**
 * Encapsulation of the analysis using {@link State}, {@link Context},
 * {@link Solver}, {@link InitialStateBuilder}, {@link Transfer},
 * {@link WorkListStrategy}, {@link SpecialVars}, and {@link Monitoring}.
 */
public final class Analysis implements IAnalysis<State, Context, CallEdge<State>, IAnalysisMonitoring<State, Context, CallEdge<State>>, SpecialVars, Analysis> {

    private Solver solver;

    private InitialStateBuilder initial_state_builder;

    private Transfer transfer;

    private WorkListStrategy worklist_strategy;

    private IAnalysisMonitoring<State, Context, CallEdge<State>> monitoring;

    private EvalCache eval_cache;

    /**
     * Constructs a new analysis object.
     */
    public Analysis(SolverSynchronizer sync) {
        initial_state_builder = new InitialStateBuilder();
        transfer = new Transfer();
        worklist_strategy = new WorkListStrategy();
        eval_cache = new EvalCache();
        solver = new Solver(this, sync);
    }

    @Override
    public AnalysisLatticeElement<State, Context, CallEdge<State>> makeAnalysisLattice(FlowGraph fg) {
        return new AnalysisLatticeElement<>(fg);
    }

    @Override
    public InitialStateBuilder getInitialStateBuilder() {
        return initial_state_builder;
    }

    @Override
    public Transfer getNodeTransferFunctions() {
        return transfer;
    }

    @Override
    public IEdgeTransfer<State, Context> getEdgeTransferFunctions() {
        return transfer;
    }

    @Override
    public IWorkListStrategy<Context> getWorklistStrategy() {
        return worklist_strategy;
    }

    @Override
    public IAnalysisMonitoring<State, Context, CallEdge<State>> getMonitoring() {
        if (monitoring == null)
            monitoring = new Monitoring<>();
        return monitoring;
    }

    @Override
    public void setSolverInterface(Solver.SolverInterface c) {
        transfer.setSolverInterface(c);
        worklist_strategy.setCallGraph(c.getAnalysisLatticeElement().getCallGraph());
    }

    /**
     * Returns the solver.
     */
    public Solver getSolver() {
        return solver;
    }

    /**
     * Returns the eval cache.
     */
    public EvalCache getEvalCache() {
        return eval_cache;
    }

    @Override
    public CallEdge<State> makeCallEdge(State edge_state) {
        return new CallEdge<>(edge_state);
    }

    @Override
    public boolean allowNextIteration() {
        return true;
    }
}
