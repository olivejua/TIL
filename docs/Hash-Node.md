
## Node-1

```java
public class HashMap {
	...
	static class Node<K, V> implements Map.Entry<K, V> {
		...
	}
}
```

> [!check]
> - Map.Entry를 상속받았구나. HashMap에서 사용하는 요소를 Map 인터페이스 메서드에 만족하는 요소를 받고, 반환하려면 Map 인터페이스에 정의되어있는 Entry를 상속받아야해서겠구나. 그런데 Entry는 어떻게 생겼고, 왜 따로 구현했을까? Map.Entry를 먼저 알아보자.

## Node-2 : Map.Entry

```java
public interface Map<K, V> {
	...
	
	interface Entry<K, V> {
		K getKey();
		V getValue();
		V setValue();
		boolean equals();
		int hashCode();
		
		public static <K extends Comparable<? super K>> Comparator<Map.Entry<K, V>> comparingByKey() {
			return (Comparator<Map.Entry<K, V> & Serializable) (c1, c2) -> c1.getKey().compareTo(c2.getKey());
		}
		
		public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K, V>> comparingByValue() {
			return (Comparator<Map.Entry<K, V>> & Serializable) (c1, c2) -> c1.getValue().compareTo(c2.getValue());
		}
		
		public static <K, V> Comparator<Map.Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
			Objects.requireNonNull(cmp);
			return (Comparator<Map.Entry<K, V>> & Serializable) (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
		}
		
		public static <K, V> Comparator<Map.Entry<K, V>> comparingByValue(Comparator<? super V> cmp) {
			Objects.requireNonNull(cmp)
			return (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
		}
	}
}
```

> [!check]
> - equals()와 hashcode()를 재정의하도록 추상메서드로 선언한 이유는 key, value가 모두 동일할 경우 같은 값으로 인식하게 하라는 의미이다.

> [!question]- comparingByKey()에서 Comparable이 나오는데 Comparable과 Comparator는 무슨 차이일까?
> 둘 다 객체를 정렬하는데 사용되지만 몇가지의 차이점이 있다.
> **Comparable 인터페이스**
> - 정의 : 객체의 자연 순서를 정의하는데 사용된다.
> - 메서드: `compareTo(T o)` 메서드 하나만 존재한다.
> - 용도: 클래스 자체에서 객체의 기본 정렬 순서를 정의하고자 할 때 사용된다.
> > [!example] - 예제: `Comparable` 인터페이스를 구현한 클래스는 다음과 같다.
> > ```java 
> > public class Student implements Comparable\<Student\> {
> > 	private String name;
> > 	private int rollName;
> > 	
> > 	public Student(String name, int rollNumber) {
> > 		this.name = name;
> > 		this.rollNumber = rollNumber;
> > 	}
> > 	
> > 	public int getRollNumber() {
> > 		return rollNumber;
> > 	}
> > 	
> > 	@Override
> > 	public int compareTo(Student other) {
> > 		return Integer.compare(this.rollNumber, other.rollNumber);
> > 	}
> > }
> > ```
> > - 사용: `Collections.sort()` 또는 `Arrays.sort()`를 사용하여 정렬할 수 있다.
> > ```java 
> > List\<Student\> students = new ArrayList<>();
> > students.add(new Student("Alice", 2));
> > students.add(new Student("Bob", 1));
> > Collections.sort();
> > ```
>
> **Comparator 인터페이스**
> - 정의: 특정 순서로 객체를 정렬하는데 사용되는 비교자를 정의한다.
> - 메서드: `compare(T o1, T o2)` 메서드 하나만 존재한다.
> - 용도: 기본 정렬 순서와 다른 기준으로 객체를 정렬하고자할 때 사용한다.
> > [!example] 예제: `Comparator` 인터페이스를 구현한 클래스는 다음과 같다.
> > ```java 
> > public class StudentNameComparator implements Comparator\<Student\> {
> > 	@Override
> > 	public int compare(Student s1, Student s2) {
> > 		return s1.getName().compareTo(s2.getName());
> > 	}
> > }
> > ```
> > - 사용: `Collections.sort()` 또는 `Arrays.sort()` 메서드와 함께 사용한다.
> > ```java 
> > List\<Student\> students = new ArrayList\<\>();
> > students.add(new Student("Alice", 2));
> > students.add(new Student("Bob", 1));
> > Collections.sort(students, new StudentNameComparator());
> > ```

