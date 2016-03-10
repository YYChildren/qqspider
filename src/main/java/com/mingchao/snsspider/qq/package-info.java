/**
 * 
 */
/**
 * @author Administrator
 * TODO
 * 1 提取参数
 *     Hadoop 文件路径/应用目录  或者  数据库账号与密码
 *     WebDriverPool 池大小
 *     BaseTaskExcutor 线程池大小
 *     Cookie持久化路径（可选）
 *     布隆过滤器预期数据量 预期错误率
 *     布隆过滤器持久化路径
 *     
 *     应用登录账号与密码 
 *     
 * 2 driver崩溃处理 √
 * 3 driver 超时处理（当页面一直在旋时，不返回，也不超时, 需要刷新） √
 * 4 转换成分布式
 * 5 判断状态优化 √
 * 6 所有core组件都集中到Resource，Task里不再放有core组件 √
 * 7 存储文件定时更新 （不采用）
 * 8 说说没有内容延时较大
 * 9 说说下一页优化 √
 * 10 Chrome不加载图片 (不采用)
 * 11 Schedule存储 √
 * 		可将UserKey 队列和RelaKey 队列先存到数据库，然后Schedule每次批量取 
 * 12 数据库的队列为空问题
 * 13 BloomFilter 存储到文件
 * 14 登录任务分离到单独的线程
 */
package com.mingchao.snsspider.qq;