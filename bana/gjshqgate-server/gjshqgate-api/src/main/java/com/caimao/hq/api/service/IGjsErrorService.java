/**
 *
 */
package com.caimao.hq.api.service;


import com.caimao.hq.api.entity.GjsError;

/**
 * @author yzc
 */
public interface IGjsErrorService {

    public int insert(GjsError gjsError);

    public int insert(String finance_mic,String message,String content,String type);
}
