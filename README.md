# dmind

首页
![首页](https://d-pic.oss-cn-shanghai.aliyuncs.com/dmind/home.jpg)
---

提交信息
![提交信息](https://d-pic.oss-cn-shanghai.aliyuncs.com/dmind/parse.jpg)
---

文件上传
![文件上传](https://d-pic.oss-cn-shanghai.aliyuncs.com/dmind/oss-file.jpg)
---

邮件发送
![邮件发送](https://d-pic.oss-cn-shanghai.aliyuncs.com/dmind/mail-detail.jpg)
---

本地文件
![本地文件](https://d-pic.oss-cn-shanghai.aliyuncs.com/dmind/output.jpg)
---

效果
![效果](https://d-pic.oss-cn-shanghai.aliyuncs.com/dmind/xmind.jpg)
---


### 1.安装Baidu LAC分词
LAC全称Lexical Analysis of Chinese，是百度自然语言处理部研发的一款联合的词法分析工具，实现中文分词、词性标注、专名识别等功能。

举例：
LAC是个优秀的分词工具 -> [LAC, 是, 个, 优秀, 的, 分词, 工具]

项目主页：`https://github.com/baidu/lac`

安装：
`pip install lac`
国内用户可以使用百度源安装，安装速率更快
`pip install lac -i https://mirror.baidu.com/pypi/simple`

### 2.开通OSS对象存储服务
通过OSS服务，安全快速的上传要处理的关键词数据。我这里使用的是阿里云OSS服务，有需要可以自己更换储存服务。

服务介绍`https://www.aliyun.com/product/oss`

创建Bucket，可以理解为电脑C盘 D盘
配置权限，个人数据，选择私有。
添加RAM角色。获取AccessKeyId / AccessKeySecret

![开通OSS](https://d-pic.oss-cn-shanghai.aliyuncs.com/dmind/oss-home.jpg)
![创建Bucket](https://d-pic.oss-cn-shanghai.aliyuncs.com/dmind/oss-b-add.jpg)
![创建Bucket](https://d-pic.oss-cn-shanghai.aliyuncs.com/dmind/oss-bucket-list.jpg)
![设置权限](https://d-pic.oss-cn-shanghai.aliyuncs.com/dmind/oss-policy.jpg)
![获取AccessKey](https://d-pic.oss-cn-shanghai.aliyuncs.com/dmind/access.jpg)


### 3. 准备邮箱
用于发送生成器思维导图xmind文件。
![获取AccessKey](https://d-pic.oss-cn-shanghai.aliyuncs.com/dmind/mail.jpg)

### 4.构建打包
获取项目 `git clone https://github.com/weidongdu/dmind.git`
![项目结构](https://d-pic.oss-cn-shanghai.aliyuncs.com/dmind/tree.jpg)
进入项目目录  `cd dmind`
修改配置 `application.properties` `static/js/upload.js`
指定邮箱，设置oss秘钥

打包`mvn clean package`

### 5.启动
启动 百度LAC 分词服务 （python）
进入py文件夹,
可以通过修改dict.txt,添加自定义分词 比如：我的兄弟 这样当遇到[我的兄弟]是，就会当作一个完整的词，而不是 [我、的、兄弟]
执行`./run.sh`

启动Web 数据处理服务（Java）可根据实际情况调整内存大小

`nohup java -Xmx1024m -jar target/dmind-0.0.1-jar-with-dependencies.jar >> nohup.log 2>&1 &`