> [!question]- comparingByKey(Comaprator\<? super K\> cmp)에서 왜 미리 Objects.requireNonNull(cmp)을 실행하는걸까?
> 어차피 아래 compare()를 실행하면서 NullPointerException이 나서 한것과 안한것의 결과는 똑같을텐데 미리 검증하는 이유가 뭐지?
>
> -> `Objects.requireNonNull(cmp)`를 사용하는 것은 파라미터가 null이 아님을 명시적으로 확인하여 가독성과 유지보수성을 높이고, 오류를 조기에 발견하여 디버깅을 용이하게 하기 위함이다. 해당 로직이 없다면 NPE 발생시 compare까지 타고 들어가서 알아챘을것이다. 하지만 미리 예를 발생시킴으로써 불필요한 로직까지 확인하는 과정을 줄이고 더 조기에 발견하게 하여 빠르게 파악할 수 있도록 한다.

## Node-3

#### Field & Constructor & Getter
```java
public class HashMap {
	...
	static class Node<K, V> implements Map.Entry<K, V> {
		final int hash;
		final K key;
		V value;
		Node<K, V> next;
		
		Node(int hash, K key, V value, Node\<K, V\> next) {
			this.hash = hash;
			this.key = key;
			this.value = value;
			this.next = next;
		}
		
		public final K getKey() {
			return key;
		}
		
		public final K getValue() {
			return value;
		}
		
		public final String toString() {
			return key + "=" + value;
		}
	}
}
```

> [!check]
> - hash와 key는 final이고 value와 next는 언제나 바뀔 수 있기 때문에 final 선언이 되어있지 않구나


#### hashCode()

```java
public class HashMap {
	...
	static class Node<K, V> implements Map.Entry<K, V> {
		...
		public final int hashcode() {
			return Objects.hashcode(key) ^ Objects.hashcode(value);
		}
	}
}
```

> [!question]- 대부분의 객체에서는 Objects.hashCode()를 사용하는데 어떻게 실행되고 있을까?
> 1. Objects.hashCode
     > 	Objects.hashCode에서도 Object 객체 내 hashCode를 호출하여 반환하고 있다.
     > 	```java 
> 	public class Objects {
> 		...
> 		public static int hashCode(Obejct o) {
> 			return o != null ? o.hashCode() : 0;
> 		}
> 		...
> 	}
> 	```
> 2. Object 클래스 내 hashCode()
     > 	hashCode 메서드는 native 코드로 구현되어 있다.
     > 	```java 
> 	public class Object {
> 		public native int hashCode();
> 	}


#### tableSizeFor(int cap)
> 해시테이블 크기를 결정하기 위해 사용된다. 주어진 용량(cap)을 넘지 않으면서 2의 제곱수 중 가장 가까운 값을 계산한다. 이를 통해 테이블의 크기를 적절하게 설정하고, 해시 충돌을 최소화할 수 있다.

```java
static final int tableSizeFor(int cap) {
	int n = cap - 1;
	n |= n >>> 1;
	n |= n >>> 2;
	n |= n >>> 4;
	n |= n >>> 8;
	n |= n >>> 16;
	return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}
```

1. 초기화 : 입력된 용량 `cap`을 기준으로 1을 뺀 값을 `n`에 저장한다. 이는 2의 제곱수를 계산할 때 `cap`이 정확히 2의 제곱수인 경우를 처리하기 위함이다. 예를 들어 `cap`이 16이면 `n`은 15가 된다.
2. 비트 연산을 통해 가장 가까운 2의 제곱수 계산:
    1. `n`의 비트 패턴을 채워서 가장 가까운 2의 제곱수로 변환한다. 이 연산들이 끝나면 `n`은 `cap`보다 가장 가까운 2의 제곱수 -1이 된다.
3. 최종 크기 결정
    1. `n`이 음수인 경우, 최소 크기 1을 반환한다.
    2. `n`이 최대 용량 `MAXIMUM_CAPACITY` 이상인 경우 최대용량을 반환한다.
    3. 그렇지 않으면 `n + 1`을 반환하여, `cap`보다 크거나 같은 가장 가까운 2의 제곱수를 반환한다.

> [!example]
> 예제 1: `cap = 10`
> 	1. 초기화: `n = 10 - 1 = 9`
> 	2. 비트연산:
> 		- `n |= n >>> 1;` => `9 |= 4;`

**주어진 용량을 넘지 않으면서 2의 제곱수 중 가장 가까운 값을 계산한다.**

> [!question] 1을 빼는 이유는? 빼지 않으면 어떤 결과가 나오지?
> 주어진 용량이 이미 2의 제곱수인 경우 이를 유지해야하는데 1을 빼지 않으면 다음 2의 제곱수를 반환하게 된다.

> [!question]- 그냥 2배를 곱하지 않고 2의 제곱수로 하는 이유는?
> 해시 함수와 인덱싱에서 효율성이 극대화된다.
> **이유 1: 효율적인 인덱스 계산**
> - 해시 테이블의 크기가 2의 제곱수이면, 해시 값을 인덱스로 변환하는 과정이 매우 간단해진다. 비트 연산을 사용하여 인덱스를 계산할 수 있기 때문이다. 예를 들어 해시 테이블의 크기가 2ⁿ이라면, 해시 값의 하위 n비트를 사용하여 인덱스를 계산할 수 있다. 이는 비트마스크 연산으로 쉽게 수행할 수 있다.
    > 	  ```java 
