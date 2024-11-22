package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author ：zc
 * @date ：2024/11/1  13:19
 */
@RestController
public class DeferredResultController {

	@Autowired
	private Executor asyncExecutor;

	@GetMapping("/test")
	public String test() {
		return "deferredResult";
	}


	@GetMapping("/test2")
	public DeferredResult<String> processDeferredRequest2() {
		DeferredResult<String> deferredResult = new DeferredResult<>(3000L);
		//开启另一个线程
		asyncExecutor.execute(() -> {
			try {
				Thread.sleep(4000L);
				deferredResult.setResult("处理完成");
			} catch (Exception e) {
				deferredResult.setErrorResult(e.getMessage());
			}
		});
		deferredResult.onTimeout(() -> deferredResult.setErrorResult("请求超时"));
		deferredResult.onCompletion(() -> System.out.println("请求完成"));
		return deferredResult;
	}

	class MyTask extends RecursiveTask<String> {
		@Override
		protected String compute() {
			// 将任务拆分成子任务，并递归执行
			// ...
			return "处理完成";
		}
	}

	@GetMapping("/test3")
	public DeferredResult<String> processDeferredRequest() {
		DeferredResult<String> deferredResult = new DeferredResult<>(5000L);
		ForkJoinPool.commonPool().submit(() -> {
			try {
				Thread.sleep(2000);
				deferredResult.setResult("处理完成");
			} catch (Exception e) {
				deferredResult.setErrorResult(e.getMessage());
			}
		});
		return deferredResult;
	}


	@GetMapping("/completable")
	public CompletableFuture<String> processCompletableRequest() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "处理完成";
		});
	}


}
