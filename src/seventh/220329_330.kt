package seventh

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


/*
    convention 관례
     어떤 언어 기능과 미리 정해진 이름의 함수를 연결해주는 기법
      예를 들어 + 연산을 어떤 클래스 안에 plus 라는 함수를 정의하여 클래스의 인스턴스에 대해 + 연산을 사용할 수 있다.

    코틀린에서 함수 이름을 통한 관례(convention)를 채택한 이유는 기존 자바 클래스를 코틀린 언어에 적용하기 위함이다.
     기존 자바 클래스가 구현하는 인터페이스는 이미 고정돼 있고 코틀린 쪽에서 자바 클래스가 새로운 인터페이스를 구현하도록 만들 수는 없다.
     따라서 기존 자바 클래스에 대해 확장 함수를 구현하면서 관례에 따라 이름을 붙이면 기존 자바 코드를 바꾸지 않아도 새로운 기능을 쉽게 부여할 수 있다.

    연산자를 오버로딩하는 함수 앞에는 operator 키워드를 붙인다. operator 를 붙임으로써 어떤 함수가 관례를 따르는 함수임을 명확히 알 수 있다.
     operator 없이 관례에 사용하는 함수의 이름을 사용할 경우 오류가 생겨 이름을 변경해줘야 한다.

    연산자를 멤버 함수로 만드는 대신 확장 함수로 만들 수도 있다.

    코틀린에서는 프로그래머가 직접 연산자를 만들어 사용할 수 없고, 언어에서 미리 정해둔 연산자만 오버로딩할 수 있으며, 관례에 따르기 위해 클래스에서 정의해야 하는 이름이 연산자별로 정해져 있다.

    연산자 정의시 두 피연산자가 같은 타입일 필요는 없다.
    자동으로 교환 법칙을 지원하지 않음에 유의해야 한다.
    연산자의 반환 타입이 꼭 두 피연산자 중 하나와 일치해야만 하는 것도 아니다.
    비트 연산자에 대해 특별한 연산자 함수를 사용하지 않는다.

    복합 대입 연산자
     plus 와 같은 연산자를 오버로딩하면 코틀린은 + 연산자 뿐 아니라 그와 관련 있는 연산자인 += 도 자동으로 함께 지원한다.

    이론적으로 += 을 plus 와 plusAssign 양쪽으로 컴파일할 수 있다.
     그러나 plus 와 plusAssign 모두 정의하고 +=을 사용할 경우 컴파일러는 오류를 보고한다.
     일반적으로 새로운 클래스를 일관성있게 설계하는게 가장 좋다.
      코틀린 표준 라이브러리는 컬렉션에 대해 두 가지 접근 방법을 함께 제공한다.
      +와 -는 항상 새로운 컬렉션을 반환하며,
      += 와 -= 연산자는 항상 변경 가능한 컬렉션에 작용해 메모리에 있는 객체의 상태를 변화시킨다.
      또한 읽기 전용 컬렉션에서 += 과 -=은 변경을 적용한 복사본을 반환한다.

    단항 연산자를 오버로딩하는 절차도 이항 연산자와 마찬가지다.

    동등성 연산자 equals
    코틀린은 == 연산자 호출을 equals 메소드 호출로 컴파일 한다.
     != 연산자를 호출하는 식도 equals 호출로 컴파일된다.
     == 과 != 은 내부에서 인자가 널인지 검사하므로 널이 될수 있는 값에도 적용가능
    식별자 비교 연산자 ===
     자바 == 연산자와 같다. ===는 자시느이 두 피연산자가 서로 같은 객체를 가리키는지 비교한다. 원시 타입의 경우 두 값이 같은지 비교한다.
     ===를 오버로딩할 수는 없다는 사실을 기억하자ㅏ.
     equals 함수에는 override 가 붙어있다. 다른 연산자 오버로딩과 달리 Any에 정의된 메소드이므로 override 가 필요하다.
     Any의 equals 에는 operator 가 붙었지만 하위 클래스에서 오버라이드 하는 메소드 앞에는 operator를 붙이지 않아도 자동으로 상위 클래스의 oeprator 지정이 적용된다. 또한 Any에서 상속받은 equals 가 확장 함수보다 우선 순위가 높기 때문에 equals를 확장 함수로 정의할 수 없다.

    순서 연산자 compareTo
     자바에는 < 나 > 등의 연산자로는 원시 타입의 값만 비교할 수 있다.
     다른 모든 타입의 값에는 element1.compareTo(element2) 를 명시적으로 사용해야 한다.
     코틀린은 compareTo 메소드를 호출하는 관례(convention)를 제공한다.
     따라서 <, >, <=, >= 은 compareTo 호출로 컴파일된다.
     compareTo 가 반환하는 값은 Int 다.
     a >= b 는 a.compareTo(b) < 0 으로 컴파일된다.

     코틀린 표준 라이브러리의 compareValuesBy는 두 객체와 여러 비교 함수를 인자로 받는다.
      첫 번째 비교함수에 두 객체를 넘겨서 두 객체가 같지 않다는 결과가 나오면 그 결과 값을 즉시 반환하고, 두 객체가 같다는 결과(0)가 나오면 두 번째 비교 함수를 통해 두 객체를 비교한다.
      각 비교함수는 람다나 프로퍼티/메소드 참조일 수 있다. 그렇지만 필드를 직접 비교하면 코드는 조금 더 복잡해지지만 비교 속도는 훨씬 빨라진다는 사실을 기억하자.
       언제나 그렇듯이 처음에는 성능에 신경 쓰지 말고 이해하기 쉽고 간결하게 코드를 작성하고, 나중에 그 코드가 자주 호출됨에 따라 성능이 문제가 되면 성능을 개선하라.

    위임 프로퍼티 delegated property
     코틀린이 제공하는 관례에 의존하는 특성 중에 독특하면서 강력한 기능.
     값을 뒷받침하는 필드에 단순히 저장하는 것보다 더 복잡한 방식으로 작동하는 프로퍼티를 쉽게 구현할 수 있다.
      그 과정에서 접근자 로직을 매번 재구현할 필요도 없다.
      예를 들어, 프로퍼티는 위임을 사용해 자신의 값을 필드가 아니라 데이터 베이스 테이블이나 브라우저 세션, 맵 등에 저장할 수 있다.
     이런 특성의 기반에는 '위임'이 있다.
      위임은 객체가 직접 작업을 수행하지 않고 다른 도우미 객체가 그 작업을 처리하게 맡기는 디자인 패턴을 말한다.
       작업을 처리하는 도우미 객체를 위임 객체(delegate)라고 부른다.
     프로퍼티 위임 관례를 따르는 Delegate 클래스는 operation fun getValue 와 operation fun setValue 메소드를 제공해야한다.
     관례를 사용하는 다른 경우와 마찬가지로 getValue 와 setValue 는 멤버 메소드이거나 확장 함수일 수 있다.
     by 키워드는 프로퍼티와 위임 객체를 연결한다.

    by lazy() 를 사용한 프로퍼티 초기화 지연
     지연 초기화(lazy initialization)는 객체의 일부분을 초기화하지 않고 남겨뒀다가 실제로 그 부분의 값이 필요한 경우 초기화할 때 흔히 쓰이는 패턴이다.
     초기화 과정에서 자원을 많이 사용하거나 객체를 사용할 때마다 초기화하지 않아도 되는 프로퍼티에 대해 지연 초기화 패턴을 사용할 수 있다.

     위임 프로퍼티는 데이터를 저장할 때 쓰이는 뒷받침하는 프로퍼티와 값이 오직 한 번만 초기화됨을 보장하는 게터 로직을 함께 캡슐화해준다.
      위임 객체를 반환하는 표준 라이브러리 함수가 바로 lazy 다.
     lazy 함수는 코틀린 관례에 맞는 시그니처에 getValue 메소드가 들어있는 객체를 반환한다.
      따라서 lazy 를 by 키워드와 함께 사용해 위임 프로퍼티를 만들 수 있다.
      lazy 함수의 인자는 값을 초기화할 때 호출할 람다다.

    위임 프로퍼티 컴파일 규칙: 위임 프로퍼티가 어떤 방식으로 동작하는지 정리해보자.
     컴파일러는 위임 객체를 감춰진 프로퍼티에 저장하며 그 감춰진 프로퍼티를 <delegate> 라는 이름로 부른다.
     또한 컴파일러는 프로퍼티를 표현하기 위해 KProperty 타입의 객체를 사용하며 이 객체를 <property> 라고 부른다.
     컴파일러는 모든 프로퍼티 접근자 안에 getValue 와 setValue 호출 코드를 생성해준다.
     이 매커니즘은 상당히 단순하지만 상당히 흥미로운 활용법이 많다.
      프로퍼티 값이 저장될 장소를 바꿀 수 있고(맵, 데이터베이스 테이블, 사용자 세션의 쿠키 등),
      프로퍼티를 읽거나 쓸 때 벌어질 일을 변경할 수도 있다(값 검증, 변경 통지 등).
      이 모두를 간결한 코드로 달성할 수 있다.

    프로퍼티 값을 맵에 저장
     자신의 프로퍼티를 동적으로 정의할 수 있는 객체를 만들 때 위임 프로퍼티를 활용하는 경우가 자주 있다.
      그런 객체를 확장 가능한 객체(expando object)라고 부르기도 한다.
     표준 라이브러리가 Map 과 MutableMap 인터페이스에 대해 getValue 와 setValue 확장 함수를 제공한다.
      name by Map 인 경우 p.name 은 Map.getValue(p, prop) 를 호출하고 이는 Map[prop.name] 을 구현한다.

    프레임워크에서 위임 프로퍼티 활용
     객체 프로퍼티를 저장하거나 변경하는 방법을 바꿀 수 있으면 프레임워크를 개발할 때 유용하다.
*/

