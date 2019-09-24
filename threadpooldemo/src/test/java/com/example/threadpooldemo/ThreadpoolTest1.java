package com.example.threadpooldemo;

import org.junit.Test;

import java.util.concurrent.*;

public class ThreadpoolTest1 {


    @Test
    public void test1() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(6, 10, 1L, TimeUnit.DAYS, new ArrayBlockingQueue<>(6),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        /**
         * 1.常用参数
         *  corePoolSize  核心线程池大小
         *  maxmumPoolSize
         *、
         *
         *  任务缓存队列及排队策略
         *
         * 　　workQueue的类型为BlockingQueue<Runnable>，通常可以取下面三种类型：
         *
         * 　　1）ArrayBlockingQueue：基于数组的先进先出队列，此队列创建时必须指定大小；
         *
         * 　　2）LinkedBlockingQueue：基于链表的先进先出队列，如果创建时没有指定此队列大小，则默认为Integer.MAX_VALUE；
         *
         * 　　3）synchronousQueue：这个队列比较特殊，它不会保存提交的任务，而是将直接新建一个线程来执行新来的任务。
         *
         * 任务拒绝策略
         *
         * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
         * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
         * ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
         *
         *
         * 过程总结：
         *
         *     如果当前线程池中的线程数目小于corePoolSize，则每来一个任务，就会创建一个线程去执行这个任务；
         *     如果当前线程池中的线程数目>=corePoolSize，则每来一个任务，会尝试将其添加到任务缓存队列当中，
         *     若添加成功，则该任务会等待空闲线程将其取出去执行；若添加失败（一般来说是任务缓存队列已满），则会尝试创建新的线程去执行这个任务；
         *     如果当前线程池中的线程数目达到maximumPoolSize，则会采取任务拒绝策略进行处理；
         *     如果线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止，
         *     直至线程池中的线程数目不大于corePoolSize；如果允许为核心池中的线程设置存活时间，
         *     那么核心池中的线程空闲时间超过keepAliveTime，线程也会被终止。
         *
         */

        /**
         *
         * 并不提倡我们直接使用ThreadPoolExecutor，而是使用Executors类中提供的几个静态方法来创建线程池：
         *
         * Executors.newCachedThreadPool();        //创建一个缓冲池，缓冲池容量大小为Integer.MAX_VALUE
         * Executors.newSingleThreadExecutor();   //创建容量为1的缓冲池
         * Executors.newFixedThreadPool(int);    //创建固定容量大小的缓冲池
         */

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        ExecutorService executorService1 = Executors.newCachedThreadPool();
        ExecutorService executorService2 = Executors.newSingleThreadExecutor();


        for(int i=0;i<15;i++){
            MyTask myTask = new MyTask(i);
            executor.execute(myTask);
            System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
                    executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
        }
        executor.shutdown();

    }


    class MyTask implements Runnable {
        private int taskNum;

        public MyTask(int num) {
            this.taskNum = num;
        }

        @Override
        public void run() {
            System.out.println("正在执行task "+taskNum);
            try {
                Thread.currentThread().sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task "+taskNum+"执行完毕");
        }
    }
}
