package ent.otego.saurus.integration;

import static org.junit.Assert.*;

import org.junit.Test;

public class MessageUtilsTest {

    @Test
    public void millisecondsToTime() {
        System.out.println(MessageUtils.millisecondsToTime(60183));
    }
}