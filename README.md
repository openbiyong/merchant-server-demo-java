# 商户对接 Demo

## Quick Start

#### 1. 此Demo基于 Java 8 开发，使用 maven 管理依赖包

打包命令

    mvn clean install -Dmaven.test.skip=true

#### 2. 启动应用

    java -jar merchant-server-demo-1.1.0.jar


#### 3. 使用前需要调用接口初始化 App 信息。以下是使用 curl 命令在本地初始化应用。参见`AppController`。

    curl http://127.0.0.1:25000/app/setup -X POST -H 'Content-Type: application/json' -d '
    {
      "appId":"填写你的appId",
      "privateKey":"填写你的私钥",
      "biyongPublicKey":"填写BiYong为你的App提供的公钥",
      "apiUrl":"填写BiYong后台访问url",
      "serverUrl":"填写本服务的访问url"
    }'
    
#### 4. 启动后可直接访问测试页面。参见`WebController`。

授权测试页面

    http://127.0.0.1:25000/page/auth

支付测试页面
    
    http://127.0.0.1:25000/page/pay
