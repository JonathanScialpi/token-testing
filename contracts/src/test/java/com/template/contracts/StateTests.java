package com.template.contracts;

import com.template.states.FungEvoTokenType;
import org.junit.Test;

public class StateTests {

    //Mock State test check for if the state has correct parameters type
    @Test
    public void hasFieldOfCorrectType() throws NoSuchFieldException {
        FungEvoTokenType.class.getDeclaredField("msg");
        assert (FungEvoTokenType.class.getDeclaredField("msg").getType().equals(String.class));
    }
}