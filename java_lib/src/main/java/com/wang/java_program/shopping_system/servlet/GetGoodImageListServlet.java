package com.wang.java_program.shopping_system.servlet;

import com.wang.java_util.GsonUtil;
import com.wang.java_util.TextUtil;
import com.wang.java_program.shopping_system.Response;
import com.wang.java_program.shopping_system.StateCode;
import com.wang.java_program.shopping_system.bean.GoodImage;
import com.wang.java_program.shopping_system.dao.GoodImageDao;
import com.wang.web.CustomHttpServlet;

import java.util.HashMap;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class GetGoodImageListServlet extends CustomHttpServlet {

    private Response<List<GoodImage>> response;

    @Override
    protected String[] onGetParameterStart() {
        return new String[]{"goodId"};
    }

//    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {

        String goodId = (String) parameterMap.get("goodId");

        if (TextUtil.isEmpty(goodId)) {
            response = new Response<>(StateCode.PARAM_ERROR, "goodId����Ϊ��");
            return;
        }

        try {
            GoodImageDao goodImageDao = new GoodImageDao();
            List<GoodImage> goodImageList = goodImageDao.query(Integer.parseInt(goodId));
            response = new Response<>(StateCode.OK, null, goodImageList);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response<>(StateCode.ERROR_UNKNOWN, e.toString());
        }

    }

    @Override
    protected String onWriteResultStart() {
        return GsonUtil.formatJson(response);
    }
}
