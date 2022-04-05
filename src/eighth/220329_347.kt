package eighth

import java.io.BufferedReader
import java.io.FileReader

/*
    람다는 추상화를 하기 좋은 도구이다.
    8장에서 다룰 것
     람다를 인자로 받거나 반환하는 함수인 고차 함수를 만드는 방법.
     고차 함수로 코드를 더 간결하게 다듬고 코드 중복을 없애고 더 나은 추상화를 구축하는 방법.
     람다를 사용함에 따라 발생할 수 있는 성능상 부가 비용을 없애고 람다 안에서 더 유연하게 흐름을 제어할 수 있는 코틀린 특성인 인라인 함수를 다룬다.

    고차 함수
     다른 함수를 인자로 받거나 함수를 반환하는 함수.
     람다나 함수 참조를 인자로 넘길 수 있거나 람다나 함수 참조를 반환하는 함수.
     코틀린에서는 람다나 함수 참조를 사용해 함수를 값으로 표현할 수 있음.

    함수 타입
     함수 파라미터의 타입을 괄호 안에 넣고 그 뒤에 화살표를 추가한 다음 함수의 반환 타입을 지정하면 된다.
     함수를 정의할 때 반환 타입이 Unit인 경우 생략하지만 함수 타입을 선언할 때는 반환 타입을 반드시 명시해야 하므로 Unit을 빼먹으면 안된다.
     변수에 함수 타입을 지정하면 파라미터 타입을 유추하므로 람다 식 안에서 굳이 파라미터 타입을 적을 필요가 없다.
     함수 타입 자체가 null 이 되도록 만드려면 ()로 묶어서 ?를 붙여줘야한다.
     함수 타입의 각 파라미터에 이름을 지정할 수도 있다.
      람다를 사용할 때 함수 타입에 붙은 이름을 사용해도 되고, 원하는 이름으로 바꿔서 사용해도 된다.
      파라미터 이름은 타입 검사 시 무시된다. 이름이 꼭 함수 타입 선언의 파라미터 이름과 같지 않아도 된다.

    고차함수를 어떻게 구현하는가?
    인자로 받은 함수 호출
     인자로 받은 함수를 호출하는 구문은 일반 함수를 호출하는 구문과 같다.

    자바에서 코틀린 함수 타입 사용
     컴파일된 코드 안에서 함수 타입은 일반 인터페이스로 바뀐다. 즉, 함수 타입의 변수는 FunctionN 인터페이스를 구현하는 객체를 저장한다.
     각 인터페이스에는 invoke 메소드 정의가 하나 들어있다. invoke를 호출하면 함수를 실행할 수 있다.
     invoke 메소드 본문에는 람다의 본문이 들어간다.
     자바 8 람다를 넘기면 자동으로 함수 타입의 값으로 변환된다.
     자바 8 이전의 자바에서는 필요한 FunctionN 인터페이스의 invoke 메소드를 구현하는 무명 클래스를 넘기면 된다.

    디폴트 값을 지정한 함수 타입 파라미터
     파라미터를 함수 타입으로 선언할 때 디폴트 값을 정할 수 있다.
      다른 디폴트 파라미터 값과 마찬가지로 = 뒤에 람다를 넣으면 된다.

    널이 될 수 있는 함수 타입 파라미터
     널이 될 수 있는 함수 타입으로 함수를 받으면 그 함수를 직접 호출할 수 없다는 점에 유의해야한다.
      코틀린은 NPE 가 발생할 가능성이 있으므로 그런 코드의 컴파일을 거부할 것이다.
      null 여부를 명시적으로 검사하는 것도 한 가지 해결 방법이다.
      함수 타입이 invoke 메소드를 구현하는 인터페이스라는 사실을 활용하면 안전 호출 구문으로 "함수타입변수?.invoke()" 처럼 호출할 수도 있다.

    여기까지 함수를 인자로 받는 함수를 만드는 방법
    이제 함수를 반환하는 함수를 본다.

    함수에서 함수를 반환
     함수가 함수를 반환하는 것보다 함수를 인자로 받는 경우가 훨씬 많다. 하지만 함수를 반환하는 함수도 유용하다.
      프로그램의 상태나 다른 조건에 따라 달라질 수 있는 로직이 있는 경우
     함수를 반환하는 함수를 정의하려면 함수의 반환 타입으로 함수 타입을 지정해야 한다.
      함수를 반환하려면 return 식에 람다나 멤버 참조나 함수 타입의 값을 계산하는 식 등을 넣으면 된다.

    고차 함수는 코드 구조를 개선하고 중복을 없앨때
    람다를 활용한 중복 제거
     함수 타입과 람다 식은 재활용하기 좋은 코드를 만들 때 쓸 수 있는 훌륭한 도구다.
     코드의 일부분을 복사해 붙여넣고 싶은 경우가 있다면 그 코드를 람다로 만들면 중복을 제거할 수 있을 것이다.

     일부 잘 알려진 객체 지향 디자인 패턴을 함수 타입과 람다 식을 사용해 단순화할 수 있다.
      전략 패턴을 생각해보자. 람다 식이 없다면 인터페이스를 선언하고 구현 클래스를 통해 전략을 정의해야 한다.
      함수 타입을 언어가 지원하면 일반 함수 타입을 사용해 전략을 표현할 수 있고 경우에 따라 다른 람다 식을 넘김으로써 여러 전략을 전달할 수 있다.

    지금까지 고차 함수를 만드는 방법이었다. 이제 고차 함수의 성능을 보자.
    인라인 함수: 람다의 부가 비용 없애기
     람다를 활용한다고 코드가 항상 더 느려지지는 않는다. inline 키워드를 통해 어떻게 람다의 성능을 개선하는지 보자.
     5장에서 코틀린이 보통 람다를 무명 클래스로 컴파일하지만 그렇다고 람다식을 사용할 때마다 새로운 클래스가 만들어지지는 않는다는 사실을 설명했고,
     람다가 변수를 포획하면 람다가 생성되는 시점마다 새로운 무명 클래스 객체가 생긴다는 사실도 설명했다.
      이런 경우 실행 시점에 무명 클래스 생성에 따른 부가 비용이 든다.
      따라서 람다를 사용하는 구현은 똑같은 작업을 수행하는 일반 함수를 사용한 구현보다 덜 효율적이다.
     그럼 반복되는 코드를 별도의 라이브러리 함수로 빼내되 컴파일러가 자바의 일반 명령문만큼 효율적인 코드를 생성하게 만들 수 없을까?
      코틀린 컴파일러에서는 가능하다.
       inline 변경자를 어떤 함수에 붙이면 컴파일러는 그 함수를 호출하는 모든 문장을 함수 본문에 해당하는 바이트코드로 바꿔치기 해준다.

    어떤 함수를 inline 으로 선언하면 그 함수의 본문이 인라인(inline)된다.
     함수를 호출하는 코드를 함수를 호출하는 바이트코드 대신에 함수 본문을 번역한 바이트 코드로 컴파일한다는 뜻.
     인라인 함수의 본문 뿐만 아니라 전달된 람다의 본문도 함게 인라이닝된다는 점에 유의해야한다.
     인라인 함수를 호출하면서 람다 대신 함수 타입의 변수를 넘길 수도 있다.
      이런 경우 인라인 함수를 호출하는 코드에서 변수에 저장된 람다의 코드를 알 수 없어서 람다의 본문은 인라이닝 되지 않는다.

    인라인 함수의 한계
     인라이닝을 하는 방식으로 인해 람다를 사용하는 모든 함수를 인라이닝할 수는 없다.
      파라미터로 받은 람다를 다른 변수에 저장하고 나중에 그 변수를 사용한다면 람다를 표현하는 객체가 어딘가는 존재해야 하기 때문에 람다를 인라이닝할 수 없다.
     일반적으로 인라인 함수의 본문에서 람다 식을 바로 호출하거나 람다 식을 인자로 전달받아 바로 호출하는 경우에는 그 람다를 인라이닝할 수 있다.
      그런 경우가 아니라면 Illegal usage of inline-parameter 라는 메세지와 함께인라이닝을 금지시킨다.
     시퀀스에 대해 동작하는 메소드 중에는 람다를 받아서 모든 시퀀스 원소에 그 람다를 적용한 새 시퀀스를 반환하는 함수가 많다.
      그런 함수는 인자로 받은 람다를 시퀀스 객체 생성자의 인자로 넘기곤 한다.
     둘 이상의 람다를 인자로 받는다면 noinline 변경자를 파라미터 이름 앞에 붙여서 인라이닝을 금지할 수 있다.

    컬렉션 연산 인라이닝
     코틀린 표준 라이브러리의 컬렉션 함수는 대부분 람다를 인자로 받는다. 함수를 사용하지 않고 직접 이런 연산을 구현하면 더 효과적이지 않을까?
     코틀린의 filter 함수는 인라인 함수다. filter 를 써서 생긴 바이트 코드와 뒤 예제에서 생긴 바이트 코드는 거의 같다.
     시퀀스 연산에서는 람다가 인라이닝되지 않기 때문에 크기가 작은 컬렉션은 오히려 일반 컬렉션 연산이 더 성능이 나을 수도 있다.
     시퀀스를 통해 성능을 향상 시킬 수 있는 경우는 컬렉션 크기가 큰 경우뿐이다.

    함수를 인라인으로 선언해야 하는 경우
     inline 키워드를 사용해도 람다를 인자로 받는 함수만 성능이 좋아질 가능성이 높다.
     일반 함수 호출의 경우 JVM은 이미 강력하게 인라이닝을 지원한다. 이런 과정은 바이트 코드를 실제 기계어 코드로 번역하는 과정(JIT)에서 일어난다.
     JVM의 최적화 활용시
      바이트코드에서는 각 함수의 구현이 정확히 한번만 있으면 되고, 각 함수를 호출하는 부분에서 따로 함수 코드를 중복할 필요가 없다.
     코틀린 inline 사용시
      바이트코드에서 함수를 호출하는 부분을 함수 본문으로 대치하기 때문에 코드 중복이 생긴다.
     함수를 직접 호출하면 스택 트레이스가 더 깔끔해진다.
     반면 람다를 인자로 받는 함수를 인라이닝하면 이익이 더 많다.
      첫째, 인라이닝을 통해 없앨 수 있는 부가 비용이 상당하다.
       함수 호출 비용을 줄일 수 있을 뿐 아니라 람다를 표현하는 클래스와 람다 인스턴스에 해당하는 객체를 만들 필요도 없어진다.
      둘째, 현재의 JVM은 함수 호출과 람다를 인라이닝해 줄 정도로 똑똑하지는 못하다.
      마지막, 인라이닝을 사용하면 일반 람다에서는 사용할 수 없는 몇 가지 기능을 사용할 수 있다.
       이런 기능 중에는 넌로컬 반환이 있다.
     하지만 인라이닝을 붙일 때 코드 크기에 주의해야 한다. 코드 크기가 큰 걸 호출 지점마다 복사한다면 바이트코드가 전체적으로 아주 커질 수 있다.
     코틀린 표준 라이브러리가 제공하는 inline 함수를 보면 모두 크기가 아주 작다는 사실을 알 수 있을 것이다.


    자원 관리를 위해 인라인된 람다 사용
     람다로 중복을 없앨 수 있는 일반적인 패턴 중 한 가지는 어떤 작업을 하기 전에 자원을 획득하고 작업을 마친 후 자원을 해제하는 자원 관리다.
      자원(resource) - 파일, 락, 데이터베이스, 트랜잭션 등
     자원 관리 패턴을 만들 때 보통 사용하는 방법은 try/finally 문을 사용하되 try 블록을 시작하기 직전에 자원을 획득하고 finally 블록에서 자원을 해제하는 것이다.
     자바 try-with-resource 와 같은 기능을 제공하는 use 라는 함수가 코틀린 표준 라이브러리에있다.
      use 함수는 닫을 수 있는 자원에 대한 확장 함수며, 람다를 인자로 받는다.
      use 함수는 람다를 호출한 다음에 자원을 닫아준다.
       람다가 정상 종료한 경우는 물론 람다 안에서 예외가 발생한 경우에도 자원을 확실히 닫는다.
      use 함수도 인라인함수다. 따라서 use를 사용해도 성능에는 영향이 없다.
     람다의 본문 안에서 사용한 return 은 넌로컬 return 이다.
      이 return 문은 람다가 아니라 람다가 포함되어있는 함수를 끝내면서 값을 반환한다.


    람다 안의 return
     람다 안에서 return을 사용하면 람다를 호출하는 함수가 실행을 끝내고 반환한다.
      람다를 둘러싸고 있는 블록보더 더 바깥에 있는 다른 블록을 반환하게 만드는 return 문을 넌로컬 return 이라 부른다.
     람다를 인자로 받는 함수가 인라인 함수인 경우에만 return이 바깥쪽 함수를 반환시킬 수 있다.
      즉, 인라인되는 경우에는 코드가 그 자리에 그대로 들어가게 되어서 람다 바깥쪽에 return 을 쓴 것과 다르지 않은 코드가 되는 것.
     인라이닝 되지 않는 함수에 전달되는 람다 안에서 return을 사용할 수 없다.
      람다를 변수에 담고 바깥쪽 함수로부터 반환된 뒤에 람다가 호출되면 return이 원치 않은 함수를 반환하게 된다.

    레이블을 사용한 return
     람다 식에도 로컬 return 을 사용할 수 있다.
     람다 안에서 로컬 return 은 for 루프의 break 와 비슷한 역할을 한다.
     로컬 return 은 람다의 실행ㅇ르 끝내고 람다를 호출했던 코드의 실행을 계속 이어간다.
     로컬 return 과 넌로컬 return 구분을 위해 레이블(label)을 사용해야 한다.
      return 으로 실행을 끝내고 싶은 람다 식 앞에 레이블을 붙이고, return 키워드 뒤에 그 레이블을 추가하면 된다.
      람다 식에 레이블을 붙이려면 레이블 이름 뒤에 @문자를 추가한 것을 람다를 여는 { 앞에 넣으면 된다.
      람다로부터 반환하려면 return 키워드 뒤에 @문자와 레이블을 차례로 추가하면된다.
     람다에 레이블을 붙여서 사용하는 대신 람다를 인자로 받는 인라인 함수의 이름을 return 뒤에 사용해도 된다.
     this 식의 레이블에도 마찬가지 규칙이 적용된다.

    넌로컬 반환문은 람다 안의 여러 위치에 return 식이 들어가야하는 경우 불편하다.
    코틀린은 코드 블록을 여기저기 전달하기 위한 다른 해법으로 무명 함수를 제공한다.

    무명함수
     코드 블록을 함수에 넘길 때 사용할 수 있는 다른 방법이다.
     일반 함수와 비슷하지만 함수 이름이나 파라미터 타입을 생략할 수 있다.
     무명 함수도 일반 함수와 같은 반환 타입 지정 규칙을 따른다.
     무명 함수 안에서 레이블이 붙지 않은 return 식은 무명 함수 자체를 반환시킬 뿐 무명함수를 둘러싼 다른 함수를 반환시키지 않는다.
     무명 함수는 일반 함수와 비슷해 보이지만 실제로는 람다 식에 대한 문법적 편의일 뿐이다.


*/
enum class OS { WINDOWS, LINUX, MAC, IOS, ANDROID }
data class SiteVisit(val path: String, val duration: Double, val os: OS)

