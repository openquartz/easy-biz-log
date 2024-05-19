# Springboot-Starter-注解-通用操作日志组件

本项目基于项目 [`mzt-biz-log`](https://github.com/mouzt/mzt-biz-log) 做了一些定制改造优化。\
1、更加适配多种定制存储starter包的扩展，例如：MySql、MongoDB、ElasticSearch等。\
2、使用[`java-obj-diff`](https://github.com/openquartz/java-obj-diff)项目做对象差异比对工具。支持自定义对象解析,自定义类型比较器,差异结果脱敏等特性。

> **注意**：项目未发布到maven中央仓库，需要手动添加到本地仓库 或者 到私有仓库中使用。

## 快速入门

### 基本使用

#### maven依赖添加对应的starter依赖

##### 使用jdbc-mysql存储时

```xml

<dependency>
    <groupId>com.openquartz</groupId>
    <artifactId>easybizlog-spring-boo-starter-jdbc</artifactId>
    <version>1.0.0</version>
</dependency>
```

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
    <version>2.3.2.RELEASE</version>
</dependency>
```

##### 使用elastic-search存储时

```xml

<dependency>
    <groupId>com.openquartz</groupId>
    <artifactId>easybizlog-spring-boo-starter-es</artifactId>
    <version>1.1.0</version>
</dependency>
```

```xml

<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>7.6.2</version>
</dependency>
```

##### 使用mongodb存储时

```xml

<dependency>
    <groupId>com.openquartz</groupId>
    <artifactId>easybizlog-spring-boo-starter-mongodb</artifactId>
    <version>1.2.0</version>
</dependency>
```

```xml

<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>4.2.3</version>
</dependency>
```

#### 日志埋点

###### 1. 普通的记录日志

* type：是拼接在 bizNo 上作为 log 的一个标识。避免 bizNo 都为整数 ID 的时候和其他的业务中的 ID 重复。比如订单 ID、用户 ID
  等，type可以是订单或者用户
* bizNo：就是业务的 ID，比如订单ID，我们查询的时候可以根据 bizNo 查询和它相关的操作日志
* success：方法调用成功后把 success 记录在日志的内容中
* SpEL 表达式：其中用双大括号包围起来的（例如：{{#order.purchaseName}}）#order.purchaseName 是
  SpEL表达式。Spring中支持的它都支持的。比如调用静态方法，三目表达式。SpEL 可以使用方法中的任何参数

```
    @LogRecord(
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,测试变量「{{#innerOrder.productName}}」,下单结果:{{#_ret}}",
            type = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    public boolean createOrder(Order order) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());
        // db insert order
        Order order1 = new Order();
        order1.setProductName("内部变量测试");
        LogRecordContext.putVariable("innerOrder", order1);
        return true;
    }
```

此时会打印操作日志 "张三下了一个订单,购买商品「超值优惠红烧肉套餐」,测试变量「内部变量测试」,下单结果:true"

###### 2. 期望记录失败的日志, 如果抛出异常则记录fail的日志，没有抛出记录 success 的日志。从 1.1.0-SNAPSHOT 版本开始，在LogRecord实体中添加了 fail 标志，可以通过这个标志区分方法是否执行成功了

```
    @LogRecord(
            fail = "创建订单失败，失败原因：「{{#_errorMsg}}」",
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,测试变量「{{#innerOrder.productName}}」,下单结果:{{#_ret}}",
            type = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    public boolean createOrder(Order order) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());
        // db insert order
        Order order1 = new Order();
        order1.setProductName("内部变量测试");
        LogRecordContext.putVariable("innerOrder", order1);
        return true;
    }
```

其中的 #_errorMsg 是取的方法抛出异常后的异常的 errorMessage。

###### 3. 日志支持子类型

比如一个订单的操作日志，有些操作日志是用户自己操作的，有些操作是系统运营人员做了修改产生的操作日志，我们系统不希望把运营的操作日志暴露给用户看到，
但是运营期望可以看到用户的日志以及运营自己操作的日志，这些操作日志的bizNo都是订单号，所以为了扩展添加了子类型字段,主要是为了对日志做分类，查询方便，支持更多的业务。

```
    @LogRecord(
            subType = "MANAGER_VIEW",
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,测试变量「{{#innerOrder.productName}}」,下单结果:{{#_ret}}",
            type = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    public boolean createOrder(Order order) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());
        // db insert order
        Order order1 = new Order();
        order1.setProductName("内部变量测试");
        LogRecordContext.putVariable("innerOrder", order1);
        return true;
    }
```

###### 4. 支持记录操作的详情或者额外信息

如果一个操作修改了很多字段，但是success的日志模版里面防止过长不能把修改详情全部展示出来，这时候需要把修改的详情保存到
extra 字段， extra 是一个 String ，需要自己序列化。这里的 #order.toString()
是调用了 Order 的 toString() 方法。 如果保存 JSON，自己重写一下 Order 的 toString() 方法就可以。

```
    @LogRecord(
            extra = "{{#order.toString()}}",
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,测试变量「{{#innerOrder.productName}}」,下单结果:{{#_ret}}",
            type = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    public boolean createOrder(Order order) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());
        // db insert order
        Order order1 = new Order();
        order1.setProductName("内部变量测试");
        LogRecordContext.putVariable("innerOrder", order1);
        return true;
    }
```

###### 5. 如何指定操作日志的操作人是什么？ 框架提供了两种方法

* 第一种：手工在LogRecord的注解上指定。这种需要方法参数上有operator

```
    @LogRecord(
            operator = "{{#currentUser}}",
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,下单结果:{{#_ret}}",
            type = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    public boolean createOrder(Order order, String currentUser) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());
        // db insert order
        return true;
    }
```

这种方法手工指定，需要方法参数上有 operator 参数，或者通过 SpEL 调用静态方法获取当前用户。

* 第二种： 通过默认实现类来自动的获取操作人，由于在大部分web应用中当前的用户都是保存在一个线程上下文中的，所以每个注解都加一个operator获取操作人显得有些重复劳动，所以提供了一个扩展接口来获取操作人
  框架提供了一个扩展接口，使用框架的业务可以 implements 这个接口自己实现获取当前用户的逻辑， 对于使用 Springboot 的只需要实现
  IOperatorGetService 接口，然后把这个 Service
  作为一个单例放到 Spring 的上下文中。使用 Spring Mvc 的就需要自己手工装配这些 bean 了。

```
@Configuration
public class LogRecordConfiguration {

    @Bean
    public IOperatorGetService operatorGetService() {
        return () -> Optional.of(OrgUserUtils.getCurrentUser())
                .map(a -> new OperatorDO(a.getMisId()))
                .orElseThrow(() -> new IllegalArgumentException("user is null"));
    }
}

//也可以这么搞：
@Service
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

    @Override
    public OperatorDO getUser() {
        OperatorDO operatorDO = new OperatorDO();
        operatorDO.setOperatorId("SYSTEM");
        return operatorDO;
    }
}
```

###### 6. 日志文案调整

对于更新等方法，方法的参数上大部分都是订单ID、或者产品ID等，
比如下面的例子：日志记录的success内容是："更新了订单{{#orderId}},更新内容为...."，这种对于运营或者产品来说难以理解，所以引入了自定义函数的功能。
使用方法是在原来的变量的两个大括号之间加一个函数名称 例如 "{ORDER{#orderId}}" 其中 ORDER
是一个函数名称。只有一个函数名称是不够的,需要添加这个函数的定义和实现。可以看下面例子
自定义的函数需要实现框架里面的IParseFunction的接口，需要实现两个方法：

* functionName() 方法就返回注解上面的函数名；

* executeBefore() true：这个函数解析在注解方法执行之前运行，false：方法执行之后。有些更新方法，需要在更新之前查询出数据，这时候可以吧executeBefore返回true，
  executeBefore为true的时候函数内不能使用_ret和errorMsg的内置变量

* apply()函数参数是 "{ORDER{#orderId}}"中SpEL解析的#orderId的值，这里是一个数字1223110，接下来只需要在实现的类中把 ID
  转换为可读懂的字符串就可以了，
  一般为了方便排查问题需要把名称和ID都展示出来，例如："订单名称（ID）"的形式。

```
    // 没有使用自定义函数
    @LogRecord(success = "更新了订单{{#orderId}},更新内容为....",
            type = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}",
            extra = "{{#order.toString()}}")
    public boolean update(Long orderId, Order order) {
        return false;
    }

    //使用了自定义函数，主要是在 {{#orderId}} 的大括号中间加了 functionName
    @LogRecord(success = "更新了订单{ORDER{#orderId}},更新内容为...",
            type = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}",
            extra = "{{#order.toString()}}")
    public boolean update(Long orderId, Order order) {
        return false;
    }

    // 还需要加上函数的实现
    @Slf4j
    @Component
    public class OrderParseFunction implements IParseFunction {
    
        @Override
        public boolean executeBefore() {
            return true;
        }
    
        @Override
        public String functionName() {
            return "ORDER";
        }
    
        @Override
        public String apply(Object value) {
            log.info("@@@@@@@@");
            if (StringUtils.isEmpty(value)) {
                return "";
            }
            log.info("###########,{}", value);
            Order order = new Order();
            order.setProductName("xxxx");
            return order.getProductName().concat("(").concat(value.toString()).concat(")");
        }
    }
```

###### 7. 日志文案调整 使用 SpEL 三目表达式

```
    @LogRecord(type = LogRecordTypeConstant.CUSTOM_ATTRIBUTE, bizNo = "{{#businessLineId}}",
            success = "{{#disable ? '停用' : '启用'}}了自定义属性{ATTRIBUTE{#attributeId}}")
    public CustomAttributeVO disableAttribute(Long businessLineId, Long attributeId, boolean disable) {
    	return xxx;
    }
```

###### 8. 日志文案调整 模版中使用方法参数之外的变量&函数中也可以使用Context中变量

可以在方法中通过 LogRecordContext.putVariable(variableName, Object) 的方法添加变量，第一个对象为变量名称，后面为变量的对象，
然后我们就可以使用 SpEL 使用这个变量了，例如：例子中的
{{#innerOrder.productName}} 是在方法中设置的变量，除此之外，在上面提到的自定义函数中也可以使用LogRecordContext中的变量。

若想跨方法使用，可通过LogRecordContext.putGlobalVariable(variableName, Object) 放入上下文中，此优先级为最低，若方法上下文中存在相同的变量，则会覆盖

```
    @Override
    @LogRecord(
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,测试变量「{{#innerOrder.productName}}」,下单结果:{{#_ret}}",
            type = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    public boolean createOrder(Order order) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());
        // db insert order
        Order order1 = new Order();
        order1.setProductName("内部变量测试");
        LogRecordContext.putVariable("innerOrder", order1);
        return true;
    }
```

###### 9. 函数中使用LogRecordContext的变量

使用 LogRecordContext.putVariable(variableName, Object) 添加的变量除了可以在注解的 SpEL 表达式上使用，还可以在自定义函数中使用
这种方式比较复杂，下面例子中示意了列表的变化，比如
从[A,B,C] 改到 [B,D] 那么日志显示：「删除了A，增加了D」

```
    @LogRecord(success = "{DIFF_LIST{'文档地址'}}", bizNo = "{{#id}}", prefix = REQUIREMENT)
    public void updateRequirementDocLink(String currentMisId, Long id, List<String> docLinks) {
        RequirementDO requirementDO = getRequirementDOById(id);
        LogRecordContext.putVariable("oldList", requirementDO.getDocLinks());
        LogRecordContext.putVariable("newList", docLinks);

        requirementModule.updateById("docLinks", RequirementUpdateDO.builder()
                .id(id)
                .docLinks(docLinks)
                .updater(currentMisId)
                .updateTime(new Date())
                .build());
    }
    
    
    @Component
    public class DiffListParseFunction implements IParseFunction {
    
        @Override
        public String functionName() {
            return "DIFF_LIST";
        }
    
        @SuppressWarnings("unchecked")
        @Override
        public String apply(String value) {
            if (StringUtils.isBlank(value)) {
                return value;
            }
            List<String> oldList = (List<String>) LogRecordContext.getVariable("oldList");
            List<String> newList = (List<String>) LogRecordContext.getVariable("newList");
            oldList = oldList == null ? Lists.newArrayList() : oldList;
            newList = newList == null ? Lists.newArrayList() : newList;
            Set<String> deletedSets = Sets.difference(Sets.newHashSet(oldList), Sets.newHashSet(newList));
            Set<String> addSets = Sets.difference(Sets.newHashSet(newList), Sets.newHashSet(oldList));
            StringBuilder stringBuilder = new StringBuilder();
            if (CollectionUtils.isNotEmpty(addSets)) {
                stringBuilder.append("新增了 <b>").append(value).append("</b>：");
                for (String item : addSets) {
                    stringBuilder.append(item).append("，");
                }
            }
            if (CollectionUtils.isNotEmpty(deletedSets)) {
                stringBuilder.append("删除了 <b>").append(value).append("</b>：");
                for (String item : deletedSets) {
                    stringBuilder.append(item).append("，");
                }
            }
            return StringUtils.isBlank(stringBuilder) ? null : stringBuilder.substring(0, stringBuilder.length() - 1);
        }
    }
```

###### 10. 使用 condition，满足条件的时候才记录日志

比如下面的例子：condition 变量为空的情况 才记录日志；condition 中的 SpEL 表达式必须是 bool 类型才生效。不配置 condition
默认日志都记录

```
    @LogRecord(success = "更新了订单ORDER{#orderId}},更新内容为...",
            type = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}",
            detail = "{{#order.toString()}}", condition = "{{#condition == null}}")
    public boolean testCondition(Long orderId, Order order, String condition) {
        return false;
    }
```

###### 11. 使用对象 diff 功能

支持字段别名,自定义格式化,数据脱敏,数据对象自定义深度解析等特性
详见  [java-obj-diff使用说明](https://github.com/openquartz/java-obj-diff)

###### 12. 增加了操作日志 Monitor 监控接口

用户可以自己实现 ILogRecordPerformanceMonitor 接口，实现对日志性能的监控。默认是 DefaultLogRecordPerformanceMonitor 需要开启
debug 才能打印日志

```
//开启debug方法：
logging:
  level:
    com.openquartz.logapi.service.impl: debug


//日志打印例子：
---------------------------------------------
ns         %     Task name
---------------------------------------------
000111278  003%  before-execute
003277960  097%  after-execute
```

###### 13.记录成功日志的条件

默认逻辑：被注解的方法不抛出异常会记录 success 的日志内容，抛出异常会记录 fail 的日志内容， 当指定了 successCondition 后
successCondition 表达式为true的时候才会记录
success内容，否则记录 fail 内容

```
    @LogRecord(success = "更新成功了订单{ORDER{#orderId}},更新内容为...",
            fail = "更新失败了订单{ORDER{#orderId}},更新内容为...",
            type = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}",
            successCondition = "{{#result.code == 200}}")
    public Result<Boolean> testResultOnSuccess(Long orderId, Order order) {
        Result<Boolean> result = new Result<>(200, "成功", true);
        LogRecordContext.putVariable("result", result);
        return result;
    }
```

###### 14.日志记录与业务逻辑一起回滚

默认日志记录错误不影响业务的流程，若希望日志记录过程如果出现异常，让业务逻辑也一起回滚，在 @EnableLogRecord 中
joinTransaction 属性设置为 true，
另外 @EnableTransactionManagement order 属性设置为0 (让事务的优先级在@EnableLogRecord之前)

```
@EnableLogRecord(tenant = "com.openquartz.test", joinTransaction = true)
@EnableTransactionManagement(order = 0)
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
```

###### 15.方法记录多条日志

若希望一个方法记录多条日志，在方法上重复写两个注解即可，前提是两个注解**不相同**

```
    @LogRecord(
            subType = "MANAGER_VIEW", extra = "{{#order.toString()}}",
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,下单结果:{{#_ret}}",
            type = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    @LogRecord(
            subType = "USER_VIEW",
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,下单结果:{{#_ret}}",
            type = LogRecordType.USER, bizNo = "{{#order.orderNo}}")
    public boolean createOrders(Order order) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());
        return true;
    }
```

###### 16.用对象的`equals`和`toString`

框架给到用户的比对结果可能不符合用户预期，在此框架提供重载比对方法。
如在`LocalDate`比对中，默认输出结果为：
> 【localDate的dayOfMonth】从【1】修改为【4】；【localDate的dayOfWeek】从【WEDNESDAY】修改为【SATURDAY】；【localDate的dayOfYear】从【32】修改为【35】

在配置文件中加入，`ebl.log.record.useEqualsMethod`，**需要填入类的全路径，多个类用英文逗号分割**

```
mzt:
  log:
    record:
      useEqualsMethod: java.time.LocalDate,java.time.Instant
```

重载后的比对结果为：
> 【localDate】从【2023-02-24】修改为【-999999999-01-01】

#### 框架的扩展点

* 重写OperatorGetServiceImpl通过上下文获取用户的扩展，例子如下

```
@Service
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

    @Override
    public Operator getUser() {
         return Optional.ofNullable(UserUtils.getUser())
                        .map(a -> new Operator(a.getName(), a.getLogin()))
                        .orElseThrow(()->new IllegalArgumentException("user is null"));
       
    }
}
```

* ILogRecordService 保存/查询日志的例子,使用者可以根据数据量保存到合适的存储介质上，比如保存在数据库/或者ES。自己实现保存和删除就可以了

> 也可以只实现保存的接口，毕竟已经保存在业务的存储上了，查询业务可以自己实现，不走 ILogRecordService
> 这个接口，毕竟产品经理会提一些千奇百怪的查询需求。

```
@Service
public class DbLogRecordServiceImpl implements ILogRecordService {

    @Resource
    private LogRecordMapper logRecordMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(LogRecord logRecord) {
        log.info("【logRecord】log={}", logRecord);
        LogRecordPO logRecordPO = LogRecordPO.toPo(logRecord);
        logRecordMapper.insert(logRecordPO);
    }

    @Override
    public List<LogRecord> queryLog(String bizKey, Collection<String> types) {
        return Lists.newArrayList();
    }

    @Override
    public PageDO<LogRecord> queryLogByBizNo(String bizNo, Collection<String> types, PageRequestDO pageRequestDO) {
        return logRecordMapper.selectByBizNoAndCategory(bizNo, types, pageRequestDO);
    }
}
```

* IParseFunction 自定义转换函数的接口，可以实现IParseFunction 实现对LogRecord注解中使用的函数扩展 例子：

```
@Component
public class UserParseFunction implements IParseFunction {
    private final Splitter splitter = Splitter.on(",").trimResults();

    @Resource
    @Lazy
    private UserQueryService userQueryService;

    @Override
    public String functionName() {
        return "USER";
    }

    @Override
    // 11,12 返回 11(小明，张三)
    public String apply(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        List<String> userIds = Lists.newArrayList(splitter.split(value));
        List<User> misDOList = userQueryService.getUserList(userIds);
        Map<String, User> userMap = StreamUtil.extractMap(misDOList, User::getId);
        StringBuilder stringBuilder = new StringBuilder();
        for (String userId : userIds) {
            stringBuilder.append(userId);
            if (userMap.get(userId) != null) {
                stringBuilder.append("(").append(userMap.get(userId).getUsername()).append(")");
            }
            stringBuilder.append(",");
        }
        return stringBuilder.toString().replaceAll(",$", "");
    }
}
```
* 异步保存日志支持

默认同步方式。如需异步保存日志可以注入Bean 到Spring中，Bean 名称为 `executeSaveLogExecutor`.

#### 变量相关

> LogRecord 可以使用的变量出了参数也可以使用返回值 #_ret 变量，以及异常的错误信息 #_errorMsg，也可以通过 SpEL 的 T
> 方式调用静态方法噢



