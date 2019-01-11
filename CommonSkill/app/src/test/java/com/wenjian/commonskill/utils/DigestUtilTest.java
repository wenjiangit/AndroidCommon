package com.wenjian.commonskill.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Description: DigestUtilTest
 * Date: 2018/7/12
 *
 * @author jian.wen@ubtrobot.com
 */
public class DigestUtilTest {

    private static final String SHA1_WENJIAN = "98f99cbc4292fc430d21edc9d2def97528e788fb";
    private static final String SHA256_WENJIAN = "ed2d2f861b7e2b5a94e35b4e600d61871544f55de8ad8bd9d3488dffb0e903cf";
    private static final String MD5_WENJIAN = "02fc951d0fc3165149b8fbe5cc757503";

    @Test
    public void md5() throws Exception {
        String md5 = DigestUtil.md5("wenjian".getBytes());
        assertEquals(MD5_WENJIAN, md5);
        assertEquals(MD5_WENJIAN.length(), 32);

        md5 = HashUtil.md5("wenjian");
        assertEquals(MD5_WENJIAN, md5);
        assertEquals(MD5_WENJIAN.length(), 32);
    }

    @Test
    public void sha1() throws Exception {
        String sha1 = DigestUtil.sha1("wenjian".getBytes());
        assertEquals(SHA1_WENJIAN, sha1);
        assertEquals(SHA1_WENJIAN.length(), 40);

        sha1 = HashUtil.md5("wenjian");
        assertEquals(MD5_WENJIAN, sha1);
        assertEquals(MD5_WENJIAN.length(), 32);
    }

    @Test
    public void sha256() throws Exception {
        String sha256 = DigestUtil.sha256("wenjian".getBytes());
        assertEquals(SHA256_WENJIAN, sha256);
        assertEquals(SHA256_WENJIAN.length(), 64);
        sha256 = HashUtil.md5("wenjian");
        assertEquals(MD5_WENJIAN, sha256);
        assertEquals(MD5_WENJIAN.length(), 32);


    }

    @Test
    public void hash() throws Exception {
        System.out.println("wenjian".hashCode());
    }


}