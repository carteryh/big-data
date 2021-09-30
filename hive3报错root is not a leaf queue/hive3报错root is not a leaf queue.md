# Failed to submit application_1618751895029_0003 to YARN : root is not a leaf queue

​		hive执行数据插入报错,执行sql如下：

```sql
insert into table dw_weblog_detail partition(datestr='20181101')
select c.valid,c.remote_addr,c.remote_user,c.time_local,
substring(c.time_local,0,10) as daystr,
substring(c.time_local,12) as tmstr,
substring(c.time_local,6,2) as month,
substring(c.time_local,9,2) as day,
substring(c.time_local,12,2) as hour,
c.request,c.status,c.body_bytes_sent,c.http_referer,c.ref_host,c.ref_path,c.ref_query,c.ref_query_id,c.http_user_agent
from
(SELECT 
a.valid,a.remote_addr,a.remote_user,a.time_local,
a.request,a.status,a.body_bytes_sent,a.http_referer,a.http_user_agent,b.ref_host,b.ref_path,b.ref_query,b.ref_query_id 
FROM ods_weblog_origin a LATERAL VIEW parse_url_tuple(regexp_replace(http_referer, "\"", ""), 'HOST', 'PATH','QUERY', 'QUERY:id') b as ref_host, ref_path, ref_query, ref_query_id) c;
```

​		报错信息:

```
Query ID = root_20210418213139_023852ed-4af2-45d6-aacc-54faab5f78b0
Total jobs = 3
Launching Job 1 out of 3
Number of reduce tasks not specified. Estimated from input data size: 1
In order to change the average load for a reducer (in bytes):
  set hive.exec.reducers.bytes.per.reducer=<number>
In order to limit the maximum number of reducers:
  set hive.exec.reducers.max=<number>
In order to set a constant number of reducers:
  set mapreduce.job.reduces=<number>
java.io.IOException: org.apache.hadoop.yarn.exceptions.YarnException: Failed to submit application_1618751895029_0003 to YARN : root is not a leaf queue
        at org.apache.hadoop.mapred.YARNRunner.submitJob(YARNRunner.java:346)
        at org.apache.hadoop.mapreduce.JobSubmitter.submitJobInternal(JobSubmitter.java:251)
        at org.apache.hadoop.mapreduce.Job$11.run(Job.java:1576)
        at org.apache.hadoop.mapreduce.Job$11.run(Job.java:1573)
        at java.security.AccessController.doPrivileged(Native Method)
        at javax.security.auth.Subject.doAs(Subject.java:422)
        at org.apache.hadoop.security.UserGroupInformation.doAs(UserGroupInformation.java:1845)
        at org.apache.hadoop.mapreduce.Job.submit(Job.java:1573)
        at org.apache.hadoop.mapred.JobClient$1.run(JobClient.java:576)
        at org.apache.hadoop.mapred.JobClient$1.run(JobClient.java:571)
        at java.security.AccessController.doPrivileged(Native Method)
        at javax.security.auth.Subject.doAs(Subject.java:422)
        at org.apache.hadoop.security.UserGroupInformation.doAs(UserGroupInformation.java:1845)
        at org.apache.hadoop.mapred.JobClient.submitJobInternal(JobClient.java:571)
        at org.apache.hadoop.mapred.JobClient.submitJob(JobClient.java:562)
        at org.apache.hadoop.hive.ql.exec.mr.ExecDriver.execute(ExecDriver.java:423)
        at org.apache.hadoop.hive.ql.exec.mr.MapRedTask.execute(MapRedTask.java:149)
        at org.apache.hadoop.hive.ql.exec.Task.executeTask(Task.java:205)
        at org.apache.hadoop.hive.ql.exec.TaskRunner.runSequential(TaskRunner.java:97)
        at org.apache.hadoop.hive.ql.Driver.launchTask(Driver.java:2664)
        at org.apache.hadoop.hive.ql.Driver.execute(Driver.java:2335)
        at org.apache.hadoop.hive.ql.Driver.runInternal(Driver.java:2011)
        at org.apache.hadoop.hive.ql.Driver.run(Driver.java:1709)
        at org.apache.hadoop.hive.ql.Driver.run(Driver.java:1703)
        at org.apache.hadoop.hive.ql.reexec.ReExecDriver.run(ReExecDriver.java:157)
        at org.apache.hadoop.hive.ql.reexec.ReExecDriver.run(ReExecDriver.java:218)
        at org.apache.hadoop.hive.cli.CliDriver.processLocalCmd(CliDriver.java:239)
        at org.apache.hadoop.hive.cli.CliDriver.processCmd(CliDriver.java:188)
        at org.apache.hadoop.hive.cli.CliDriver.processLine(CliDriver.java:402)
        at org.apache.hadoop.hive.cli.CliDriver.executeDriver(CliDriver.java:821)
        at org.apache.hadoop.hive.cli.CliDriver.run(CliDriver.java:759)
        at org.apache.hadoop.hive.cli.CliDriver.main(CliDriver.java:683)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at org.apache.hadoop.util.RunJar.run(RunJar.java:323)
        at org.apache.hadoop.util.RunJar.main(RunJar.java:236)
Caused by: org.apache.hadoop.yarn.exceptions.YarnException: Failed to submit application_1618751895029_0003 to YARN : root is not a leaf queue
        at org.apache.hadoop.yarn.client.api.impl.YarnClientImpl.submitApplication(YarnClientImpl.java:336)
        at org.apache.hadoop.mapred.ResourceMgrDelegate.submitApplication(ResourceMgrDelegate.java:304)
        at org.apache.hadoop.mapred.YARNRunner.submitJob(YARNRunner.java:331)
        ... 37 more
Job Submission failed with exception 'java.io.IOException(org.apache.hadoop.yarn.exceptions.YarnException: Failed to submit application_1618751895029_0003 to YARN : root is not a leaf queue)'
FAILED: Execution Error, return code 1 from org.apache.hadoop.hive.ql.exec.mr.MapRedTask. org.apache.hadoop.yarn.exceptions.YarnException: Failed to submit application_1618751895029_0003 to YARN : root is not a leaf queue
```

解决方案，在hive命令窗口执行以下命令:

```
set mapred.job.queue.name=root.root;
```

具体报错图片日志地址:

https://github.com/carteryh/big-data/tree/main/hive3%E6%8A%A5%E9%94%99root%20is%20not%20a%20leaf%20queue

相关大数据学习demo地址:

https://github.com/carteryh/big-data

