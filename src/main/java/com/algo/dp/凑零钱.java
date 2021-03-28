package com.algo.dp;

import java.util.Arrays;
import java.util.List;

/**
 * 给你 k 种面值的硬币，面值分别为 c1, c2 ... ck，
 * 每种硬币的数量无限，再给一个总金额 amount，
 * 问你最少需要几枚硬币凑出这个金额，如果不可能凑出，算法返回 -1 。
 * <p>
 * 比如说 k = 3，面值分别为 1，2，5，总金额 amount = 11。
 * 那么最少需要 3 枚硬币凑出，即 11 = 5 + 5 + 1。
 */


/**
 * # 初始化 base case
 * dp[0][0][...] = base
 * # 进行状态转移
 * for 状态1 in 状态1的所有取值：
 *     for 状态2 in 状态2的所有取值：
 *         for ...
 *             dp[状态1][状态2][...] = 求最值(选择1，选择2...)
 */
public class 凑零钱 {
    public static void main(String[] args) {
        List<Integer> coins = Arrays.asList(1, 2, 5);
        int amount = 11;

        System.out.println(coinChange(coins, amount));
    }

    //暴力递归
    public static int coinChange(List<Integer> coins, int amount) {
        return dp(coins, amount);
    }

    //假设是dp(3)
    //依次为
    //1+1+1、1+1+2、1+1+5
    //1+2+1、1+2+2、1+2+5 //判断到第2个数为2时，因为3-1=2,2-2=0,返回0,不再判断第三个数字,返回结果为1+1+0=2个
    //1+5+1、1+5+2、1+5+5 //判断到第2个数为5时，因为3-1=2,2-5=-3<0,返回-1,不再判断第三个数字
    //2+1+1、2+1+2、2+1+5 //判断到第2个数为1时，因为3-2=1,1-1=0,返回0,不再判断第三个数字,返回结果为1+1+0=2个
    //2+2+1、2+2+2、2+2+5 //判断到第2个数为2时，因为3-2=1,1-2=-1<0,返回-1,不再判断第三个数字
    //2+5+1、2+5+2、2+5+5 //判断到第2个数为5时，因为3-2=1,1-5=-4<0,返回-1,不再判断第三个数字
    // 如果第一个数是5时,因为5>3,所以3-5=-2<0，会直接返回-1,失败，不再判断后面的数字
    //5+1+1、5+1+2、5+1+5
    //5+2+1、5+2+2、5+2+5
    //5+5+1、5+5+2、5+5+5



    //dp(11) 组合从1*11开始
    // 1*10+1(即1*11)
    // 1*10+2
    // 1*10+5
    // 1*9+2+1
    // 1*9+2+2
    // 1*9+2+5
    //...
    // 5+5+1
    // 5+5+2
    // 5+5+5

    //dp(11)采用5+5+1
    //1+dp(11-5)=1+dp(6)
    //1+[1+dp(6-5)]=1+[1+dp(1)]=2+dp(1)
    //2+[1+dp(1-1]=2+[1+dp(0)]=3+dp(0)=3

    //dp(11)采用5+2+2+2
    //1+dp(11-5)=1+dp(6)
    //1+[1+dp(6-2]=1+[1+dp(4)]=2+dp(4)
    //2+[1+dp(4-2)]=2+[1+dp(2)]=3+dp(2)
    //3+[1+dp(2-2)]=3+[1+dp(0)]=4+dp(0)=4

    //dp(11)采用 5+2+5 则失败
    //1+dp(11-5)=1+dp(6)
    //1+[1+dp(6-2]=1+[1+dp(4)]=2+dp(4)
    //2+[1+dp(4-5)]=2+[1+dp(-1)],出现dp(-1)失败
    public static int dp(List<Integer> coins, int amount) {
        // base case
        if (amount == 0) {
            return 0;
        }

        if (amount < 0) {
            return -1;
        }

        // 求最小值，所以初始化为正无穷
        int res = Integer.MAX_VALUE;
        for (int coin : coins) {
            // 只有递归到最后amount-coin为0时,才能凑出结果,最终相当于subproblem=dp(coins,0)=0,需要的硬币数为subproblem+1=0+1=1
            // 里面的dp又是一个for循环,无数层for循环嵌套
            int subproblem = dp(coins, amount - coin);

            //子问题无解，跳过
            if (subproblem == -1) {
                continue;
            }

            //res = min(res, 1 + subproblem)
            //res = (res < 1+subproblem) ? res : 1+subproblem;
            if (1 + subproblem < res) {
                res = 1 + subproblem;
            }

        }

        if (res != Integer.MAX_VALUE) {
            return res;
        } else {
            return -1;
        }

    }
}
