# Databend Operator

使用 提供 Databend 的云原生 Operator 服务

## CRD 生成

使用 fabric8 [CRD generation](https://github.com/fabric8io/kubernetes-client/blob/main/doc/CRD-generator.md) 功能，可以提供 CRD 的初始化 Yaml。

```shell
# 构建编译
mvn clean install
# 编译后，可以在如下目录查看生成的 CRD 文件
ls target/classes/META-INF/fabric8/
databendclusters.databend.datafuselabs.com-v1.yml  databendclusters.databend.datafuselabs.com-v1beta1.yml
```

