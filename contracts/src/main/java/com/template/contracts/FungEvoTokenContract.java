package com.template.contracts;

import com.r3.corda.lib.tokens.contracts.EvolvableTokenContract;
import com.template.states.FungEvoTokenType;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import static net.corda.core.contracts.ContractsDSL.requireThat;

// ************
// * Contract *
// ************
public class FungEvoTokenContract extends EvolvableTokenContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String ID = "com.template.contracts.FungEvoTokenContract";

    @Override
    public void additionalCreateChecks(@NotNull LedgerTransaction tx) {
//        FungEvoTokenType newToken = (FungEvoTokenType) tx.getOutputStates().get(0);
//        requireThat( require -> {
//            require.using("Recipient Email cannot be empty",(!newToken.getReceipient().equals("")));
//            require.using("Token Issuer Email and token recipient Email cannot be the same",
//                    (!newToken.getReceipient().equals(newToken.getIssuer())));
//            return null;
//        });
    }

    @Override
    public void additionalUpdateChecks(@NotNull LedgerTransaction tx) {
        /*This additional check does not apply to this use case.
         *This sample does not allow token update */
    }
}