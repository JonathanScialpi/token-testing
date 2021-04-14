package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableList;
import com.r3.corda.lib.tokens.contracts.states.FungibleToken;
import com.r3.corda.lib.tokens.workflows.flows.rpc.IssueTokens;
import com.r3.corda.lib.tokens.workflows.types.PartyAndAmount;
import com.r3.corda.lib.tokens.workflows.utilities.FungibleTokenBuilder;
import com.template.states.FungEvoTokenType;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.Party;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.SignedTransaction;

import java.util.Arrays;
import java.util.UUID;

@InitiatingFlow
@StartableByRPC
public class IssueFungEvoToken extends FlowLogic<SignedTransaction> {

//    private final String tokenTypeLinearId;
    private final Party issuedTo;

    public IssueFungEvoToken(Party issuedTo){
//        this.tokenTypeLinearId = tokenTypeLinearId;
        this.issuedTo = issuedTo;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // get FungEvoToken state from vault
//        StateAndRef<FungEvoTokenType> fungEvoTokenTypeStateAndRef = getServiceHub().getVaultService()
//                .queryBy(FungEvoTokenType.class).getStates().stream()
//                .filter(i -> i.getState().getData().getLinearId().equals(this.tokenTypeLinearId)).findAny()
//                .orElseThrow(() -> new IllegalArgumentException("Sorry, couldn't find that token type" + this.tokenTypeLinearId));

        FungEvoTokenType fungEvoTokenTypeStateAndRef = getServiceHub().getVaultService().queryBy(FungEvoTokenType.class)
                .getStates().get(0).getState().getData();


        //use token builder
        FungibleToken fungEvoToken = new FungibleTokenBuilder()
                .ofTokenType(fungEvoTokenTypeStateAndRef.toPointer(FungEvoTokenType.class))
                .issuedBy(getOurIdentity())
                .withAmount(100)
                .heldBy(issuedTo)
                .buildFungibleToken();

       return subFlow(new IssueTokens(ImmutableList.of(fungEvoToken)));
    }
}
