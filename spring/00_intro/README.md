# 00_introduction

### Цель

Понять зачем нужен Spring/Springboot. 

### Теория

#### IoC и DI
Описать один предложением, что такое Spring и зачем он нужен довольно тяжело, так как спрингу уже больше 15 лет, и за это время в нем было реализовано бесчисленное множество различного функционала. Тем неменее можно выделить основную часть фреймворка - а именно **inversion of controll (IoC)** контейнер. 
Суть принципе IoC заключается в интерватации способа работы обычной программа. Типичная программа реализует конкретный функционал за счет вызова максимально абстрактных функций/методов/кусков кода в такой же максимально абстрактной библиотеке. Т.е. главенствующую роль в исполнении произвольного кода занимает сам этот код. В случае с IoC - главенствующую роль занимает некий фреймворк, а кастомный код занимает какие-то места в этом фреймворке. Вот есть прекрасное [полутороминутное видео[1]](https://www.youtube.com/watch?v=vFzP2SaMyA0) про IoC.

Существует несколько способо реализации принципе IoC. Самым распространенным является внедрение зависимостей - Dependency Injection (DI). Суть DI в явном указании зависимостей компонентов в программе. Таким образом, компоненты DI не могут (и не должны) осуществлять свою работу, если вызывающий/создающий их компонент 
не передаст внутрь этого компонента все необходимые зависимости. Рассмотрим пример.

**Ужасная жизнь без DI**. Представим что мы пишет интернет-магазин. У нас будет 2 класса - собственно интернет магазин ```OnlineStore``` и хранилище с вещами этого магазина - ```Warehouse```. Если не использовать DI, наша программа могла вы выглядеть следующим образом.
```
public class OnlineStore {

    private Warehouse warehouse;

    public OnlineStore() {
        this.warehouse = new Warehouse();
    }
}
```

Таким образом, от внешнего мира скрыт факт того, что ```OnlineStore``` зависит от класса ```Warehouse```. так *можно* делать, но давайте рассмотри альтернативу.

**Прекрасная жизнь с DI**. Давайте явно выразим зависимость ```OnlineStore``` от ```Warehouse```.

```
public class OnlineStore {

    private final Warehouse warehouse;

    public OnlineStore(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
```

Обратите внимание на модификатор final и на передачу ```Warehouse``` в конструктор ```OnlineStore```. Что изменилось? Теперь любой класс, который создает ```OnlineStore``` ОБЯЗАН передать в конструктор ```Warehouse```, иначе использовать класс будет невозможно. Такой подход дает несколько преимуществ.

Во-первых, становится очень просто подменять одну реализацию другой

```
public class ShirtsWarehouse extends Warehouse {

}


<somewhere deeper in your code> 

    public static void main(String[] args) {
	// Создаем OnlineStore, который работает с конкретной имплементацией нашего Warehouse
        var shirtsStore = new OnlineStore(new ShirtsWarehouse);
    }
```

Такая замена в частности позволяет очень просто подменять реализацию на mock во время тестирования.

Во-вторых, мы получаем возможность сразу же обнаружить циклические зависимости в наших классах. Так как создание всего происходит через констуктор и внедрение зависимостей мы попросту не может создать циклические зависимости.

К слову, если вы работали с конфигурацией Spring через XML советую обратится к [документации[2]](https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/core.html#beans-java), где рассказывается об основных аннотациях для спринга.

#### А при чем здесь Spring

```Spring Core``` как раз таки предоставляет IoC контейнер, работающий по принципу DI. Это самое сердце фреймворка и все его возможности становятся доступны через этот самый DI. Важно понимтаь, что концепция DI никак не связана ни с базами данных, ни с вебом, ни с интерфейсами. Любое Java (или Kotlin) приложение может воспользоваться Spring для управления зависимостями. 

Spring позволяет на полуавтомате внедрять зависимости в классы. Фреймворк сам контроллирует этапы создания и время жизни этих зависимстей.

Помимо Core, но используя этот самый Core,  Spring охватывает все или практически все возможности, которые могут понадобится при разработке современных приложений, а именно:
1. IoC контейнер и DI
2. Взаимодействие в Web (в том числе - реактивное и асинхронное)
3. Взаимодействие с хранилищами. В том числе почти весь NoSQL например mongoDB, cassandra, neo4j. Также взаимодействие с in-memory хранилищами
4. Взаимодействия с очередями/топиками и кешами, в том числе kafka, reddis, ehcache, memcached. 
4. Поддержка облачных решение/микросервисов, в том числе Service Discovery, trancing, global configuration, API gateways. 
5. Безопасность приложений, в том числе в распределенных средах через oauth. 
6. Мощный фрейморк для тестирования всего выше перечисленного функционала.

#### Spring boot

Чтобы сконфигурировать какой-то компонент приложения - например Servlet API для работы с Web, необходимо проделать очень много работы. Это фундамментальная сложность, так как функционал Sevlet API в своб очередь сам довольно богатый и всеохватывающий. Но эту сложность хотелось бы упростить. Еще одной проблемой является то, что как правило набор действий, необходимых для страрта приложения, несильно отличается от одного приложения к другому. Как правило достаточно взять некоторой "конфигурации поумолчанию", а затем немного её подправить. Именно этим и занимается Spring boot.

**Spring Boot** - Это базирующийся на Spring фреймворк, позволяющий упростить подход к конфигурации Spring. Основная функция бута - это автоматическая конфигурация компонентов Spring на основе зависимостей, которые есть внутри приложения, а также предоставление простых интерфейсов для переопределения/дополнения возможностей фреймворка. В настоящий момент скорее всего нет смысла начинать проект на Spring без Spring Boot. Делает он это при помощи особых классов-автоконфигураторов. При работе с фреймворком, как правило достаточно добавить в зависимости один из артефактов-стартеров, чтобы сразу получить сконфигурированный поумолчанию компонент, например добавление зависимости :

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
``` 

Позволит пользоваться сразу всеми возможностями фреймворка для тестирования спринга (кроме тестирования security - для него нужна дополнительная зависимость spring-security-test). 

[Здесь[3]](https://dzone.com/articles/building-your-first-spring-boot-web-application-ex) можно посмотреть мини-гайд по старту Spring Boot приложения.

#### Так что же почитать!

1. Один дядя записал почти [500 мини упражнений по Spring5 + Hibernate[4]](https://coursehunters.net/course/spring-i-hibernate-dlya-novichkov). Очень рекомендую сначала посмотреть. Первые несколько частей можно пропустить, там про установку среды.
2. У Spring очень хорошая документация и javadoc, поэтому общий совет если что-то не понятно - лезть в документацию или исходники. Для ознакомления рекомендую прочитать всю доку, но для начала достаточного [введения[5]](https://docs.spring.io/spring/docs/current/spring-framework-reference/overview.html#overview) и [первой части (1 глава)[6]](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#spring-core) про основные концепции и философию вреймворка.
3. В случае со Spring лучшая теория - это практика. Поэтому далее рекомендую обратить к уже наработаному материалу и пройти спринговые курсы ниже

#### Spring.io курсы
##### Prepare project:
1. https://spring.io/guides/gs/maven/

##### Simple web applications:
1. https://spring.io/guides/gs/rest-service/
2. https://spring.io/guides/gs/producing-web-service/
3. https://spring.io/guides/gs/consuming-rest/
4. https://spring.io/guides/gs/consuming-web-service/

##### Developing RESTfull service (tutolrial):
1. https://spring.io/guides/tutorials/rest/

##### Access relational DB:
1. https://spring.io/guides/gs/relational-data-access/
2. https://spring.io/guides/gs/accessing-data-rest/

##### JMS:
1. https://spring.io/guides/gs/messaging-jms/

##### Security:
1. https://spring.io/guides/gs/securing-web/

##### Spring boot:
1. https://spring.io/guides/gs/spring-boot/


### Почитать

1. Что такое IoC https://www.youtube.com/watch?v=vFzP2SaMyA0
2. Конфигурация Spring через аннотации https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/core.html#beans-java
3. Getting started Spring Boot https://dzone.com/articles/building-your-first-spring-boot-web-application-ex
4. Мини-курсы по Spring https://coursehunters.net/course/spring-i-hibernate-dlya-novichkov
5. Spring Overview https://docs.spring.io/spring/docs/current/spring-framework-reference/overview.html#overview
6. Spring IoC container (1 глава) https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#spring-core
