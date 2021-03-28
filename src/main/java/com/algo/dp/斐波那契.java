package com.algo.dp;

// 1 2 3 4 5 6 7
// 1 1 2 3 5 8 13
public class 斐波那契 {
    public static void main(String[] args) {
        // h(5)+h(4)
        // h(4)+h(3)+h(3)+h(2)
        // h(3)+h(2)+h(2)+h(1)+h(2)+h(1)+h(1)+h(0)
        // h(2)+h(1)+h(1)+h(0)+h(1)+h(0)+h(1)+h(1)+h(0)+h(1)+h(1)+h(0)
        // h(1)+h(0)+h(1)+h(1)+h(0)+h(1)+h(0)+h(1)+h(1)+h(0)+h(1)+h(1)+h(0)
        // h(1)     +h(1)+h(1)     +h(1)     +h(1)+h(1)     +h(1)+h(1)
        System.out.println(fib(6));


        /**
         *  h(6)=h(5)+h(4)
         *  h(5)=h(4)+h(3)
         *  h(4)=h(3)+h(2)
         *
         *  h(2)=1
         *  h(1)=1
         *  h(3)=h(2)+h(1)=1+1=2
         *  0,0,0,2,0,0,0
         *
         *  h(2)=1
         *  h(4)=h(3)+h(2)=2+1=3
         *  0,0,0,2,3,0,0
         *
         *  h(3)=2
         *  h(5)=h(4)+h(3)=3+2=5
         *  0,0,0,2,3,5,0
         *
         *  h(4)=3
         *  h(6)=h(5)+h(4)=5+3=8
         *  0,0,0,2,3,5,8
         */
        //h(5)+h(4)
        //h(4)+h(3)+h(4)
        //h(3)+h(2)+h(3)+h(4)
        //h(2)+h(1)+h(2)+h(3)+h(4)
        //(1+1)+1+h(3)+h(4)
        //3+2+h(4)
        //5+3
        System.out.println(fib_memo(6));

        //动态规划
        //状态转移方程
        //f(n)=1,n=1,2
        //f(n-1)+f(n-2),n>=3
        System.out.println(fib_dp(6));

        //空间压缩
        System.out.println(fib_dp_compress(6));

    }

    // 暴利递归
    public static int fib(int N) {
        if (N == 1 || N == 2) {
            return 1;
        } else {
            return fib(N - 1) + fib(N - 2);
        }
    }

    //带备忘录的递归解法(自顶向下)

    /**
     * 至此，带备忘录的递归解法的效率已经和迭代的动态规划解法一样了。
     * 实际上，这种解法和迭代的动态规划已经差不多了，只不过这种方法叫做「自顶向下」，动态规划叫做「自底向上」。
     *
     * @param N
     * @return
     */
    public static int fib_memo(int N) {
        if (N == 1) {
            return 1;
        }

        // 注意要是N+1,为了方便表示第0个不用
        int[] memo = new int[N + 1];

        return helper(memo, N);
    }

    public static int helper(int[] memo, int n) {
        if (n == 1 || n == 2) {
            return 1;
        }

        if (memo[n] != 0) {
            return memo[n];
        }

        memo[n] = helper(memo, n - 1) + helper(memo, n - 2);
        return memo[n];

    }


    //dp 数组的迭代解法(自底向上)
    //状态转移方程
    //f(n)=1,n=1,2
    //f(n)=f(n-1)+f(n-2),n>=3
    public static int fib_dp(int N) {
        if (N < 1) {
            return 0;
        }

        if (N == 1 || N == 2) {
            return 1;
        }

        int[] dp = new int[N + 1];

        dp[1] = dp[2] = 1;

        for (int i = 3; i < dp.length; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }

        return dp[N];

    }

    //状态压缩
    //把空间复杂度降为 O(1)
    public static int fib_dp_compress(int N) {
        if (N < 1) {
            return 0;
        }

        if (N == 1 || N == 2) {
            return 1;
        }

        // 上一个:f(n-2)
        int prev = 1;
        // 当前:f(n-1)
        int curr = 1;

        for (int i = 3; i <= N; i++) {
            int sum = prev + curr;
            prev = curr;
            curr = sum;
        }

        return curr;
    }

}
