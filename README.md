# App后台 Demo

返回值统一格式：
    
    {
      "code":"200",             // 200 代表接口请求成功
      "msg":"",                 // 错误信息，请求成功无此字段
      "data": {}                // JSON格式业务数据，每个接口返回数据格式请参考以下文档
    }

使用前需要调用接口初始化 App 信息，具体方式请查看 AppController 类

## DemoController 提供如下接口供商户进行测试

### 1. 登录

> GET /user/login/{token}

返回data:

    {
      "auth":"true",              // true/false 是否有查看此用户信息权限
      "pubInfoAuth":"true",       // true/false 是否有查看用户公开信息权限
      "firstName":"John",         // 如果 pubInfoAuth 为 false，此字段不返回
      "lastName":"Due",           // 如果 pubInfoAuth 为 false，此字段不返回
      "selfieUrl":"http://..."    // 用户头像链接，如果 pubInfoAuth 为 false 或用户无头像，此字段不返回
      "isKycPass":"true",         // 如果 pubInfoAuth 为 false，此字段不返回，true/false 用户是否进行了实名认证
      "phoneAuth":"true",         // true/false 是否有查看用户手机号
      "phone":"86-13300000000",   // 如果 phoneAuth 为 false，此字段不返回
    }

### 2. 用户授权

> GET /user/auth/{biyToken}

返回data: 与接口 `1.登录` 相同