/*
    검색
    1. 디자인 패턴 : 위임
*/

class Delegate {
    operator fun getValue(a:Int, property: KProperty<*>) {/* 게터를 구현하는 로직을 담는다. */}
    operator fun setValue(a: Int, property: KProperty<*>, c: Int) { /* 세터를 구현하는 로직을 담는다. */  }
}
/*
class Foo {
    var p: Type by Delegate() // by 키워드는 프로퍼티와 위임 객체를 연결한다.
     /*
        p 는 접근자 로직을 다른 객체에게 위임한다. 접근자: get, set
        Delegate() 는 위임 객체(delegate).
        by 뒤의 식을 계산해서 위임에 쓰일 객체를 얻는다.
    */
    private val deleage = Delegate()
    var p: Type
     set(value: Type) = deleage.setValue()
     get() = delegate.getValue()
}
*/

open class PropertyChangeAware {
    protected val changeSupport = PropertyChangeSupport(this)

    fun addPropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.addPropertyChangeListener(listener)
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.removePropertyChangeListener(listener)
    }
}

class Person_335(val name: String, age: Int, salary: Int): PropertyChangeAware() {
    var age: Int  = age
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange("age", oldValue, newValue) // 프로퍼티 변경을 리스너에게 통지
        }
    var salary: Int = salary
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange("salary", oldValue, newValue)
        }
}

