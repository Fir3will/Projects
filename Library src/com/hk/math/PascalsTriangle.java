/**************************************************************************
 *
 * [2017] Fir3will, All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of "Fir3will" and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to "Fir3will" and its suppliers
 * and may be covered by U.S. and Foreign Patents, patents
 * in process, and are protected by trade secret or copyright law
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from "Fir3will".
 **************************************************************************/
package com.hk.math;

public class PascalsTriangle
{
    public static int factorial(int a)
    {
        return (int) factorial((long) a);
    }

    public static long factorial(long a)
    {
        long product = 1;

        for (long i = 2; i <= a; i++)
        {
            product *= i;
        }

        return product;
    }

    public static int nCr(int n, int r)
    {
        return (int) nCr((long) n, (long) r);
    }

    public static int nPr(int n, int r)
    {
        return (int) nPr((long) n, (long) r);
    }

    public static long nCr(long n, long r)
    {
        return factorial(n) / (factorial(r) * factorial(n - r));
    }

    public static long nPr(long n, long r)
    {
        return factorial(n) / factorial(n - r);
    }

    public static int[] pascalsTriangle(int exp)
    {
        int[] coeff = new int[exp + 1];
        for (int i = 0; i < exp; i++)
        {
            coeff[i] = nCr(exp, i);
        }
        coeff[coeff.length - 1] = 1;
        return coeff;
    }

    public static String getTerm(int exp, int term)
    {
        int a = exp - term;
        int i = pascalsTriangle(exp)[term];
        return (i == 1 ? "" : i) + (a == 0 ? "" : "a" + getExponent(a)) + (term == 0 ? "" : "b" + getExponent(term));
    }

    public static String getFullExpression(int exp)
    {
        if (exp == 0)
            return "1";
        String s = "";
        for (int i = 0; i < exp + 1; i++)
        {
            s += getTerm(exp, i) + (i == exp ? "" : " + ");
        }
        return s;
    }

    public static String getExponent(int num)
    {
        if (num == 1)
            return "";
        String n = String.valueOf(num);
        String s = "";
        for (int i = 0; i < n.length(); i++)
        {
            s += exponents[Integer.parseInt(String.valueOf(n.charAt(i)))];
        }
        return s;
    }

    private static final char[] exponents = "â�°Â¹Â²Â³â�´â�µâ�¶â�·â�¸â�¹".toCharArray();

    private PascalsTriangle()
    {
    }
}
