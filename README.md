
这是一个java后端实现QQ、微博、微信的第三方登陆案例。可以直接拿到项目中直接使用。
### 基本介绍
	1，QQ和微博登录官方都有提供sdk,所以使用了sdk做登陆，获取唯一标示token。
	2，直接使用的servlet最基本的javaweb基础知识实现，所以简单易懂。
	3，为了不引入qq的jar文件和微博的类，所以不用官方提供的sdk也实现了登陆并获取token的功能。
	4，总结：里面有微博和qq登陆两种方式。微信没有sdk，直接利用url和接口实现了登陆。获取唯一标识id.
	5，微博和qq两种登录实现方式，我都是亲自测过的，没有任何问题。
	6，备注：由于没有微信账号，所以微信登录我没有测试，由于做法都差不多，所以如有问题，请自行仔细排查。
	
### 项目结构介绍以及使用注意事项
![项目架构](https://mcxiaobing.gitee.io/blog/img/github/david_javaweb_thridlogin/1.png)

	1，可以看出应该很明确，微博登录类是：WeiboServlet,QQ是IndexServlet和PcServlet。微信是WeChatServlet
	2，由于里面代码很详细，具体我就不在陈述了，请参考里面的代码。里面注释和官方请求参数地址写的很清楚。
	3，注意：如果使用微博和QQ的sdk中Oauth登陆，记得修改config.properties和qqconnectconfig.properties中的文件。
### 最后再给大家贴一个微博登录的实现代码。
![微博登录的实现代码](https://mcxiaobing.gitee.io/blog/img/github/david_javaweb_thridlogin/2.png)

### 所以每个类写的都是很详细的。当然如果你有疑问，可以联系我。谢谢。



	部分资源来自互联网收集，并修改，归原作者所有，感谢。
	
	当然如果您感觉我侵犯了您的权益，请私信，我会立刻删除相关内容。
	
	项目虽小，但是开源不易，请尊重作者，转载请说明出处。谢谢。

[点击查看更多开源项目](https://gitee.com/mcxiaobing)

[欢迎关注本人微博](http://weibo.com/mcxiaobing )
	
	