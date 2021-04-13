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
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;

public class MintFungEvoToken extends FlowLogic<SignedTransaction> {

    private final String tokenTypeLinearId;
    private final Party issuedTo;

    public MintFungEvoToken(String tokenTypeLinearId, Party issuedTo){
        this.tokenTypeLinearId = tokenTypeLinearId;
        this.issuedTo = issuedTo;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // get FungEvoToken state from vault
        StateAndRef<FungEvoTokenType> fungEvoTokenTypeStateAndRef = getServiceHub().getVaultService()
                .queryBy(FungEvoTokenType.class).getStates().stream()
                .filter(i -> i.getState().getData().getLinearId().equals(this.tokenTypeLinearId)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Sorry, couldn't find that token type" + this.tokenTypeLinearId));

        //get the TokenType object
        FungEvoTokenType fungEvoTokenType = fungEvoTokenTypeStateAndRef.getState().getData();

        //use token builder
        FungibleToken fungEvoToken = new FungibleTokenBuilder()
                .ofTokenType(fungEvoTokenType.toPointer(FungEvoTokenType.class))
                .issuedBy(getOurIdentity())
                .withAmount(100)
                .heldBy(issuedTo)
                .buildFungibleToken();

       return subFlow(new IssueTokens(ImmutableList.of(fungEvoToken)));
    }
}
