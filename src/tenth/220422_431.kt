package tenth

import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/*

    애노테이션이란?
     코드 사이에 주석처럼 쓰이며 특별한 의미, 기능을 수행하도록 하는 기술이다.
     프로그램에게 추가적인 정보를 제공해주는 메타데이터라고 볼 수 있다.
      meta data : 데이터를 위한 데이터
     애노테이션 자체는 아무런 동작을 가지지 않는 표식일 뿐이다.

    리플렉션이란?
     구체적인 클래스 타입을 알지 못해도 그 클래스의 메소드, 타입, 변수들에 접근할 수있도록 해주는 자바 API
     자바의 리플렉션은 클래스, 인터페이스, 메소드들을 찾을 수 있고, 객체를 생성하거나 변수를 변경하거나 메소드를 호출할 수 있다.
     코드를 작성할 시점에는 어떤 타입의 클래스를 사용할지 모르지만, 런타임 시점에 지금 실행되고 있는 클래스를 가져와서 실행해야 할 때 사용한다.
      프레임 워크나 IDE에서 이런 동적인 바인딩을 이용한 기능을 제공한다.
      intelliJ의 자동완성 기능은 리플렉션을 이용한 기능이다.

    Reference
     - https://velog.io/@yeon/Reflection%EC%9D%B4%EB%9E%80

    애노테이션과 리플렉션
     애노테이션을 사용하면 라이브러리가 요구하는 의미를 클래스에게 부여할 수 있다.
     애노테이션을 선언할 때 사용하는 문법은 자바와 약간 다르다.

     리플렉션을 사용하면 실행 시점에 컴파일러 내부 구조를 분석할 수 있다.
     리플렉션 API의 일반 구조는 자바와 같지만 세부 사항에서 약간 차이가 있다.

    애노테이션과 리플렉션의 사용법을 보여주는 예제로 JSON 직렬화와 역직렬화 라이브러리인 JKid 를 구현한다.
     실행시점에 코틀린 객체의 프로퍼티를 읽거나 JSON 파일에서 읽은 데이터를 코틀린 객체로 만들기 위해 리플렉션을 사용한다.
     애노테이션을 통해 제이키드 라이브러리가 클래스와 프로퍼티를 직렬화하고 역직렬화하는 방식을 변경한다.

    애노테이션
     메타데이터를 선언에 추가하면 애노테이션을 처리하는 도구가 컴파일 시점이나 실행 시점에 적절한 처리를 해준다.

    애노테이션 적용
     애노테이션은 @과 애노테이션 이름으로 이뤄진다.
     애노테이션을 적용하려면 적용하려는 대상 앞에 애노테이션을 붙이면 된다.

     @Deprecated 애노테이션
      코틀린에서는 replaceWith 파라미터를 통해 옛 버전을 대신할 수 있는 패턴을 제시할 수 있다.

     애노테이션에 인자를 넘길 때는 괄호 안에 인자를 넣는다.
      인자로는 원시 타입의 값, 문자열, enum, 클래스 참조, 다른 애노테이션 클래스, 지금까지 말한 요소로 이뤄진 배열이 들어갈 수 있다.
      애노테이션 인자를 컴파일 시점에 알 수 있어야 한다.
       따라서 임의의 프로퍼티를 인자로 지정할 수 없다.
       프로퍼티를 애노테이션 인자로 사용하려면 그 앞에 const 변경자를 붙여야 한다. (일반 프로퍼티는 오류가 발생한다)
        컴파일러는 const가 붙은 프로퍼티를 컴파일 시점 상수로 취급한다.
         const가 붙은 파라미터는 파일의 맨 위나 object 안에 선언해야 하며, 원시 타입이나 String으로 초기화해야만 한다.

     애노테이션 인자를 지정하는 문법
      애노테이션 인자를 지정하는 문법은 자바와 약간 다르다.
      - 클래스를 애노테이션 인자로 지정할 때는 클래스 이름 뒤에 ::class 를 넣어야 한다. (클래스 참조)
      - 다른 애노테이션을 인자로 지정할 때는 인자로 들어가는 애노테이션 이름 앞에 @를 붙이지 않는다.
      - 배열을 인자로 지정하려면 arrayOf 함수를 사용한다.
         자바에서 선언한 애노테이션 클래스를 사용한다면 value 라는 이름의 파라미터가 필요에 따라 자동으로 가변 길이 인자로 변환된다.
         따라서 그런 경우 @JavaAnnotationWithArrayValue("abc", "foo", "bar") 처럼 arrayOf 함수를 쓰지 않아도 된다.


    애노테이션 대상
     사용 지점 대상(use-site target)은 @ 기호와 애노테이션 이름 사이에 붙으며, 애노테이션 이름과는 콜론(:)으로 분리된다.
      ex) @get:Rule (get <- 사용지점 대상 / Rule <- 애노테이션 이름) , 이 애노테이션 사용하는 예를 보자.
       제이유닛에서는 각 테스트 메소드 앞에 그 메소드를 실행하기 위한 규칙을 지정할 수 있다.
       규칙을 지정하려면 공개 필드나 메소드 앞에 @Rule 을 붙여야 한다.
       @Rule 은 공개 필드에 적용되지만 코틀린의 필드는 기본적으로 비공개이기 때문에 예외가 생긴다. 정확한 대상에 적용하려면 @get:Rule 을 사용해야한다.(게터에 애노테이션을 붙임)
     프로퍼티에 자바에 선언된 애노테이션을 붙이는 경우 기본적으로 필드에 그 애노테이션이 붙는다.
     코틀린으로 애노테이션을 선언하면 프로퍼티에 직접 적용할 수 있는 애노테이션을 만들 수 있다.
      사용 지점 대상을 지원하는 대상 목록
       - property 프로퍼티 전체, 자바에서 선언된 애노테이션에는 이 사용 지점 대상을 사용할 수 없다.
       - field 프로퍼티에 의해 생성되는 필드
       - get 프로퍼티 게터
       - set 프로퍼티 세터
       - receiver 확장 함수나 프로퍼티의 수신 객체 파라미터
       - param 생성자 파라미터
       - setparam 세터 파라미터
       - delegate 위임 파라미터의 위임 인스턴스를 담아둔 필드
       - file 파일 안에 선언된 최상위 함수와 프로퍼티를 담아두는 클래스
         file 대상을 사용하는 애노테이션은 package 선언 앞에서 파일의 최상위 수준에만 적용할 수 있다.
     자바와 달리 코틀린에서는 애노테이션 인자로 클래스 or 함수 선언 or 타입 외에 임의의 식을 허용한다.
     가장 흔히 쓰이는 예로는 컴파일러 경고를 무시하기 위한 @Suppress 애노테이션이 있다.

    애노테이션을 사용하는 고전적인 예제로 객체 직렬화 제어를 들 수 있다.
     직렬화는 객체를 저장장치에 저장하거나 네트워크를 통해 전송하기 위해 텍스트나 이진 형식으로 변환하는 것이다.
     역직렬화는 텍스트나 이진 형식으로 저장된 데이터로부터 원래의 객체를 만들어낸다.

    애노테이션 선언
     일반 클래스와의 차이는 class 키워드 앞에 annotation이라는 변경자가 붙어있다는 점 뿐인 것 같다.
     하지만 애노테이션 클래스는 오직 선언이나 식과 관련 있는 메타데이터의 구조를 정의하기 때문에 내부에 아무 코드도 들어있을 수 없다.
     파라미터가 있는 애노테이션을 정의하려면 애노테이션 클래스의 주 생성자에 파라미터를 선언해야 한다.
      다만 애노테이션 클래스에서는 모든 파라미터 앞에 val을 붙여야 한다.

    메타애노테이션 : 애노테이션을 처리하는 방법 제어
     애노테이션 클래스에 적용할 수 있는 애노테이션을 메타애노테이션이라고 부른다.
     컴파일러가 애노테이션을 처리하는 방법을 제어한다.
     가장 흔히 쓰이는 메타애노테이션은 @Target 으로 애노테이션 적용 대상을 지정하기 위해 사용한다.
     메타애노테이션을 직접 만들어야 한다면 ANNOTATION_CLASS를 대상으로 지정하라.
     대상을 PROPERTY 로 지정한 애노테이션은 자바에서 사용할 수 없다. 자바에서 사용하려면 FIELD를 두 번째 대상으로 추가해야 한다.
      코틀린 프로퍼티와 자바 필드에 적용가능하게 된다.

    애노테이션 파라미터로 클래스 사용
     어떤 클래스를 선언 메타데이터로 참조할 수 있는 기능이 필요할 때도 있다.
      클래스 참조를 파라미터로 하는 애노테이션 클래스를 선언하면 사용할 수 있다.
      annotation class DeserializeInterface(val targetClass: KClass<out Any>)
       KClass는 java.lang.Class 타입과 같은 역할을 하는 코틀린 타입이다.
        코틀린 클래스에 대한 참조를 저장할 때 KClass 타입을 사용한다.
       KClass의 타입 파라미터는 KClass의 인스턴스가 가리키는 코틀린 타입을 지정한다.(즉, 참조하려는 클래스 타입을 말함)
        예를 들어 CompanyImpl::class의 타입은 KClass<CompanyImpl> 이다.

    몇 가지 애노테이션
     @JsonExclude
      애노테이션을 사용하면 직렬화나 역직렬화 시 그 프로퍼티를 무시할 수 있다.
     @JsonName
      애노테이션을 사용하면 프로퍼티를 표현하는 키/값 쌍의 키로 프로퍼티 이름 대신 애노테이션이 지정한 이름을 쓰게할 수 있다.
      제외할 프로퍼티에는 반드시 디폴트 값을 지정해야만 한다. 없으면 역직렬화 시 객체를 만들 수 없다.
     @Target
      애노테이션을 적용할 수 있는 요소의 유형을 지정한다.
      애노테이션 클래스에 대해 구체적인 @Target을 지정하지 않으면 모든 선언에 적용할 수 있는 애노테이션이 된다.
     @Retention
      정의 중인 애노테이션 클래스를 소스 수준에서만 유지할지, .class 파일에 저장할지, 실행 시점에 리플렉션을 사용해 접근할 수 있게 할지를 지정하는 메타애노테이션이다.
      자바 컴파일러는 기본적으로 .class 파일에는 저장하지만 런타임에는 사용할 수 없게 한다. 하지만 대부분의 애노테이션은 런타임에도 사용할 수 있어야 하므로
      코틀린에서는 기본적으로 애노테이션의 @Retention 을 RUNTIME 으로 지정한다.

    애노테이션에 저장된 데이터에 접근하는 방법
    리플렉션을 사용해야 애노테이션에 저장된 데이터에 접근할 수 있다.
    리플렉션 : 실행 시점에 코틀린 객체 내부 관찰
     실행 시점에 객체의 프로퍼티와 메소드에 접근할 수 있게 해주는 방법이다.
     보통 컴파일러는 실제로 가리키는 선언을 컴파일 시점에 찾아내서 해당 선언이 실제 존재함을 보장한다.
      하지만 타입과 관계없이 객체를 다뤄야 하거나 객체가 제공하는 메소드나 프로퍼티 이름을 오직 실행 시점에만 알 수 있는 경우가 있다.
      직렬화 라이브러리는 어떤 객체든 JSON을 변환할 수 있어야 하고, 실행 시점이 되기 전까지는 라이브러리가 직렬화할 프로퍼티나 클래스에 대한 정보를 알 수 없다.
      이런 경우 리플렉션을 사용해야 한다.
     코틀린에서 리플렉션을 사용하려면 두 가지 서로 다른 리플렉션 API 를 다뤄야 한다.
      첫 번째는 자바가 java.lang.reflect 패키지를 통해 제공하는 표준 리플렉션이다.
       코틀린 클래스는 일반 자바 바이트코드로 컴파일되므로 자바 리플렉션 API도 코틀린 클래스를 컴파일한 바이트코드를 완벽히 지원한다.
      두 번째는 코틀린이 kotlin.reflect 패키지를 통해 제공하는 코틀린 리플렉션 개념에 대한 리플렉션을 제공한다.
       자바에는 없는 프로퍼티나 널이 될 수 있는 타입과 같은 코틀린 고유 개념에 대한 리플렉션을 제공한다.
       현재 코틀린 리플렉션 API는 자바 리플렉션 API를 완전히 대체할 수 있는 복잡한 기능을 제공하지는 않는다. 따라서 자바 리플렉션을 대안으로 사용해야 하는 경우가 생긴다.
       코틀린 리플렉션 API가 코틀린 클래스만 다룰 수 있는 것은 아니다. 다른 JVM 언어에서 생성한 바이트코드를 충분히 다룰 수 있다.

    코틀린 리플렉션 API
     KClass
      코틀린 리플렉션 API를 사용할 때 처음 접하게 되는 클래스를 표현하는 KClass다.
      java.lang.Class에 해당하는 KClass를 사용하면 클래스 안에 있는 모든 선언을 열거하고 각 선언에 접근하거나 클래스의 상위 클래스를 얻는 등의 작업이 가능하다.
      실행 시점에 객체의 클래스를 얻으려면 먼저 객체의 javaClass 프로퍼티를 사용해 객체의 자바 클래스를 얻어야 한다.
       javaClass는 자바의 java.lang.Object.getClass() 와 같다.
      일단 자바 클래스를 얻었으면 .kotlin 확장 프로퍼티를 통해 자바에서 코틀린 리플렉션 API로 옮겨올 수 있다.
     KCallable
      함수와 프로퍼티를 아우르는 공통 상위 인터페이스다. 그 안에는 call 메소드가 들어있다.
       call 을 사용하면 함수나 프로퍼티의 게터를 호출할 수 있다.

     KFunction
     KProperty
      최상위 프로퍼티는 KProperty() 인터페이스의 인스턴스로 표현되며, 인자가없는 get 메소드가 있다.
      멤버 프로퍼티는 KProperty1 인스턴스로 표현되며, 인자가 1개인 get 메소드가 있다.
       멤버 프로퍼티는 객체에 속해 있으므로 get 메소드에게 객체 인스턴스를 넘겨야 프로퍼티 값을 얻을 수 있다.
       KProperty1 은 제네릭 클래스이며, KProperty<Person, Int> 타입이라면 첫 번째 파라미터는 수신 객체 타입, 두 번째 파라미터는 프로퍼티 타입을 표현한다.
      최상위 수준이나 클래스 안에 정의된 프로퍼티만 리플렉션으로 접근할 수 있고, 함수의 로컬 변수에는 접근할 수 없다.

    애노테이션을 활용한 직렬화 제어
    어떻게 특정 애노테이션이 붙은 프로퍼티를 제외할 수 있을까?
     KAnnotatedElement 인터페이스에는 annotations 프로퍼티가 있다.
      annotations 는 소스 코드상 해당 요소에 적용된 @Retention을 RUNTIME 으로 지정한 모든 애노테이션 인스턴스의 컬렉션이다.
     KProperty 는 KAnnotatedElement 를 확장하므로 property.annotations 를 통해 프로퍼티의 모든 애노테이션을 얻을 수 있다.
      그러나 모든 애노테이션을 사용하지 않고, 어떤 한 애노테이션을 찾기만 하면 된다. 이럴 때 findAnnotation 이라는 함수가 쓸모 있다.
       findAnnotation 함수는 인자로 전달받은 타입에 해당하는 애노테이션이 있으면 그 애노테이션을 반환한다.




*/

