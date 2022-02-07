package fourth

/* backing field 접근하기 */
class User(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            println("""
                Address was change for $name: "$field" -> "$value".
            """.trimIndent())
            field= value // 접근자의 본문에서 field 라는 특별한 식별자를 통해 backing field에 접근할 수 있다.
        }
}

class LengthCounter {
    var counter: Int = 0
        private set // 접근자의 가시성을 바꿀 수 있다. 이런식으로 프로퍼티자체가 private이 되는게 아닌 set 만 private으로 바꿔 클래스 안에서만 값을 변경하도록 바꿨다.

    fun addWord(word: String) {
        counter += word.length
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is LengthCounter)
            return false
        return counter == other.counter
    }

    override fun hashCode(): Int {
//        return super.hashCode()
        return counter.hashCode() * 31 + counter
    }
}


// 필요한 메소드를 컴파일러가 자동으로 만들어준다.
// equals, hashCode, toString, copy 등 (7.4절에서 나머지 메소드를 알아보자)
data class Client(val name: String, val postalCode: Int)


// 인터페이스를 구현할 때 by 키워드를 통해 그 인터페이스에 대한 구현을 다른 객체에 위임 중이라는 사실을 명시할 수 있다.
// 구현을 innerList에 위임함. 구현해야하는 것들은 ArrayList의 방식으로 구현될 것이다.
// ArrayList에만 있는 것들까지 구현되었을까? --> Collection 인터페이스를 구현하는 것이기 때문에 ArrayList에만 있는 것들은 구현되지 않는다.
// 위임은 인터페이스만 가능하다. 클래스는 안됨.
class DelegatingCollection<T> (
        innerList: Collection<T> = ArrayList<T>()
): Collection<T> by innerList {
//    override val size: Int get() = 10
}

fun main(args: Array<String>) {
    val user = User("Alice")
    user.address = "Seoul"

    val lengthcounter = LengthCounter()
    val lengthcounter2 = LengthCounter()
    lengthcounter.addWord("Kotlin")
    lengthcounter2.addWord("Kotlin")

    println(lengthcounter.counter)

    println(lengthcounter == lengthcounter2)
    println("${lengthcounter.hashCode()} // ${lengthcounter2.hashCode()}") // "1642534850 // 1724731843" hashCode를 오버라이드 하기 전에는 다른 값이 나온다.

    val processed = hashSetOf(lengthcounter)
    println(processed.contains(lengthcounter2)) // HashSet은 원소를 비교할 때 비용을 줄이기 위해 먼저 객체의 해시 코드를 비교하고 해시 코드가 같은 경우에만 실제 값을 비교한다.

    // 데이터 클래스 인스턴스를 불변 객체로 더 쉽게 활용할 수 있게 copy 메소드를 제공한다.
    // 복사본은 원본과 다른 생명주기를 가지며, 일부 프로퍼티 값을 바꾸거나 복사본을 제거해도 프로그램에서 원본을 참조하는 다른 부분에 전형 영향을 끼치지 않는다.
    // 객체를 복사하면서 일부 프로퍼티를 바꿀 수 있다.
    val client = Client("Kotlin", 4000)
    println(client.copy())
    println(client.copy(postalCode = 5000))
    println(client == client.copy())
    println(client === client.copy())
    println(client.copy() == client.copy())
    println(client.copy() === client.copy())

    val delegatingCollection = DelegatingCollection<Int>()
    println(delegatingCollection.size)
}