> 	  int index = hash & (table.length - 1);
> 	  ```
    >   여기서 `table.length`는 2의 제곱수이다. 비트마스크 연산은 매우 빠르며, 나눗셈 연산을 피할 수 있다.
>
> **이유 2: 충돌 최소화**
> - 2의 제곱수를 사용하면 해시 함수가 생성하는 해시 값의 비트들이 고르게 분포되도록 도울 수 있다. 이는 해시 충돌을 줄이는데 도움이 된다.
> > [!example] 예시
> > **예시 1: 해시테이블 크기가 2의 제곱수일 때 : 16**
> > - 해시 값: 0b110010 (50)
> > - 테이블 크기: 16 (0b10000)
      > > 인덱스 계산 :
> > ```java 
> > int index = hash & (table.length - 1) // hash & 0b1111(15)
> > // 0b110010 & 0b1111 = 0b0010 (2)
> > ```
> > 결과 인덱스: 2
> > **예시 2: 해시 테이블 크기가 10 (2의 제곱수가 아님)**
> > - 해시 값: 0b110010 (50)
> > - 테이블 크기: 10
> > ```java 
> > int index = hash % table.length = 50 % 10 = 0
> > ```
> > 결과 인덱스: 0
>
> **이유 3: 리사이징의 단순화**
> - 2의 제곱수의 크기를 유지하면, 새크기를 계산하는 것이 간단해지고, 요소를 새로운 해시테이블로 재배치하는 과정도 최적화할 수 있다.
    > ==흠.. 일단 이부분은 좀 더 설득될 수 있는 이유들을 찾아봐야겠다.==



## TreeNode
> `TreeNode` 클래스는 `HashMap`에서 해시 충돌이 발생할 때, 연결 리스트를 대신하여 트리 구조를 사용하는데 사용된다. 이는 충돌이 많이 발생할 경우 성능을 향상시키기 위한것이다.

```java
static final class TreeNode<K, V> extends LinkedHashMap.Entry<K, V> {
	TreeNode<K, V> parent; // red-black tree links
	TreeNode<K, V> left;
	TreeNode<K, V> right;
	TreeNode<K, V> prev;
	
	...
}
```

> [!summary] 역할 및 목적
> - 해시 충돌 해결: `HashMap`에서 동일한 해시 값을 가지는 여러 항목이 있는 경우, 해시 버킷(bucket) 내에서 충돌이 발생한다. 초기에는 이러한 충돌을 연결리스트로 처리하지만, 일정한 임계값(기본적으로 8)을 초과하면 트리 구조로 변환된다.
> - 성능 최적화: 연결리스트는 최악의 경우 시간 복잡도가 O(n)이지만 레드-블랙 트리는 O(log n)이다. 따라서 충돌이 많은 경우 연결리스트 대신 트리 구조를 사용하면 성능이 크게 향상된다.

### 주요 메서드
- find(int h, Object k): 주어진 해시 값과 키를 사용하여 트리에서 해당 노드를 찾는다.
- putTreeVal(HashMap<K, V> map, Node<K, V>[] tab, int h, K k, V v): 트리에 새로운 값을 삽입한다.
- removeTreeNode(HashMap<K, V> map, Node<K, V>[] tab, boolean movable): 트리에서 노드를 제거한다.
- split(HashMap<K, V> map, Node<K, V>[] tab, int index, int bit): 해시 테이블이 리사이징될 때, 트리노드를 새로운 버킷으로 분할한다.


### TreeNode-2: `treeify(Node<K, V>[] tab)`
```java
final void treeify(Node<K, V>[] tab) {
	TreeNode<K, V> root = null;
	for (TreeNode<K, V> x = this, next; x != null; x = next) {
		next = (TreeNode<K, V>) x.next;
		x.left = x.right = null;
		if (root == null) {
			x.parent = null;
			x.red = false;
			root = x;
		}
		else {
			K k = x.key;
			int h = x.hash;
			Class<?> kc = null;
			for (TreeNode<K, V> p = root; ; ) {
				int dir, ph;
				K pk = p.key;
				if ((ph = p.hash) > h) 
					dir = -1;
				else if (ph < h) 
					dir = 1;
				else if ((kc == null && (kc = comparableClassfor(k)) == null) || (dir = compareComparables(kc, k, pk)) == 0)
					dir = tieBreakOrder(k, pk);
				TreeNode<K, V> xp = p
			}
		}
	}
}
```

> [!tip]
> 레드 블랙 트리 구조 이해에 도움되는 블로그글: https://code-lab1.tistory.com/62