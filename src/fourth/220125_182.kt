package fourth

import java.awt.Window
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File


// object 키워드를 다양한 상황에서 사용하지만 모든 경우 "클래스를 정의하면서 동시에 인스턴스(객체)를 생성"한다는 공통점이 있다.
// - 객체 선언은 싱글턴을 정의하는 방법 중 하나이다.
// - 동반 객체는 인스턴스 메소드는 아니지만 어떤 클래스와 관련 있는 메소드와 팩토리 메소드를 담을 때 쓰인다.
// - 객체 식은 무명 내부 클래스 대신 쓰인다.

data class Person(val name: String, val sallary: Int) {

    // 각 인스턴스마다 SalaryComaprator 객체가 생성되는 것은 생기는 것은 아니다.
    // 어떤 클래스의 인스턴스를 비교하는 Comparator를 클래스 내부에 정의하는 게 더 바람직하다.
    object SalaryComparator : Comparator<Person> {
        override fun compare(p1: Person, p2: Person): Int { // p1이 작으면 -1 , p1이 크면 1 , p1p2가 같으면 0 을 반환함
            /*
                급여(salary)를 비교하고 급여가 같다면 이름(name)을 비교
                급여순 -> 이름순
            */
            val result = p1.sallary.compareTo(p2.sallary)
            if (result == 0)
                return p1.name.compareTo(p2.name)
            return p1.sallary.compareTo(p2.sallary)
        }
    }
}


// 객체지향 시스템을 설계하다 보면 인스턴스가 하나만 필요한 클래스가 유용한 경우가 많다.
// 자바에서는 보통 클래스의 생성자를 private으로 제한하고 정적인 필드에 그 클래스의 유일한 객체를 저장하는 싱글턴 패턴을 통해 이를 구현한다.
// 코틀린은 객체 선언 기능을 통해 싱글턴을 언어에서 기본 지원한다.
// '객체 선언'은 클래스를 정의하고 그 클래스의 인스턴스를 만들어서 변수에 저장하는 모든 작업을 단 한 문장으로 처리한다.
// 싱글턴 객체는 객체 선언문이 있는 위치에서 생성자 호출 없이 즉시 만들어진다.
// 객체 선언이 항상 좋은 것은 아니다.
// 객체 생성을 제어할 수 없고 생성자 파라미터를 지정할 수 없으므로 단위 테스트를 하거나 소프트웨어 시스템의 설정이 달라질 때 객체를 대체하거나 객체의 의존관계를 바꿀 수 없다. 그런 기능이 필요하다면 의존관계 주입 프레임워크와 코틀린 클래스를 함께 사용해야 한다.
object Payroll { // Payroll 선언된 이 위치에서 만들어졌음.
    val allEmployees = arrayListOf<Person>()

    fun calculateSallary(): Int {
        var totalSallary = 0
        for (person in allEmployees) {
            totalSallary += person.sallary
        }
        return totalSallary
    }
}

// 클래스 안에 선언된 객체 중 하나에 companion 이라는 특별한 표식을 붙이면 클래스의 동반 객체로 만들 수 있다.
// 동반 객체의 프로퍼티나 메소드의 접근하려면 동반 객체가 정의된 클래스의 이름을 사용한다. 그 결과 자바의 정적 메소드 호출이나 정적 필드를 사용하는 구문과 같아진다.
// private 생성자를 호출하기 좋은 위치가 바로 동반 객체다.
// 동반 객체는 자신을 둘러싼 클래스의 모든 private 멤버에 접근할 수 있다. 따라서 바깥쪽 클래스의 private 생성자도 호출할 수 있다.
// 동반 객체는 팩토리 패턴을 구현하기 가장 적합한 위치다.
class A {
    private val member = "Member"

    companion object { // 동반 객체
        fun bar() {
            val a = A()
            println("Companion object called ${a.member}")
        }
    }
}

// 부 생성자가 2개 있는 클래스를 살펴보고, 다시 그 클래스를 동반 객체 안에서 팩토리 클래스를 정의하는 방식으로 변경해보자.
class User_187 {
    val nickname: String

    constructor(email: String) {
        nickname = email.substringBefore('@')
    }

    constructor(facebookAccountId: Int) {
        nickname = getFacebookName(facebookAccountId)
    }

    private fun getFacebookName(facebookAccountId: Int): String {
        return setOf("Alice", "Bob", "Cash, Duck").random()
    }
}

