package com.wn.util;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * Base64位字符串转换为图片工具类
 * @author Administrator
 *
 */
public class Base64ToImageUtil {
	private static boolean flag = false;
	/**
	 * base64转图片
	 * @param imgStr
	 * @param path
	 * @return
	 */
	public static boolean generateImage(String imgStr, String path) {
		if (imgStr == null) {
			return false;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		// 解密
		try {
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
			return true;
			
		} catch (IOException e) {
			return false;
		}
			
	}
	
	
	
	/**
	 * 对外调用的方法
	 * 
	 * 把json字符串转换成本地图片
	 * @param jsonStrImg 字符串
	 * @param path	路径 如 ："D:/"
	 * @return 返回true表示保存成功，否则保存失败
	 */
	public static String base64ImgToFile(String jsonStrImg,String path,String host) {
		List<Image> imgList = new ArrayList<Image>();

		String[] split = jsonStrImg.split("base64,");
		for (int i = 0; i < split.length; i++) {
			if(i != 0) {
				//截取图片字符串
				String imgStr = split[i];
				int indexOf = imgStr.indexOf("\" data-filename");
				
				String substring = imgStr.substring(0, indexOf);

				Image image = new Image(UUID.randomUUID().toString()+".jpg", substring);
				imgList.add(image);
			}
		}
		
		for (int i = 0; i < imgList.size(); i++) {
			boolean generateImage = Base64ToImageUtil.generateImage(imgList.get(i).getImg(), path+imgList.get(i).getImgName());
			flag = generateImage;
		}
		
		String replacePath = replacePath(jsonStrImg, imgList,host);
		return replacePath;
	}
	
	/**
	 * 对base64编码字符串替换成图片路径
	 * @param jsonStr
	 * @param imgList
	 * @return
	 */
	public static String replacePath(String jsonStr,List<Image> imgList,String host) {
		int i = 0;
		//正则表达式获取字符串中的base64编码
		StringBuffer operatorStr=new StringBuffer(jsonStr);  
		Pattern pattern = Pattern.compile("data:image.*?data-filename=.*?((png)|(jpg))") ; 
		Matcher matcher = pattern.matcher(operatorStr) ; 
		//对base64编码字符串替换成图片路径
		while(matcher.find()){
			operatorStr.replace(matcher.start(), matcher.end(), host+imgList.get(i).getImgName());
			matcher = pattern.matcher(operatorStr);  //更新Matcher对象，指向新字符串的匹配
			i++;
        }
		return operatorStr.toString();
		
	}
	
}
