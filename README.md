# dmind

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


### 3. 准备邮箱
用于发送生成器思维导图xmind文件。

### 4.构建打包
获取项目 `git clone`
修改配置 `application.properties` `static/js/upload.js`
打包`mvn clean package`

### 5.启动
启动 百度LAC 分词服务 （python）
进入py文件夹,
可以通过修改dict.txt,添加自定义分词 比如：我的兄弟 这样当遇到[我的兄弟]是，就会当作一个完整的词，而不是 [我、的、兄弟]
执行`./run.sh`

启动Web 数据处理服务（Java）可根据实际情况调整内存大小

`nohup java -Xmx1024m -jar target/dmind-0.0.1-jar-with-dependencies.jar >> nohup.log 2>&1 &`
