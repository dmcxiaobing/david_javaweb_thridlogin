package com.david.wechat;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.david.utils.HttpUrlconnectionUtils;
import com.david.utils.HttpUrlconnectionUtils.CallBack;
import com.david.webtools.common.base.BaseServlet;
import com.david.weibo.weibo4j.Oauth;
import com.david.weibo.weibo4j.http.AccessToken;

/**
 * 微信登陆。官方文档：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419316505&token=8274539c2521f41171079beb28c31b53b65e6e60&lang=zh_CN
 * @author ：David
 * @weibo ：http://weibo.com/mcxiaobing
 * @github: https://github.com/QQ986945193
 */

public class WechatServlet extends BaseServlet{

	private String appId = "";
	private String redirect_uri = "";
	private String response_type = "code";
	private String scope = "snsapi_login";
	private String state = "state";
	
	
	/**
	 * 微信登录接口地址，重定向登陆页面
	 * http://localhost/david_javaweb_thridlogin/wechatServlet.do?method=wechatLogin
	 */
	public String wechatLogin(HttpServletRequest request, HttpServletResponse response)throws Exception {
		// 拼接地址，然后重定向到登陆的页面
//		urlEncode对链接进行处理
		redirect_uri = URLEncoder.encode(redirect_uri,"utf-8");
		String wechatUrl = "https://open.weixin.qq.com/connect/qrconnect?appid="
				+appId+"&redirect_uri="+redirect_uri
				+"&response_type="+response_type+"&scope="+
				scope+"&state="+state;
		System.out.println(wechatUrl);
		response.sendRedirect(wechatUrl);
		return null;
		
	}
	
	/**
	 * 微信官方。回调的接口地址。http://localhost/david_javaweb_thridlogin/wechatServlet.do?method=wechatRedirect
	 * 返回结果会带有一个参数code。
	 * 
	 * 用户允许授权后，将会重定向到redirect_uri的网址上，并且带上code和state参数
		redirect_uri?code=CODE&state=STATE
		若用户禁止授权，则重定向后不会带上code参数，仅会带上state参数
		redirect_uri?state=STATE
	 */
	public String wechatRedirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.err.println("微信回调了此接口");
		if (request.getParameter("code")!=null&&!request.getParameter("code").equals("")) {
			// 这里则获取到了登陆成功后的结果code.
			// 通过code 获取到微信的唯一标识access_token
			String getTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?";
			String params = "appid=3121596734&secret=2fffdc02298fa15db399e5f8c396c373&code="+request.getParameter("code")+"&grant_type=authorization_code";
			getTokenUrl = getTokenUrl+params;
			HttpUrlconnectionUtils.doGetAsyn(getTokenUrl, new CallBack() {
				
				@Override
				public void onRequestComplete(String result) {
					/**
					 * v正确的返回：
						{"access_token":"ACCESS_TOKEN", 
						"expires_in":7200, 
						"refresh_token":"REFRESH_TOKEN",
						"openid":"OPENID", 
						"scope":"SCOPE",
						"unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
						}
						参数说明
						参数	说明
						access_token	接口调用凭证
						expires_in	access_token接口调用凭证超时时间，单位（秒）
						refresh_token	用户刷新access_token
						openid	授权用户唯一标识
						scope	用户授权的作用域，使用逗号（,）分隔
						unionid	当且仅当该网站应用已获得该用户的userinfo授权时，才会出现该字段。
					 */
					System.out.println(result);
				}
			});
			// 获取成功，这里直接转发到登陆成功的jsp
			return "f:/wechat/loginsuccess.jsp";
		}
		return null;
	}
	
}
