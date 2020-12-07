
## JsonUtil 配合ApiResponse泛型的使用

当前采用的是moshi库来处理json，相比Gson来说，用法复杂了些，
但是性能提升，并且完美支持kotlin。

> Gson在支持 kotlin 非空方面不友好，必须指定默认值才行。

### 一层泛型实例

需要显示的描述出子层级的泛型。JsonUtil工具内部会转换为`Types.newParameterizedType`的方式来描述当前type的层级。

```kotlin
val test: ApiResponse<String> = ApiResponse(
    100, "测试", "内容"
)
val toJson1 = jsonUtil.toJson(test, String::class.java)
LogUtil.d {
    toJson1
}

val testVo = jsonUtil.fromJson<ApiResponse<String>>(toJson1, String::class.java)
LogUtil.d { testVo.toString() }

```
### 多层级泛型实例

需要使用`Types.newParameterizedType`来描述出嵌套的子层级。

```kotlin
val test2: ApiResponse<ApiResponse<String>> = ApiResponse(
    200, "测试2", test
)

val toJson2 = jsonUtil.toJson(
    test2,
    Types.newParameterizedType(ApiResponse::class.java, String::class.java)
)
LogUtil.d {
    toJson2
}

val testVo2 = jsonUtil.fromJson<ApiResponse<ApiResponse<String>>>(
    toJson2,
    Types.newParameterizedType(ApiResponse::class.java, String::class.java)
)

LogUtil.d { testVo2.toString() }
```