fun List<SiteVisit>.averageDurationFor(os: OS): Double { // 중복 코드를 별도의 함수로 추출.
    return filter { it.os == os }.map { it.duration }.average()
}

fun List<SiteVisit>.averageDurationFor(predicate: (SiteVisit) -> Boolean): Double {
    return filter(predicate).map { it.duration }.average()
}

data class Person_369(val name: String, val age: Int)

fun readFirstLineFromFile(path: String): String {
    BufferedReader(FileReader(path)).use {br -> // use 는 닫을 수 있는 자원에 사용가능함. 사용이 끝나고 자원을 확실히 닫아준다.
        return br.readLine() // 넌로컬 return
    }
}

fun lookForAlice(people: List<Person_369>) {
    people.forEach lable1@ {
        if (it.name == "Alice") return@lable1
    }
    people.forEach {// 레이블의 이름을 정하지 않았다면 inline 함수의 이름을 레이블로 사용할 수 있다.
        if (it.name=="Alice") return@forEach
    }
    people.forEach(fun(p) { // 무명 함수
        if (p.name == "Alice") return
    })
    println("Alice might be somewhere")
}
fun main() {

    val log = listOf(
            SiteVisit("/", 34.0, OS.WINDOWS),
            SiteVisit("/", 22.0, OS.MAC),
            SiteVisit("/login", 12.0, OS.WINDOWS),
            SiteVisit("/signup", 8.0, OS.IOS),
            SiteVisit("/", 16.3, OS.ANDROID)
    )


    val averageWindowsDuration = log.filter { it.os == OS.WINDOWS } // 컬렉션의 원소를 이터레이션하면서 람다가 true 인 원소만 모은다
            .map { it.duration } // 컬렉션의 각 원소에 주어진 람다를 적용해 새 컬렉션을 만든다.
            .average()
    println(averageWindowsDuration)
    println(log.averageDurationFor(OS.WINDOWS))
    println(log.averageDurationFor { it.os == OS.IOS && it.path=="/signup" })


    val people = listOf(
            Person_369("Alice", 29),
            Person_369("Bob", 31),
            Person_369("Cash", 24),
            Person_369("Duck", 37)
    )

    // 람다로 컬렉션 거르기
    println(people.filter { it.age < 30 })

    // 컬렉션을 직접 거르기
    val result = mutableListOf<Person_369>()
    for (person in people) {
        if (person.age < 30)
            result.add(person)
    }
    println(result)

    lookForAlice(people)

}