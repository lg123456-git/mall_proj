package com.wn.common;

/**
 * @BelongsProject: test4_29
 * @BelongsPackage: com.wn.common
 * @Author: 廖刚
 * @CreateTime: 2020-04-30 14:33
 * @Description:
 */
public interface Const {

    interface RsaPath{
        String PUBLIC_RSA = "d:\\rsa\\rsa.pub";
        String PRIVATE_RSA = "d:\\rsa\\rsa.pri";
    }


    //后端状态码
    interface Manager{
        //后端登录状态码
        Integer LOGIN_STATUS_ERROR = 1;
        Integer LOGIN_STATUS_SUCCESS = 0;
        Integer ROLE_LOGIN_STATUS_ERROR = 10;

        Integer CATEGORY_REPEATE_STATUS_ERROR = 501;
        Integer CATEGORY_HAS_CHILD_STATUS_ERROR = 502;
        Integer CATEGORY_STATUS_SUCCESS = 200;
        Integer CATEGORY_STATUS_ERROR = 500;
    }

    //文件服务器地址
    interface Image{
        String IMGGE_HOST = "http://front.bajin.com/MallProj/fileupload/";
    }

    interface Reception{
        Integer STATUS_ERROR = 1;
        Integer STATUS_SUCCESS = 0;
    }

}
