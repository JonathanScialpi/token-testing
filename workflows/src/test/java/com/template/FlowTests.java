package com.template;

import com.google.common.collect.ImmutableList;
import com.template.flows.CreateFungEvoTokenType;
import com.template.flows.IssueFungEvoToken;
import com.template.flows.TransferFungEvoToken;
import com.template.states.FungEvoTokenType;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.identity.Party;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.SignedTransaction;
import net.corda.testing.node.MockNetwork;
import net.corda.testing.node.MockNetworkParameters;
import net.corda.testing.node.StartedMockNode;
import net.corda.testing.node.TestCordapp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FlowTests {
    private MockNetwork network;
    private StartedMockNode a;
    private StartedMockNode b;
    private StartedMockNode c;

    @Before
    public void setup() {
        network = new MockNetwork(new MockNetworkParameters().withCordappsForAllNodes(ImmutableList.of(
                TestCordapp.findCordapp("com.template.contracts"),
                TestCordapp.findCordapp("com.template.flows"))));
        a = network.createPartyNode(null);
        b = network.createPartyNode(null);
        c = network.createPartyNode(null);
        network.runNetwork();
    }

    @After
    public void tearDown() {
        network.stopNodes();
    }

//    @Test
//    public void dummyTest() {
//        IssueFungEvoTokenType flow = new IssueFungEvoTokenType(b.getInfo().getLegalIdentities().get(0));
//        Future<SignedTransaction> future = a.startFlow(flow);
//        network.runNetwork();
//
//        //successful query means the state is stored at node b's vault. Flow went through.
//        QueryCriteria inputCriteria = new QueryCriteria.VaultQueryCriteria().withStatus(Vault.StateStatus.UNCONSUMED);
//        FungEvoTokenType state = b.getServices().getVaultService()
//                .queryBy(FungEvoTokenType.class,inputCriteria).getStates().get(0).getState().getData();
//    }

    @Test
    public void returnsFullySignedTxFromAllParties() throws ExecutionException, InterruptedException {
        Party investorA = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party investorB = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party tie =  c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        CreateFungEvoTokenType myFlow = new CreateFungEvoTokenType("Hello world", 10);
        Future<SignedTransaction> future = a.startFlow(myFlow);
        network.runNetwork();

        SignedTransaction ptx = future.get();
//        assert (ptx.getTx().getInputs().isEmpty());
//        assert (ptx.getTx().getOutputs().get(0).getData() instanceof CreateFungEvoTokenType);


        QueryCriteria.VaultQueryCriteria inputCriteria = new QueryCriteria.VaultQueryCriteria().withStatus(Vault.StateStatus.UNCONSUMED);
        FungEvoTokenType state = a.getServices().getVaultService().queryBy(FungEvoTokenType.class).getStates().get(0).getState().getData();

//        assert (state instanceof  FungEvoTokenType);

        IssueFungEvoToken issueFlow = new IssueFungEvoToken(investorA);
        Future<SignedTransaction> futureIssue = a.startFlow(issueFlow);
        network.runNetwork();

        SignedTransaction ptx2 = future.get();

        TransferFungEvoToken transferFlow = new TransferFungEvoToken(state.getLinearId().toString(), investorB, tie, 5);
        Future<SignedTransaction> futureTransfer = a.startFlow(issueFlow);
        network.runNetwork();

        SignedTransaction ptx3 = future.get();

        System.out.println(ptx3.getRequiredSigningKeys());
    }



}
