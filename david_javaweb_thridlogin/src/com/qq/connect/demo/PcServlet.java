package com.qq.connect.demo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.david.webtools.common.base.BaseServlet;
import com.qq.connect.oauth.Oauth;

/**
 * 直接网站自己拼接参数调用qq互联登陆。官方参考地址：http://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token
 * @author ：David
 * @weibo ：http://weibo.com/mcxiaobing
 * @github: https://github.com/QQ986945193
 * 
 * 不使用腾讯sdk,直接请求url接口。演示
 * 
 * 继承BaseServlet。请求带有method参数即可。
 */
public class PcServlet extends BaseServlet{
	
	
	/**
	 * Step1：获取Authorization Code

		请求地址：
		PC网站：https://graph.qq.com/oauth2.0/authorize
		请求方法：
		GET
		请求参数：
		请求参数请包含如下内容：
		参数	是否必须	含义
		response_type	必须	授权类型，此值固定为“code”。
		client_id	必须	申请QQ登录成功后，分配给应用的appid。
		redirect_uri	必须	成功授权后的回调地址，必须是注册appid时填写的主域名下的地址，建议设置为网站首页或网站的用户中心。注意需要将url进行URLEncode。
		state	必须	client端的状态值。用于第三方应用防止CSRF攻击，成功授权后回调时会原样带回。请务必严格按照流程检查用户与state参数状态的绑定。
		scope	可选	请求用户授权时向用户显示的可进行授权的列表。
		可填写的值是API文档中列出的接口，以及一些动作型的授权（目前仅有：do_like），如果要填写多个接口名称，请用逗号隔开。
		例如：scope=get_user_info,list_album,upload_pic,do_like
		不传则默认请求对接口get_user_info进行授权。
		建议控制授权项的数量，只传入必要的接口名称，因为授权项越多，用户越可能拒绝进行任何授权。
		display	可选	仅PC网站接入时使用。
		用于展示的样式。不传则默认展示为PC下的样式。
		如果传入“mobile”，则展示为mobile端下的样式。
		g_ut	可选	仅WAP网站接入时使用。
		QQ登录页面版本（1：wml版本； 2：xhtml版本），默认值为1。
		 
		返回说明：
		1. 如果用户成功登录并授权，则会跳转到指定的回调地址，并在redirect_uri地址后带上Authorization Code和原始的state值。如：
		PC网站：http://graph.qq.com/demo/index.jsp?code=9A5F************************06AF&state=test
		WAP网站：http://open.z.qq.com/demo/index.jsp?code=9A5F************************06AF&state=test
		注意：此code会在10分钟内过期。
		2. 如果用户在登录授权过程中取消登录流程，对于PC网站，登录页面直接关闭；对于WAP网站，同样跳转回指定的回调地址，并在redirect_uri地址后带上usercancel参数和原始的state值，其中usercancel值为非零，如：
		http://open.z.qq.com/demo/index.jsp?usercancel=1&state=test
		错误码说明：
		接口调用有错误时，会返回code和msg字段，以url参数对的形式返回，value部分会进行url编码（UTF-8）。
		PC网站接入时，错误码详细信息请参见：100000-100031：PC网站接入时的公共返回码。
		WAP网站接入时，错误码详细信息请参见：6000-6999：获取Authorization Code时，发生错误。
		
		Step2：通过Authorization Code获取Access Token
		
		请求地址：
		PC网站：https://graph.qq.com/oauth2.0/token
		WAP网站：https://graph.z.qq.com/moc2/token
		请求方法：
		GET
		请求参数：
		请求参数请包含如下内容：
		参数	是否必须	含义
		grant_type	必须	授权类型，在本步骤中，此值为“authorization_code”。
		client_id	必须	申请QQ登录成功后，分配给网站的appid。
		client_secret	必须	申请QQ登录成功后，分配给网站的appkey。
		code	必须	上一步返回的authorization code。
		如果用户成功登录并授权，则会跳转到指定的回调地址，并在URL中带上Authorization Code。
		例如，回调地址为www.qq.com/my.php，则跳转到：
		http://www.qq.com/my.php?code=520DD95263C1CFEA087******
		注意此code会在10分钟内过期。
		redirect_uri	必须	与上面一步中传入的redirect_uri保持一致。
		 
		返回说明：
		如果成功返回，即可在返回包中获取到Access Token。 如：
		access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14
		参数说明	描述
		access_token	授权令牌，Access_Token。
		expires_in	该access token的有效期，单位为秒。
		refresh_token	在授权自动续期步骤中，获取新的Access_Token时需要提供的参数。
		 
		错误码说明：
		接口调用有错误时，会返回code和msg字段，以url参数对的形式返回，value部分会进行url编码（UTF-8）。
		PC网站接入时，错误码详细信息请参见：100000-100031：PC网站接入时的公共返回码。
		WAP网站接入时，错误码详细信息请参见：7000-7999：通过Authorization Code获取Access Token时，发生错误。
		 
		Step3：（可选）权限自动续期，获取Access Token
		
		Access_Token的有效期默认是3个月，过期后需要用户重新授权才能获得新的Access_Token。本步骤可以实现授权自动续期，避免要求用户再次授权的操作，提升用户体验。
		请求地址：
		PC网站：https://graph.qq.com/oauth2.0/token
		WAP网站：https://graph.z.qq.com/moc2/token
		请求方法：
		GET
		请求参数：
		请求参数请包含如下内容：
		参数	是否必须	含义
		grant_type	必须	授权类型，在本步骤中，此值为“refresh_token”。
		client_id	必须	申请QQ登录成功后，分配给网站的appid。
		client_secret	必须	申请QQ登录成功后，分配给网站的appkey。
		refresh_token	必须	在Step2中，返回的refres_token。
		 
		返回说明：
		如果成功返回，即可在返回包中获取到Access Token。 如：
		access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14。
		 
		参数说明	描述
		access_token	授权令牌，Access_Token。
		expires_in	该access token的有效期，单位为秒。
		refresh_token	在授权自动续期步骤中，获取新的Access_Token时需要提供的参数。
		 
		错误码说明：
		接口调用有错误时，会返回code和msg字段，以url参数对的形式返回，value部分会进行url编码（UTF-8）。
		PC网站接入时，错误码详细信息请参见：100000-100031：PC网站接入时的公共返回码。
		WAP网站接入时，错误码详细信息请参见：7000-7999：通过Authorization Code获取Access Token时，发生错误。
	 */
	
	
	/**
	 * 获取Authorization Code
	 * 
	 * 浏览器请求地址：localhost/david_javaweb_thridlogin/pcServlet.do?method=getAuthorizationCode 
	 * 
	 * 返回结果是：http://localhost/david_javaweb_thridlogin/afterlogin.do?code=822E84140650251CA633C98D40956C9A&state=david
	 */
	public String getAuthorizationCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.sendRedirect("https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=1206673311&redirect_uri=http://localhost/david_javaweb_thridlogin/afterlogin.do&state=david");
		return null;
	}
	
	/**
	 * 通过Authorization Code获取Access Token
	 * 返回结果是：access_token=8230D1A1313383BA5013CAF25D8EF463&expires_in=70006000&refresh_token=63ABF695AB4CC9BB9D8898C342750EB5
	 */
	public String getAccessToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.sendRedirect("https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=1206673311&client_secret=EVGCsVmXxkA8SP1n&code=802E84140650251CA633C98D40956C9A&redirect_uri=http://localhost/david_javaweb_thridlogin/afterlogin.do");
		return null;
	}
	
	
	
	/**
	 * 1 请求地址

		PC网站：https://graph.qq.com/oauth2.0/me
		WAP网站：https://graph.z.qq.com/moc2/me
		2 请求方法
		
		GET
		3 请求参数
		
		请求参数请包含如下内容：
		参数	是否必须	含义
		access_token	必须	在Step1中获取到的access token。
		 
		4 返回说明
		
		PC网站接入时，获取到用户OpenID，返回包如下：
		1
		callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
		WAP网站接入时，返回如下字符串：
		client_id=100222222&openid=1704************************878C
		openid是此网站上唯一对应用户身份的标识，网站可将此ID进行存储便于用户下次登录时辨识其身份，或将其与用户在网站上的原有账号进行绑定。
	 */
	
	
	/**
	 * 获取用户OpenID,需要上面请求得到的openid值。
	 * 返回结果是(json字符串)：{
				    "client_id": "1206673311",
				    "openid": "666CAC7A8850121FD2976390EBCE7161"
				}
	 */
	public String getOpenId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.sendRedirect("https://graph.qq.com/oauth2.0/me?access_token=8230D1A1313383BA5013CAF25D8EF463");
		return null;
	}
}
