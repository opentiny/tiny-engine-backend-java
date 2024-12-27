/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 * <p>
 * Use of this source code is governed by an MIT-style license.
 * <p>
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.common.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 基础对象
 *
 * @since 2024-10-20
 */
public class PageQueryVo<T> {
    public static final int PAGESIZE_MAX = 200;
    public static final int PAGESIZE_DEFAULT = 10;
    private int pageSize = PAGESIZE_DEFAULT;
    private int current = 0;
    private T data;

    /**
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        if (pageSize > PAGESIZE_MAX) {
            pageSize = 200;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        this.pageSize = pageSize;
    }

    /**
     * get page size
     *
     * @return page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * get page num
     *
     * @return page num
     */
    public int getCurrent() {
        return current;
    }

    /**
     * set page num
     *
     * @param current
     */
    public void setCurrent(int current) {
        this.current = current;
    }

    /**
     * query data
     *
     * @return data
     */
    public T getData() {
        return data;
    }

    /**
     * set query data
     *
     * @param data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * 分页查询对象
     *
     * @return page
     */
    public <E> Page<E> getPage() {
        Page<E> page = new Page<>();
        page.setCurrent(this.current);
        page.setSize(this.pageSize);
        return page;
    }
}
