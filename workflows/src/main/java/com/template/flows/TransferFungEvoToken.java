package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.r3.corda.lib.tokens.contracts.types.TokenType;
import com.r3.corda.lib.tokens.workflows.flows.move.MoveTokensUtilities;
import com.r3.corda.lib.tokens.workflows.flows.rpc.MoveFungibleTokens;
import com.r3.corda.lib.tokens.workflows.flows.rpc.MoveFungibleTokensHandler;
import com.r3.corda.lib.tokens.workflows.internal.flows.distribution.UpdateDistributionListFlow;
import com.r3.corda.lib.tokens.workflows.internal.flows.finality.ObserverAwareFinalityFlow;
import com.r3.corda.lib.tokens.workflows.types.PartyAndAmount;
import com.template.states.FungEvoTokenType;
import net.corda.core.contracts.Amount;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;

import java.util.Arrays;

@InitiatingFlow
@StartableByRPC
public class TransferFungEvoToken extends FlowLogic<String> {

    private final String tokenTypeLinearId;
    private final Party receiver;
    private final Party observer;
    private final int quantity;

    public TransferFungEvoToken(String tokenTypeLinearId, Party receiver, Party observer, int quantity){
        this.tokenTypeLinearId = tokenTypeLinearId;
        this.quantity = quantity;
        this.receiver = receiver;
        this.observer = observer;
    }

    @Suspendable
    @Override
    public String call() throws FlowException {
        StateAndRef<FungEvoTokenType> fungEvoTokenTypeStateAndRef = getServiceHub().getVaultService()
                .queryBy(FungEvoTokenType.class).getStates().stream()
                .filter(i -> i.getState().getData().getLinearId().equals(this.tokenTypeLinearId)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Sorry, couldn't find that token type" + this.tokenTypeLinearId));

        //get the TokenType object
        FungEvoTokenType fungEvoTokenType = fungEvoTokenTypeStateAndRef.getState().getData();


        Amount<TokenType> amount = new Amount<>(quantity, fungEvoTokenType.toPointer(FungEvoTokenType.class));
//        subFlow(new MoveFungibleTokens(amount, receiver));

        PartyAndAmount<TokenType> test = new PartyAndAmount(receiver, amount);

        FlowSession receiverSession = initiateFlow(receiver);
        FlowSession observerSession = initiateFlow(observer);

        TransactionBuilder txBuilder = new TransactionBuilder(getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0));
        MoveTokensUtilities.addMoveFungibleTokens(txBuilder, getServiceHub(), Arrays.asList(test), receiver);

        SignedTransaction ptx = getServiceHub().signInitialTransaction((txBuilder));
        SignedTransaction stx = subFlow(new CollectSignaturesFlow(ptx, Arrays.asList(receiverSession, observerSession)));
        SignedTransaction ftx =  subFlow(new FinalityFlow(stx,  Arrays.asList(receiverSession, observerSession)));

        //Add the new token holder to the distribution list
        subFlow(new UpdateDistributionListFlow(ftx));
        return "Done";
        }

        @InitiatedBy(TransferFungEvoToken.class)
    public static class TransferFungEnoTokenResponder extends FlowLogic<Void> {

        private FlowSession counterSession;

        public TransferFungEnoTokenResponder(FlowSession counterSession) {
            this.counterSession = counterSession;
        }

        @Suspendable
        @Override
        public Void call() throws FlowException {
            //simply use the MoveFungibleTokensHandler as the responding flow
            subFlow(new MoveFungibleTokensHandler(counterSession));
            return null;
        }
    }
}

