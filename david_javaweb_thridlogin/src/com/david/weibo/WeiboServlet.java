package com.david.weibo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.tomcat.util.codec.binary.StringUtils;

import com.david.utils.HttpUrlconnectionUtils;
import com.david.utils.HttpUrlconnectionUtils.CallBack;
import com.david.webtools.common.base.BaseServlet;
import com.david.weibo.weibo4j.Oauth;
import com.david.weibo.weibo4j.Users;
import com.david.weibo.weibo4j.http.AccessToken;
import com.david.weibo.weibo4j.model.User;
import com.david.weibo.weibo4j.model.WeiboException;
import com.david.weibo.weibo4j.util.BareBonesBrowserLaunch;
import com.david.weibo.weibo4j.util.WeiboConfig;

/**
 * 微博
 * 
 * @author ：David
 * @weibo ：http://weibo.com/mcxiaobing
 * @github: https://github.com/QQ986945193
 */
public class WeiboServlet extends BaseServlet {
	
	/**
	 * 微博官方。回调的接口地址。http://localhost/david_javaweb_thridlogin/weiboServlet.do?method=weiboRedirect 
	 * 返回结果会带有一个参数code。样式为：
	 * http://localhost/david_javaweb_thridlogin/weiboServlet.do?method=weiboRedirect&code=b30deda44d65a3aa7c28b218d9fc371b
	 */
	public String weiboRedirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.err.println("微博回调了此接口");
		if (request.getParameter("code")!=null&&!request.getParameter("code").equals("")) {
			// 这里则获取到了登陆成功后的结果code.
			// 通过code 获取到微博的唯一标识access_token
			Oauth oauth = new Oauth();
			AccessToken token = oauth.getAccessTokenByCode(request.getParameter("code"));
			// 获取到微博的唯一标识access_token 
			System.out.println(token.getAccessToken());
			// 获取成功，这里直接转发到登陆成功的jsp
			return "f:/weibo/loginsuccess.jsp";
		}
		return null;
	}
	/**
	 * 微博登陆的接口地址。http://localhost/david_javaweb_thridlogin/weiboServlet.do?method=weiboLogin
	 * 
	 */
	public String weiboLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Oauth oauth = new Oauth();
//		重定向登陆的接口地址
		response.sendRedirect(new Oauth().authorize("code"));
//		BareBonesBrowserLaunch.openURL(oauth.authorize("code"));
//		System.out.println(oauth.authorize("code"));
//		System.out.print("Hit enter when it's done.[Enter]:");
		// 由于只是调取登陆，所以下面就不执行了，这里暂时注释掉。
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		String code = br.readLine();
//		System.out.println("code: " + code);
//		try{
//			System.out.println(oauth.getAccessTokenByCode(code));
//		} catch (WeiboException e) {
//			if(401 == e.getStatusCode()){
//				System.out.println("Unable to get the access token.");
//			}else{
//				e.printStackTrace();
//			}
//		}
		
		return null;
	}
	
	/**
	 * 微博登录，不使用sdkOauth
	 */
	public String weiboLoginNoOauth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 重定向指定参数匹配的url中	https://api.weibo.com/oauth2/authorize?client_id=3921596734&redirect_uri=http://localhost/david_javaweb_thridlogin/weiboServlet.do?method=weiboRedirect&response_type=code
		String url = "	https://api.weibo.com/oauth2/authorize?client_id=3922396734&redirect_uri=http://localhost/david_javaweb_thridlogin/weiboServlet.do?method=weiboRedirectNoOauth&response_type=code";
		response.sendRedirect(url);
		return null;
	}
	/**
	 * 微博官方。回调的接口地址。不使用sdkOauth
	 */
	public String weiboRedirectNoOauth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.err.println("微博回调了此接口");
		if (request.getParameter("code")!=null&&!request.getParameter("code").equals("")) {
			// 这里则获取到了登陆成功后的结果code.
			// 通过code 获取到微博的唯一标识access_token
			String getTokenUrl = "https://api.weibo.com/oauth2/access_token";
			String params = "client_id=3121596734&client_secret=2fffdc02298fa15db399e5f8c396c373&grant_type=authorization_code&code="+request.getParameter("code")+"&redirect_uri=http://localhost/david_javaweb_thridlogin/weiboServlet.do?method=weiboRedirectNoOauth";
//			System.out.println(params);
			HttpUrlconnectionUtils.doPostAsyn(getTokenUrl, params, new CallBack() {
				
				@Override
				public void onRequestComplete(String result) {
					// 得到json字符串 {"access_token":"2.00bJkZffO9c5RE0c34dd8074FvzXsC","remind_in":"157679999","expires_in":157679999,"uid":"24613911675","isRealName":"true"}
					System.out.println(result);
					
				}
			});
			// 获取成功，这里直接转发到登陆成功的jsp
			return "f:/weibo/loginsuccess.jsp";
		}
		return null;
	}
	
	public static void main(String[] args) {
		String access_token = WeiboConfig.getValue("access_token");
		String uid = args[0];
		Users um = new Users(access_token);
		try {
			User user = um.showUserById(uid);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
	}
}
