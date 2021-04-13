package com.template.states;

import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType;
import com.template.contracts.FungEvoTokenContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(FungEvoTokenContract.class)
public class FungEvoTokenType extends EvolvableTokenType {

    private String someData;
    private List<Party> maintainers;
    private int fractionDigits;
    private UniqueIdentifier linearId;

    public FungEvoTokenType(
           String someData,
           List<Party> maintainers,
           int fractionDigits
    ){
        this.someData = someData;
        this.maintainers = maintainers;
        this.fractionDigits = fractionDigits;
        this.linearId = new UniqueIdentifier();
    }

    @Override
    public int getFractionDigits() {
        return fractionDigits;
    }

    @NotNull
    @Override
    public UniqueIdentifier getLinearId() {
        return linearId;
    }

    @NotNull
    @Override
    public List<Party> getMaintainers() {
        return maintainers;
    }

    public String getSomeData(){
        return someData;
    }

}