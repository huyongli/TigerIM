### java.lang.VerifyError: org/jivesoftware/smack/sasl/javax/SASLJavaXMechanism
在初始化`XMPPTCPConnectionConfiguration.builder()`的时候出现上述异常，移除smack-java7相关jar包即可

### org.jivesoftware.smack.smackexception$noresponseexception no response received within reply timeout
登陆的时候出现这个异常信息，一直以为是代码出现了问题，然后各种google，结果依然无效，最后发现将手机的网络代理去掉就ok了，我擦嘞～～～