data class Person_438(val name: String, val age: Int)

fun foo_450(x: Int) = println(x)

var counter = 0

// 리플렉션 API 실전에서 사용하는 예
fun serialize(obj: Any) = buildString { serializeObject(obj) }
private fun StringBuilder.serializeObject(obj: Any) {
    val kClass = obj.javaClass.kotlin // 객체의 kClass 를 얻는다.
    val properties = kClass.memberProperties // 객체의 모든 프로퍼티를 얻는다.
    properties.joinToStringBuilder(this, "{", "}") { prop ->
        serializeString(prop.name)
        append(": ")
        serializePropertyValue(prop.get(obj))
    }
    
    obj.javaClass.kotlin.memberProperties
            .filter { it.findAnnotation<JsonExclude>() == null }
            .joinToStringBuilder(this, "{", "}") {
                serializeProperty(it, obj)
            }
}

private fun StringBuilder.serializeProperty(prop: KProperty<Any, *>, obj: Any) {
    val jsonNameAnn = prop.findAnnotation<JsonName>()
    val propName = jsonNameAnn?.name ?: prop.name
    serializeString(propName)
    append(": ")
    serializePropertyValue(prop.get(obj))
}


fun main() {
    val person = Person_438("Alice", 29)
    val kClass = person.javaClass.kotlin // KClass<Person_438>의 인스턴스를 반환한다.
    println(kClass.simpleName)
    kClass.memberProperties.forEach { println(it.name) } // memberProperties 는 확장 함수다.

    val kFunction = ::foo_450 // KFunction1의 인스턴스
    kFunction.call(12) // call 메소드는 모든 타입의 함수에 적용할 수 있는 일반적인 메소드지만 타입 안전성을 보장해주지는 않는다.

    val kProperty = ::counter
    kProperty.setter.call(21) // 리플렉션 기능을 통해 세터를 호출하면서 21을 인자로 넘긴다.
    println(kProperty.get()) // get 메소드를 통해 프로퍼티 값을 가져온다.

    val memberProperty = Person_438::name // 멤버 프로퍼티 참조
    println(memberProperty.get(person)) // get 메소드에 프로퍼티를 얻고자 하는 객체 인스턴스 전달

}

