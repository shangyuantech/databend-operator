# Databend Operator

ʹ�� �ṩ Databend ����ԭ�� Operator ����

## CRD ����

ʹ�� fabric8 [CRD generation](https://github.com/fabric8io/kubernetes-client/blob/main/doc/CRD-generator.md) ���ܣ������ṩ CRD �ĳ�ʼ�� Yaml��

```shell
# ��������
mvn clean install
# ����󣬿���������Ŀ¼�鿴���ɵ� CRD �ļ�
ls target/classes/META-INF/fabric8/
databendclusters.databend.datafuselabs.com-v1.yml  databendclusters.databend.datafuselabs.com-v1beta1.yml
```

