package com.template.contracts;

import com.template.states.FungEvoTokenType;
import net.corda.core.identity.CordaX500Name;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockServices;
import org.junit.Test;

import java.util.Arrays;

import static net.corda.testing.node.NodeTestUtils.ledger;


public class ContractTests {
    private final MockServices ledgerServices = new MockServices(Arrays.asList("com.template"));
    TestIdentity alice = new TestIdentity(new CordaX500Name("Alice",  "TestLand",  "US"));
    TestIdentity bob = new TestIdentity(new CordaX500Name("Alice",  "TestLand",  "US"));

//    @Test
//    public void issuerAndRecipientCannotHaveSameEmail() {
//        FungEvoTokenType state = new FungEvoTokenType("Hello-World",alice.getParty(),bob.getParty());
//        ledger(ledgerServices, l -> {
//            l.transaction(tx -> {
//                tx.input(TemplateContract.ID, state);
//                tx.output(TemplateContract.ID, state);
//                tx.command(alice.getPublicKey(), new TemplateContract.FungEvoTokenContract.Send());
//                return tx.fails(); //fails because of having inputs
//            });
//            l.transaction(tx -> {
//                tx.output(TemplateContract.ID, state);
//                tx.command(alice.getPublicKey(), new TemplateContract.FungEvoTokenContract.Send());
//                return tx.verifies();
//            });
//            return null;
//        });
//    }
}