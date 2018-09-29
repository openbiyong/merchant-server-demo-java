package com.biyong.open.server.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncUtil {
  private static final ExecutorService pool =
      new ThreadPoolExecutor(
          2, 50, 10, TimeUnit.MINUTES,
          new ArrayBlockingQueue<>(1024),
          (r, executor) -> {
            try {
              executor.getQueue().put(r);
            } catch (Exception e) {
              e.printStackTrace();
            }
          });

  public static void run(Runnable task) {
    pool.submit(task);
  }
}