class User_187_Factory private constructor(val nickname: String) { // 주 생성자 비공개
    companion object { // 동반 객체는 클래스 안에 정의된 일반 객체다. 이름을 붙이거나 인터페이스를 상속하거나 확장함수와 프로퍼티를 정의할 수 있다.
        fun newSubscribingUser(email: String) = User_187_Factory(email.substringBefore('@')) // 비공개 주 생성자 호출
        fun newFacebookUser(accountId: Int) = User_187_Factory(getFacebookName(accountId)) // 비공개 주 생성자 호출

        private fun getFacebookName(facebookAccountId: Int): String {
            return setOf("Alice", "Bob", "Cash", "Duck").random()
        }
    }
}

interface JSONFactory<T> {
    fun fromJSON(jsonText: String): T
}

class Person_190(val name: String) {

    companion object : JSONFactory<Person_190> {
        override fun fromJSON(jsonText: String): Person_190 { // JSON으로부터 각 원소를 다시 만들어내는 추상 팩토리가 있다면 Person 객체를 그 팩토리에게 넘길 수 있다.
            val name = jsonText.substringAfterLast("name : ").substringBefore('}')
            println(name)
            return Person_190(name)
        }
    }
}

fun <T> loadFromJSON(factory: JSONFactory<T>, jsonText: String): T {
    return factory.fromJSON(jsonText)
}

fun Person_190.Companion.extendFunction() { // 동반 객체의 확장 함수
    println("동반 객체 확장 함수 호출")
}

// 무명 객체를 정의할 때도 object 키워드를 사용
// 무명 객체는 자바의 무명 내부 클래스를 대신한다.
// 무명 객체는 싱글턴이 아니므로 쓰일 때마다 새로운 인스턴스가 생성된다.
// 무명 객체에 이름을 붙여야 한다면 변수에 무명 객체를 대입하면 된다.
val listener = object: MouseAdapter() { }

// 객체 식은 무명 객체 안에서 여러 메소드를 오버라이드 해야하는 경우에 훨씬 더 유용하다.
// 메소드가 하나뿐인 인터페이스를 구현해야 한다면 코틀린의 SAM 변환 지원을 활용하는 편이 낫다. (람다 5장에서 자세히)
fun contClicks(window: Window) {
    var clickCount = 0
    window.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent?) {
            clickCount++ // 객체 식 안의 코드는 식이 포함된 함수의 변수에 접근할 수 있다.
        }
        override fun mouseDragged(e: MouseEvent?) { super.mouseDragged(e) }
        override fun mouseEntered(e: MouseEvent?) { super.mouseEntered(e) }
    })
}

fun main(args: Array<String>) {
    Payroll.allEmployees

    val person1 = Person("Alice", 100)
    val person2 = Person("Bob", 200)
    val person3 = Person("Cash", 200)
    val person4 = Person("Duck", 50)
    println("p1 < p2 : ${Person.SalaryComparator.compare(person1, person2)}")
    println("p2 < p1 : ${Person.SalaryComparator.compare(person2, person1)}")
    println("p2 = p3 : ${Person.SalaryComparator.compare(person2, person3)}")

    val persons = listOf<Person>(person1, person2, person3, person4)
    println(persons.sortedWith(Person.SalaryComparator))

    val user1 = User_187("Bob")
    /*
        클래스 이름을 통해 동반 객체의 메소드를 호출할 수 있음.
        목적에 따라 팩토리 메소드 이름을 정할 수 있어 매우 유용하다.
        팩토리 메소드는 그 팩토리가 선언된 클래스의 하위 클래스 객체를 반환할 수도 있다.
        팩토리 메소드는 생성할 필요가 없는 객체를 생성하지 않을 수 있다. (그치 객체를 생성하지 않고 메소드 호출이 가능하니까)

        클래스를 확장해야만 하는 경우에는 동반 객체 멤버를 하위 클래스에서 오버라이드할 수 없으므로 여러 생성자를 사용하는게 더 나은 해법이다. (동반 객체 멤버를 하위에서 못쓰니 동반 객체 멤버로 만들지 말고 여러 생성자 만들어서 하위에서도 쓸 수 있도록 하자)
    */
    val user2 = User_187_Factory.newSubscribingUser("Alice@gmail.com")
    val user3 = User_187_Factory.newFacebookUser(4)
    val user4 = User_187_Factory.Companion.newFacebookUser(2) // 동반 객체의 이름을 지정하지 않으면 자동으로 Companion이 된다.

    println("user1: ${user1.nickname}, user2: ${user2.nickname}, user3: ${user3.nickname}")

    println(loadFromJSON(Person_190, "{name : Alice}").name)
    Person_190.extendFunction() // 동반 객체 확장 함수 호출
}