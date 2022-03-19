package fifth
// 5.1 람다 식과 멤버참조

// 람다(lambda expression)식 또는 람다(lambda)는 기본적으로 다른 함수에 넘길 수 있는 작은 코드 조각을 뜻한다.
// 람다를 사용하면 쉽게 공통 코드 구조를 라이브러리 함수로 뽑아낼 수 있다.
// 람다를 자주 사용하는 경우로 컬렉션 처리를 들 수 있다.
// 람다를 메소드가 하나뿐인 무명 객체 대신 사용할 수 있다.
// 수신 객체 지정 람다(lambda with receiver)는 특별한 람다로, 람다 선언을 둘러싸고 있는 환경과는 다른 상황에서 람다 본문을 실행할 수 있다.

// 코드에서 중복을 제거하는 것은 프로그래밍 스타일을 개선하는 중요한 방법 중 하나이다.
data class Person_200(val name: String, val age: Int)

// 사람들로 이뤄진 리스트가 있고 그중에 가장 연장자를 찾고 싶다. 람다를 사용해본 경험이 없다면 루프를 써서 구현할 것이다.
// 이 루프에는 많은 코드가 들어가며, 작성 중 실수를 저지르기 쉽다. 예를 들어 비교 연산자를 잘못쓰면 최소 값을 구하게 된다.
fun findTheOldest_200(people: List<Person_200>) {
    var maxAge = 0
    var theOldest: Person_200? = null
    for (person in people) {
        if (person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }
    println(theOldest)
}

// 람다는 값처럼 여기저기 전달할 수 있는 "동작"의 모음이다.
// 람다를 변수에 저장할 수도 있지만 함수에 인자로 넘기면서 바로 람다를 정의하는 경우가 대부분이다.

// 람다 식은 항상 중괄호로 둘러싸여 있다.
// 인자 목록 주변에 중괄호가 없다는 사실을 기억하라. 화살표(->)가 인자 목록과 람다 본문을 구분해준다.
// 람다 식을 변수에 저장할 수 있고, 변수를 다른 일반 함수와 마찬가지로 다룰 수 있다. sum_202(1, 2)
val sum_202= { x: Int, y: Int/*인자 목록*/ -> /*본문*/ x + y}

fun originA(a: Int , b: (Int) /*인자 목록*/ -> /*본문*/ Int) { // b 파라미터의 값으로는 사용할 함수의 내용을 적어서 보내면 되는 것. 예시 { a(인자목록) -> a*a*a(본문) } 이러면 b(5)를 쓰면 본문의 5*5*5값이 실행되어 반환되는 것.
    println(a+b(50))
}


// 변수 포획 - 람다를 함수 안에서 정의하면 함수의 파라미터뿐 아니라 람다 정의의 앞에 선언된 로컬 변수까지 람다에서 모두 사용할 수 있다.
fun printMessagesWithPrefix(messages: Collection<String>, prefix: String) {
    var localVariable = "Local"
    messages.forEach { // 자바와 달리 코틀린 람다 안에서는 final 변수가 아닌 변수에 접근할 수 있다.
        // 람다 안에서 사용하는 외부 변수를 '람다가 포획(capture)한 변수' 라고 부른다.
        println("$prefix $it $localVariable") // 함수의 파라미터 prefix와 함수의 로컬 변수도 함수안에서 쓰인 람다 안에서 사용 수 있음
        localVariable = it[0].toString() // 람다 안에서 람다 밖의 변수를 변경할 수 있다.
    }
}

// 어떤 함수가 자신의 로컬 변수를 포획한 람다를 반환하거나 다른 변수에 저장한다면 로컬 변수의 생명주기와 함수의 생명주기가 달라질 수 있다.
// 파이널 변수를 포획한 경우에는 람다 코드를 변수 값과 함께 저장한다.
// 파이널이 아닌 변수를 포획한 경우에는 변수를 특별한 래퍼로 감싸서 나중에 변경하거나 읽을 수 있게 한 다음, 래퍼에 대한 참조를 람다 코드와 함께 저장한다.
// 자바에서는 파이널 변수만 포획할 수 있다. 하지만 교묘한 속임수를 통해 변경 가능한 변수를 필드로 하는 클래스를 선언하는 것으로 포획할 수 있다.
class Ref_209<T>(var value: T) // 변경 가능한 변수를 포획하는 방법을 보여주기 위한 클래스

// 한가지 꼭 알아둬야 할 함정이 있다.
// 람다를 이벤트 핸들러나 다른 비동기적으로 실행되는 코드로 활용하는 경우 함수 호출이 끝난 다음에 로컬 변수가 변경될 수도 있다. (_210)

// 코틀린에서는 자바8과 마찬가지로 함수를 값으로 바꿀 수 있다. 이때 이중 콜론(::)을 사용한다. ::를 사용하는 식을 멤버 참조(member reference)라고 부른다.
// 멤버 참조는 프로퍼티나 메소드를 단 하나만 호출하는 함수 값을 만들어준다. ::는 클래스 이름과 참조하려는 멤버이름 사이에 위치한다.
// 최상위에 선언된 함수나 프로퍼티를 참조할 수도 있다. 클래스 이름을 생략하고 ::로 참조를 바로 시작한다.

// 생성자 참조(constructor reference)를 사용하면 클래스 생성 작업을 연기하거나 저장해둘 수 있다. ::뒤에 클래스 이름을 넣으면 생성자 참조를 만들 수 있다.
data class Person_212(val name: String, val age: Int)
fun Person_212.isAdult() = age >= 20

fun main(args: Array<String>) {
    val people = listOf(
            Person_200("Alice", 22),
            Person_200("Bob", 20),
            Person_200("Cash", 28),
            Person_200("Duck", 26),
            Person_200("Echo", 33)
    )

    findTheOldest_200(people)

    // 코틀린에서는 더 좋은 방법이 있다. 라이브러리 함수를 쓰면 된다.
    // 모든 컬렉션에 대해 maxBy를 호출할 수 있다.
    // maxBy는 '가장 큰 원소'를 찾기 위해 "비교에 사용할 값을 돌려주는 함수"를 인자로 받는다. 즉, { it.age } 는 비교에 사용할 값을 돌려주는 함수다.
    // 컬렉션의 원소(Person)를 인자(it이 인자를 가리킨다)로 받아 비교에 사용할 값을 반환한다.
    // 나이 프로퍼티를 비교해서 값이 갖아 큰 원소 찾기
    println("maxBy결과 : ${people.maxBy { it.age }}") // people의 원소가 반환된다.
    // 위의 코드와 같은 일을 한다. 이 방법은 멤버 참조를 사용한 것이다.
    // 람다나 멤버참조를 인자로 받는 함수를 통해 개선한 코드는 더 짧고 더 이해하기 쉽다.
    println(people.maxBy(Person_200::age))
    println(sum_202(100, 200)) // 변수에 저장된 람다가 호출된다.
    val separator = null
    // 원한다면 람다 식을 직접 호출해도 된다.
    { println(1+2) }()
    println( {1}() + 2 )
    // 하지만 이런 구문은 읽기 어렵고 그다지 쓸모도 없다. 굳이 람다를 만들자마자 호출하느니 람다 본문을 직접 실행하는 편이 낫다.
    // 이렇게 코드의 일부분을 블록으로 둘러싸 실행할 필요가 있다면 run을 사용하자.
    // run은 인자로 받은 람다를 실행해주는 라이브러리 함수다.
    run { println(1+2) }
    println( run { 1 } + 2 )
    // 실행 시점에 코틀린 람다 호출에는 아무 부가 비용이 들지 않으며, 프로그램의 기본 구성요소와 비슷한 성능을 낸다.
    // 코틀린이 줄여쓸 수 있게 제공했던 기능을 제거하고 정식으로 람다를 작성해보자.
    // 중괄호 안에 있는 코드는 람다 식이고, 람다 식을 maxBy 함수에 넘긴다. 람다 식은 Person 타입을 인자로 받아서 인자의 age를 반환한다.
    // 하지만 이 코드는 구분자가 많아 가독성이 떨어진다.
    // 함수 호출 시 맨 뒤에 있는 인자가 람다 식이라면 그 람다를 괄호 밖으로 뺄 수 있는 '문법 관습'이 있다.
    // 람다가 어떤 함수의 유일한 인자이고 괄호 뒤에 람다를 썼다면 호출 시 빈 괄호를 없애도 된다.
    people.maxBy( {p: Person_200 -> p.age} ) // 람다 정식 코드
    people.maxBy() {p: Person_200->p.age} // 문법 관습
    people.maxBy {p: Person_200 -> p.age} // 람다가 유일한 인자이며, 괄호 뒤에 람다를 쓰는 경우 빈 괄호 생략.
    people.maxBy {p -> p.age} // 람다 파라미터의 타입도 추론할 수 있다. maxBy의 경우 항상 컬렉션 원소 타입과 같다.
    people.maxBy { it.age } // 람다의 디폴트 파라미터 이름은 it 이다. it은 자동 생성된 파라미터의 이름이다.

    originA(50, {a: Int -> a*a} )
    originA(50, {a -> a*a} )
    originA(50) { a -> a*a}

    printMessagesWithPrefix(arrayListOf<String>("Alice", "Bob", "Cash", "Duck"), " / " )

    val counter = Ref_209(0)
    val inc = {counter.value++}
    // 실제 코드에서는 이런 래퍼를 만들지 않아도 됨.
    // 람다가 파이널 변수(val)를 포획하면 자바와 마찬가지로 변수의 값이 복사되고, 변경 가능한 변수(var)를 포획하면 Ref클래스 넣는다.
    var counter2 = 0
    val inc2 = { counter2++ } // 이 코드의 동작을 보여주는게 위의 코드이다.

    val getAge = { person: Person_200 -> person.age }
    val getAge2 = Person_200::age // 람다 식을 더 간략하게 표현한 것이다.
    // 멤버 참조는 그 멤버를 호출하는 람다와 같은 타입이다.
    println(getAge.javaClass)
    println(getAge2.javaClass)

    val createPerson = ::Person_212
    val p = createPerson("Alice", 29)
    println(p)
    // 확장 함수도 멤버 함수와 똑같은 방식으로 참조할 수 있다.
    val predicate = Person_212::isAdult

    // 코틀린 1.0에서는 클래스의 메소드나 프로퍼티에 대한 참조를 얻은 다음에 그 참조를 호출할 때 항상 인스턴스 객체를 제공해야 했다.
    val personAgeFunction = Person_212::age
    println(personAgeFunction(p))

    // 바운드 멤버 참조를 사용하면 멤버 참조를 생성할 때 클래스 인스턴스를 함께 저장한 다음 나중에 그 인스턴스에 대한 멤버를 호출해준다. (코틀린 1.1부터)
    val dmitryAgeFunction = p::age
    println(dmitryAgeFunction())
}