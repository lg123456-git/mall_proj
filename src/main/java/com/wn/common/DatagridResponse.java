package com.wn.common;

import lombok.Data;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.common
 * @Author: 廖刚
 * @CreateTime: 2020-05-15 16:02
 * @Description:
 */
@Data
public class DatagridResponse {

    private Integer total;
    private Object rows;

    public DatagridResponse() {
    }

    public DatagridResponse(Integer total, Object rows) {
        this.total = total;
        this.rows = rows;
    }
}
