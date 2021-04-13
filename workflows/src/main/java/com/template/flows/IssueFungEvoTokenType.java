package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.r3.corda.lib.tokens.contracts.states.FungibleToken;
import com.r3.corda.lib.tokens.workflows.flows.rpc.CreateEvolvableTokens;
import com.r3.corda.lib.tokens.workflows.utilities.FungibleTokenBuilder;
import com.template.states.FungEvoTokenType;
import net.corda.core.contracts.TransactionState;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class IssueFungEvoTokenType extends FlowLogic<SignedTransaction> {

    //private variables
    private Party issuer;
    private Party receiver;
    private String msg;
    private int digits;
    private List<Party> maintainers;


    //public constructor
    public IssueFungEvoTokenType(Party receiver, String msg, int digits){
        this.receiver = receiver;
        this.msg = msg;
        this.digits = digits;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {

        this.issuer = getOurIdentity();
        this.maintainers.add(issuer);

        // Step 1. Get a reference to the notary service on our network and our key pair.
        // Note: ongoing work to support multiple notary identities is still in progress.
        final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        final FungEvoTokenType fungEvoToken = new FungEvoTokenType(msg,maintainers,digits);

        TransactionState transactionState = new TransactionState(fungEvoToken, notary);
        return subFlow(new CreateEvolvableTokens(transactionState));
    }
}
