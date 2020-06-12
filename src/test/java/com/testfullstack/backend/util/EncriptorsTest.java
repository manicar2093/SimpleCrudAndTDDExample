package com.testfullstack.backend.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class EncriptorsTest {

    private Encriptors encriptors;

    @Before
    public void setUp(){
        this.encriptors = new Encriptors();
    }

     @Test
    public void encriptiongSh256AString(){

        String password_hash = encriptors.hashStringToSha256("passwordtest");
        String password_hash2 = encriptors.hashStringToSha256("contrasenaprueba");

         Assertions.assertEquals(
                 "a7574a42198b7d7eee2c037703a0b95558f195457908d6975e681e2055fd5eb9",
                 password_hash);
         Assertions.assertEquals(
                 "fdd324c4643e3dc57b46923a200bb7db8ce1f71c58b96196859e2db2fdeff47c",
                 password_hash2);

     }

}