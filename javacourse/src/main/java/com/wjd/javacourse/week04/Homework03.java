package com.wjd.javacourse.week04;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/11/27
 */
public class Homework03 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        long start = System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = sum(); //这是得到的返回值
                // 确保  拿到result 并输出
                System.out.println("1:");
                System.out.println("异步计算结果为：" + result);
                System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
            }
        }).start();

        new Thread() {
            @Override
            public void run() {
                int result = sum(); //这是得到的返回值
                // 确保  拿到result 并输出
                System.out.println("2:");
                System.out.println("异步计算结果为：" + result);
                System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
            }
        }.start();

        ExecutorService es = Executors.newFixedThreadPool(30);
        es.execute(new Runnable() {
            @Override
            public void run() {
                int result = sum(); //这是得到的返回值
                // 确保  拿到result 并输出
                System.out.println("3:");
                System.out.println("异步计算结果为：" + result);
                System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
            }
        });
        es.shutdown();

        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                int result = sum(); //这是得到的返回值
                // 确保  拿到result 并输出
                System.out.println("4:");
                System.out.println("异步计算结果为：" + result);
                System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
            }
        }, Executors.newFixedThreadPool(30));

        FutureTask<Integer> future = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();
            }
        });
        Thread thread05 = new Thread(future);
        thread05.start();
        int result = future.get();
        System.out.println("5:");
        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int result = sum(); //这是得到的返回值
                // 确保  拿到result 并输出
                System.out.println("6:");
                System.out.println("异步计算结果为：" + result);
                System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
            }
        }, 0);

        Thread.currentThread().interrupted();
        // 然后退出main线程
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }
}
