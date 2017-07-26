package com.hungsum.framework.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by zhaixuan on 2017/7/26.
 */

public class HsRound
{
    public static double Round(Double d,int bit)
    {
        return Round(d,bit,RoundingMode.HALF_UP);
    }

    public static double Round(Double d, int bit, RoundingMode roundingMode)
    {
        return new BigDecimal(d.toString()).setScale(bit,roundingMode).doubleValue();
    }
}