class ObservableProperty(var propValue: Int, val changeSupport: PropertyChangeSupport) {
    /*
    fun getValue(): Int = propValue
    fun setValue(newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(propName, oldValue, newValue)
    }
    */

    /*
        코틀린 관례에 사용하는 다른 함수와 마찬가지로 operator 변경자가 붙는다.
        getValue 와 setValue 는 프로퍼티가 포함된 객체 와 프로퍼티를 표현하는 객체를 파라미터로 받는다.
         코틀린은 KProperty 타입의 객체를 사용해 프로퍼티를 표현한다. (KProperty는 10.2절에서)
          KProperty.name 을 통해 메소드가 처리할 프로퍼티의 이름을 알 수 있다.
         KProperty 인자를 통해 프로퍼티 이름을 전달받으므로 주 생성자에서는 name 프로퍼티를 없앤다.
    */
    operator fun getValue(p: Person_337, prop: KProperty<*>): Int = propValue
    operator fun setValue(p: Person_337, prop: KProperty<*>, newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(prop.name, oldValue, propValue)
    }
}

class Person_337(val name: String, age: Int, salary: Int): PropertyChangeAware() {
    /*
    private val _age = ObservableProperty("age", age, changeSupport)
    var age: Int
        get() = _age.getValue()
        set(value) {
            _age.setValue(value)
        }
    val _salary = ObservableProperty("salary", salary, changeSupport)
    var salary: Int
        get() = _salary.getValue()
        set(value) {
            _salary.setValue(value)
        }
    */

    // by 키워드를 사용해 위임 객체를 지정하면 직접 코드를 짜야 했던 여러 작업을 코틀린 컴파일러가 자동으로 처리해준다.
    // by 오른쪽에 오는 객체를 위임 객체(delegate)라고 한다.
    // 코틀린은 위임 객체를 감춰진 프로퍼티에 저장하고, 주 객체의 프로퍼티를 읽거나 쓸 때마다 위임 객체의 getValue 와 setValue 를 호출해준다.
    // ObservableProperty 대신 코틀린 표준 라이브러리를 사용해도 된다. 다만 이 표준 라이브러리의 클래스는 PropertyChangeSupport 와는 연결되어 있지 않아 사용하는 방법을 알려주는 람다를 넘겨주어야 한다.
    var age: Int by ObservableProperty(age, changeSupport)
    var salary: Int by ObservableProperty(salary, changeSupport)
}

class Person_339(val name: String, age: Int, salary: Int): PropertyChangeAware() {
    private val observer = {
        prop: KProperty<*>, oldValue: Int, newValue: Int ->
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }
    // by 오른쪽이 꼭 새 인스턴스를 만들 필요는 없다.
    // 다만 우항에 있는 식을 계산한 결과인 객체는 컴파일러가 호출할 수 있는 올바른 타입의 getValue 와 setValue 를 반드시 제공해야한다.
    var age: Int by Delegates.observable(age, observer)
    var salary: Int by Delegates.observable(salary, observer)

}

fun main() {

    val p = Person_335("Alice", 34, 2000)
    p.addPropertyChangeListener(
            PropertyChangeListener { event ->
                // 함수형, SAM 인터페이스
                println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
            }
    )

    p.age = 35

}