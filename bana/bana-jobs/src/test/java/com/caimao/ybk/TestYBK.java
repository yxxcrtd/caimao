package com.caimao.ybk;

import com.caimao.bana.jobs.tasks.YbkQuotationTask;
import org.junit.Test;
import parent.BaseTest;

import javax.annotation.Resource;

public class TestYBK extends BaseTest {
    @Resource
    YbkQuotationTask ybkQuotationTask;

    @Test
    public void testYBKQuotationTask() throws Exception{
       // ybkQuotationTask.autoExtTargetTask();
    }
}
