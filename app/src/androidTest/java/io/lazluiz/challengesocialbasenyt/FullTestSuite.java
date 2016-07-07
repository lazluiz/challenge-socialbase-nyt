package io.lazluiz.challengesocialbasenyt;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by luiz on 07/07/16.
 * E-mail: lf.lazzarin@gmail.com
 * GitHub: github.com/luizfelippe
 */


public class FullTestSuite extends TestSuite {
    public static Test suite() {
        return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }

    public FullTestSuite() {
        super();
    }
}
