package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.r3.corda.lib.tokens.workflows.flows.rpc.CreateEvolvableTokens;
import com.template.states.FungEvoTokenType;
import net.corda.core.contracts.TransactionState;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class CreateFungEvoTokenType extends FlowLogic<SignedTransaction> {

    //private variables
    private Party issuer;
    private String msg;
    private int digits;
    private List<Party> maintainers; /// < -- here


    //public constructor
    public CreateFungEvoTokenType(String msg, int digits){
        this.msg = msg;
        this.digits = digits;
        this.maintainers = new ArrayList<>();
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {

        this.issuer = getOurIdentity();
        this.maintainers.add(issuer);

        // Step 1. Get a reference to the notary service on our network and our key pair.
        // Note: ongoing work to support multiple notary identities is still in progress.
        final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        final FungEvoTokenType fungEvoToken = new FungEvoTokenType(msg, maintainers,digits);

        TransactionState transactionState = new TransactionState(fungEvoToken, notary);
        return subFlow(new CreateEvolvableTokens(transactionState));
    }
}
