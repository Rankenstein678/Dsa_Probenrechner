package com.rankenstein.dsahelper;

import com.rankenstein.dsahelper.logic.ChanceLogic;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ChanceTest {
    @Test
    public void chanceLogicStandard() {assertEquals(12.875, ChanceLogic.calculateChance(10,10,10,0,0)[0]*100,0.1);}
    @Test
    public void chanceLogicErleichterung() {assertEquals(35.3125, ChanceLogic.calculateChance(10,10,10,0,-5)[0]*100,0.1);}
    @Test
    public void chanceLogicErschwernis() {assertEquals(2.1250, ChanceLogic.calculateChance(10,10,10,0,5)[0]*100,0.1);}
    @Test
    public void chanceLogicTalentwert() {assertEquals(47.1750, ChanceLogic.calculateChance(10,10,10,7,0)[0]*100,0.1);}
    @Test
    public void chanceLogicErlTaw() {assertEquals(30.0250, ChanceLogic.calculateChance(10,10,10,7,3)[0]*100,0.1);}
    @Test
    public void chanceLogicErschwTaw() {assertEquals(68.3750, ChanceLogic.calculateChance(10,10,10,7,-3)[0]*100,0.1);}

}