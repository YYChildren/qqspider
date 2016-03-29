**QQ空间爬虫**
---
分布式QQ空间爬虫

### 已实现
* 爬取QQ号
* 爬取关系链
* 将QQ号和关系链存入MySQL
* 可使用jmx监控，端口为50013，可以到spiderctl 中修改

### 部署注意
* 可修改Makefile中TARGET_PATH （部署目标路径）
* 需要将config下的 *.template 文件去除 .template扩展名，并配置成你所需
* 需要建表，建表语句在sql目录下

### 部署
* make clean
* make

### 运行
* 切换到Makefile中目标目录
* 启动：./spiderctl start
* 关闭：./spiderctl stop
* 配置文件在 目标目录/config 下

### TODO
* 爬取说说，存入MongoDB

