package fifth

import fourth.Person_190

/*
    groupBy
     리스트를 여러 그룹으로 이뤄진 맵으로 변경.
     원소를 어떤 특성에 따라 여러 그룹으로 나누고 싶을 때 쓴다.
      특성 파라미터로 전달하면 자동으로 구분해준다.
     결과 타입은 Map<특성, 그룹이 모인 값(리스트)> 이다.
      구분 짓는 특성이 key 가 되고 key 값에 따라 각 각 그룹이 값인 맵이다.
      각 그룹은 List 이다.

    flatMap  (flat: 평평한) (map: 컬렉션 함수 map 처럼 동작한다 ) (즉, 기존의 컬렉션 함수 map 은 하나의 리스트로 합쳐주는 것까지는 해주지 않았으나 flatMap 은 하나의 리스트로 만드는 것까지 해준다)
     인자로 주어진 람다를 컬렉션의 모든 객체에 적용하고(또는 매핑하기 map) 람다를 적용한 결과로 얻어지는 여러 리스트를 한 리스트로 한데 모은다(또는 펼치기 flatten).
     리스트의 리스트가 있는데 중첩된 리스트의 모든 원소를 한 리스트로 모아야 한다면 flatMap 을 이용할 수 있다.

     동작 순서
      1. 주어진 람다를 컬렉션의 모든 원소에 적용한다.
      2. 각 원소마다 적용된 결과는 리스트로 반환된다. (즉 원소가 4개면 4개의 리스트가 생긴다)
      3. 각각의 리스트들을 모아서 하나의 리스트로 만든다. (즉, flat 평평하게 하나의 리스트로 만들게 된다)

      flatten
       특별히 변환(map) 해야하는 내용이 없고, 리스트의 리스트를 평평하게 펼치기만 할 경우 flatten 을 사용하면 된다.
*/

/*
    지연 계산(lazy) 컬렉션 연산
     컬렉션 함수는 결과 컬렉션을 즉시 생성한다. 컬렉션 함수를 연쇄하면 매 단계마다 중간 결과를 새로운 컬렉션에 임시로 담는다(낭비).
      Sequence 시퀀스를 사용하면 중간 임시 컬렉션을 사용하지 않고도 컬렉션을 연쇄할 수 있다.
      각 연산이 컬렉션을 직접 사용하는 대신 시퀀스를 사용하게 만들면 중간 결과를 저장하는 컬렉션이 생기지 않아 원소가 많은 경우 성능이 눈에 띄게 좋아진다.

     코틀린 지연 계산 시퀀스는 Sequence 인터페이스에서 시작한다.
      이 인터페이스는 단지 한 번에 하나씩 열거될 수 있는 원소의 시퀀스를 표현할 뿐이다.
      시퀀스 안에는 iterator 라는 단 하나의 메소드가 있다. 그 메소드를 통해 시퀀스로부터 원소 값을 얻을 수 있다.
      시퀀스의 원소는 필요할 때 비로소 계산된다. 따라서 중간 처리 결과를 저장하지 않고도 연산을 연쇄적으로 적용해서 효율적으로 계산을 수행할 수 있다.
      asSequence() 확장 함수를 호출하면 어떤 컬렉션이든 시퀀스로 바꿀 수 있다.
      시퀀스를 다시 리스트로 만들 때는 toList() 를 사용한다.

     시퀀스 연산
      시퀀스 연산에는 중간(intermediate) 연산과 최종(terminal) 연산으로 나뉜다.
       중간 연산
        중간 연산은 다른 시퀀스를 반환한다.
        중간 연산은 항상 지연 계산된다.
         지연 계산이란 코드를 실행해서 결과를 얻을 필요가 있을 때 연산이 적용된다.

       최종 연산
        최종 연산은 결과를 반환한다.
         결과는 최초 컬렉션에 대해 변환을 적용한 시퀀스로부터 일련의 계산을 수행해 얻을 수 있는 컬렉션이나 원소, 숫자 또는 객체다.
        최종 연산을 호출하면 지연됐던 모든 계산이 수행된다.
      직접 연산을 할 때와 연산 수행 순서가 다르다. people.map{}.filter{} 경우
       직접 연산은 작성한 연산의 순서대로 동작한다. map 연산 후 filter 연산.
      시퀀스는 연산이 모든 연산이 각 원소에 순차적으로 적용된다. 원소1 map filter 원소2 map filter 이렇게 연산이 모든 원소에 적용된다.
       그래서 연산을 적용하다 결과가 얻어지면 반환이 이뤄지는 연산 find와 같은 연산을 사용할 때는 주의해야한다.
        people.map{}.find{} 같은 경우 find는 조건에 맞는 원소를 찾는 순간 연산이 끝나기 때문에 이 후의 원소들은 map이 제대로 적용되지 않을 수 있다.
       그리고 연산의 순서에 따라서 수행하는 연산의 횟수가 달라질 수도 있다.
        .map{}.filter{} 의 경우 원소가 변한 후 filter가 이뤄지는 반면, .filter{}.map{} 이라면 filter가 이뤄진 원소에 대해서만 원소가 변한다.
      자바8 스트림과 시퀀스의 개념은 같다. 그런데 코틀린에서 같은 개념을 따로 제공해 구현하는 이유는 안드로이드 등 예전 자바를 사용하는 경우 자바8 스트림이 없기 때문이다.

    generateSequence
     시퀀스를 만드는 방법 중 하나다.
     시작 값(seed)와 함수로 정의된 시퀀스를 반환한다.
     이 시퀀스는 첫 번째 'null'값이 될 때까지 값을 생성한다.
     시작 값(seed) 가 null 이면 빈 시퀀스가 생성된다.
     시퀀스는 seed로 시작할 때마다 여러 번 반복할 수 있다.

    takeWhileSequence
     takeWhileSequence 조건이 true 인 동안 기본 시퀀스에서 값을 반환한다.
*/
fun main(args: Array<String>) {

    val people = listOf(
            Person_212("Alice", 31),
            Person_212("Bob", 29),
            Person_212("Cash", 31),
            Person_212("Duck", 28)
    )
    // groupBy 의 결과는 person.age 가 키가 되고 각 그룹의 값은 Person_212 가 된다.
    // 즉, Map<Int, List<Person_212> 타입을 반환한다.

    println(people.groupBy({ person: Person_212 ->
        person.age
    }))

    println(people.groupBy { it.age })

    val result = people.groupBy { it.age }

    val ss = result.flatMap { it.key.until(29) }
    println(ss)


    val strings = listOf( listOf("abc", "def", "efgh"), listOf("ㄱㄴㄷ", "ㄹㅁㅂ", "ㅅㅇㅈ"))
    println(strings.flatten())

    lazy_223(people)
}

fun lazy_223(people: List<Person_212>) {
    people.asSequence()
            .map(Person_212::name)
            .filter { it.startsWith("A") }
            .toList()
}

fun sequnece_228() {
    val naturalNumbers = generateSequence(0) { it+1 } // 시작 원소를 0으로 설정하고 다음 원소를 계산하는 방법(시작원소에 +1)을 제공함.
    // takeWhileSequence
    val numbersTo100 = naturalNumbers.takeWhile { num -> num <= 100 } // 100보다 작은 수인 동안 기본 시퀀스(naturalNumbers)에서 값 반환.
    // 최종연산을 수행하기 전까지는 시퀀스의 각 숫자는 계산되지 않는다.
    // sum()이 최종연산이다.
    val result = numbersTo100.sum()
}
