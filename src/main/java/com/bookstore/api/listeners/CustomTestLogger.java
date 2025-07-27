package com.bookstore.api.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class CustomTestLogger implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("🟡 [STARTED] " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✅ [PASSED] " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("❌ [FAILED] " + result.getMethod().getMethodName());
        System.out.println("   ➤ Reason: " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⚠️ [SKIPPED] " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("🔷 Starting Test Suite: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("🔶 Finished Test Suite: " + context.getName());
    }
}
