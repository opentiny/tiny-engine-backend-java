package com.tinyengine.it.common.base;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.IBaseError;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Result class test
 *
 * @since 2024-10-29
 */
class ResultTest {
    @Test
    void testSuccess() {
        Result<Object> result = Result.success();
        Assertions.assertTrue(result.isSuccess());
    }

    @Test
    void testSuccessWithListData() {
        List<String> data = asList("a", "b");
        Result<List<String>> result = Result.success(data);
        Assertions.assertTrue(result.isSuccess());
        Assertions.assertEquals(2, result.getData().size());
    }

    @Test
    void testSuccess3() {
        Result<List<String>> listResult = new Result<>(null, "success", Arrays.asList("a"), true);
        Assertions.assertEquals("a", listResult.getData().get(0));
        listResult = new Result<>(null, "fail", Arrays.asList("a"), false);
        assertFalse(listResult.isSuccess());
        Assertions.assertEquals("a", listResult.getData().get(0));
        Assertions.assertEquals("fail", listResult.getErrMsg());
    }

    @Test
    void testFail() {
        IBaseError error = ExceptionEnum.CM001;
        Result<String> failed = Result.failed(error);
        assertNull(failed.getData());
        assertFalse(failed.isSuccess());
        Assertions.assertEquals(ExceptionEnum.CM001.getResultCode(), failed.getCode());
    }
}