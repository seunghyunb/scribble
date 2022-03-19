package fifth

import kotlin.math.pow

// 컬렉션 함수형 API

// 함수형 프로그래밍에서는 람다나 다른 함수를 인자로 받거나 함수를 반환하는 함수를 고차 함수(HOF, High Order Function)라고 부른다.
// 고차 함수는 기본 함수를 조합해서 새로운 연산을 정의하거나, 다른 고차 함수를 통해 조합된 함수를 또 조합해서 더 복잡한 연산을 쉽게 정의할 수 있다는 장점이 있다.
// 고차 함수와 단순한 함수를 이리저리 조합해서 코드를 작성하는 기법을 콤비네이터 패턴(combinator pattern)이라 부른다.
// 콤비네이터 패턴에서 복잡한 연산을 만들기 위해 값이나 함수를 조합할 때 사용하는 고차 함수를 콤비네이터(combinator)라고 부른다.
// (사견) 쓰임에 따라 이름이 다른 것이다. 넓은 범위로 보면 함수다.

// filter 함수는 컬렉션을 이터레이션 하면서 각 원소를 주어진 람다에 넘겨서 람다가 true를 반환하는 원소만 모은다. (조건에 맞는 원소만 모인 컬렉션이 만들어진다.)
// filter 는 켈렉션에서 원치 않는 원소를 제거한다(=원하는 원소만 남겨둔다). 하지만 원소를 변환할 수는 없다.

// map 함수는 컬렉션의 각 원소에 주어진 람다를 적용한 결과를 모아서 새 컬렉션을 만든다.

// Map컬렉션은 Key, Value 처리하는 함수가 따로 존재한다. filterKeys, filterValues 와 mapKeys, mapValues 가 있다.

// all은 컬렉션의 모든 원소가 어떤 조건을 만족하는지 판단한다.
// any는 컬렉션안에 조건을 만족하는 원소가 있는지 판단한다.
// count 컬렉션안에 조건을 만족하는 원소의 개수를 반환한다.
// find는 조건을 만족하는 첫 번째 원소를 반환하고, 만족하는 원소가 없는 경우 null 을 반환한다. (firstOrNull 과 같다.)
// all, any, count, find 는 컬렉션을 만들어내지 않는다.

val canBeInClub27 = {p: Person_212 /*인자*/ -> /*본문*/ p.age <= 27}

fun main(args: Array<String>) {

    val list = listOf(1, 2, 3, 4)
    println(list.filter{ it % 2 == 0 })
    println(list.map { it*it })
    println(list.map { it.toDouble().pow(5).toInt() })

    val people = listOf(Person_212("Alice", 26), Person_212("Bob", 31), Person_212("Ace", 31))
    // 사람의 리스트를 이름의 리스트로 변환할 수 있다.
    println(people.map { it.name })  // 람다를 각 원소에 적용시키면 사람("Alice", 29) -> "Alice"  이렇게 된다.
    println(people.map(Person_212::name)) // 멤버 참조를 사용해 작성할 수도 있다.
    // 이런 호출은 연계할 수 있다.
    println(people.filter { it.age > 30 }.map(Person_212::name) ) // 나이가 30보다 많은 사람의 이름으로 된 리스트(people은 리스트)
    // 가장 나이 많은 사람을 찾자. -> 가장 나이 많은 사람은 한명이 아닐 수 있다. 같은 나이가 여러 명일 수 있다. 가장 ~~한 이 한명일 거란 생각 NO
    println(people.filter { it.age == people.maxBy(Person_212::age)!!.age }) // people.maxBy()는 Person객체를 반환하니까 Person의 age값과 비교해야한다.
    // 위 코드는 100번 작업하면 100번 최댓값 연산을 수행한다(비효율).
    // 꼭 필요하지 않은 경우 굳이 계산을 반복하지 말자!
    // 항상 작성한 코드로 인해 어떤 일이 벌어질지 명확히 이해해야 한다.
    val maxAge = people.maxBy { it.age }!!.age
    println(people.filter { it.age == maxAge })

    println(people.all(canBeInClub27))
    println(people.any(canBeInClub27))
    println(people.count(canBeInClub27))
    println(people.find(canBeInClub27))
    println(people.map{
        if(it.age > 27) {
            Person_212(it.name, 27)
        } else {
            it
        }
    }.find(canBeInClub27))
}