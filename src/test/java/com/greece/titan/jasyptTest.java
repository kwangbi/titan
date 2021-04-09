package com.greece.titan;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

public class jasyptTest {

    @Test
    public void test() throws Exception{
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword("titan");

        //String url = pbeEnc.encrypt("scm-temp-rds.cluster-ckwdstlfzwlh.ap-northeast-2.rds.amazonaws.com:3306/scm");
        //System.out.println("url = " + url);

        String userName = pbeEnc.encrypt("root");
        System.out.println("userName = " + userName);

        String password = pbeEnc.encrypt("root");
        System.out.println("password = " + password);

        //System.out.println("url des = " + pbeEnc.decrypt(url));
        System.out.println("userName des = " + pbeEnc.decrypt(userName));
        System.out.println("password des = " + pbeEnc.decrypt(password));



    }


}
