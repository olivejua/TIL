
> Put -> Remove -> Get

## put()

### put()-1
```java
public V put(K key, V value) {
	return putVal(hash(key), key, value, false, true);
}
```

> [!question]- hash(key) 에서 hash는 어떻게 동작하고, 무슨 역할이지? 얘가 왜 있지? 무슨 장단점이 있어서?
> > [!todo] 우선 hash key를 만들어준다고 치고 큰 흐름부터 접근하자.

### put()-2 : putVal()


```java
/**
* @param hash hash for key
* @param key the key
* @param value the value to put
* @param onlyIfAbsent if true, don't change existing value (비어있을 경우에만 저장)
* @param evict if false, the table is in creation mode
* @return previous value, of null if none
*/
final V putVAl(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
	...
}

```

> [!check]
> - hash는 Int 타입이구나
> - evict는 무슨 역할이지?
> - 리턴타입으로 이전 값을 전달해주는 이유가 뭘까?

이어서 구현로직으로 살펴보자
```java
final V putVAl(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
	Node<K, V>[] tab; Node<K, V> p; int n, i
}
```

> [!check]
> - Node 타입이 등장하네. Map은 Node로 구성된 Collection이구나. 중요한 역할이겠다. Node를 먼저 알아보자.
    > 	-> [[4주차 과제) HashMap - Node]]


### put()-3 : hash()

```java
public class HashMap<K, V> extends AbstractMap<K, V> {
	...
	static final int hash(Object key) {
		int h;
		return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	}
}
```

1. `int h;`: 변수선언 및 초기화
2. `(key == null) ? 0 : (h == key.hashCode()) ^ (h >>> 16)`
    - Key가 null이면 0을 반환한다.
    - `h = key.hashCode()`: `key` 객체의 해시코드를 계산하고 이를 `h`에 저장한다.
    - `h >>> 16`: 해시코드의 상위 16비트를 하위16비트로 이동시킨다.
    - `(h = key.hashCode()) ^ (h >>> 16)`: 원래 해시 코드 `h`와 상위 16비트를 시프트한 값을 비트 XOR 연산한다.

> [!info]- 비트 연산 설명
> - `^`는 비트 XOR 연산자이다. 두 비트가 다르다면 `1`, 같으면 `0`을 반환한다.
> > [!example] 예시
> > - 첫 비트: `1` XOR `0` : `1`
> > - 두번째 비트: `0` XOR `0`: `0`
> > - 세번째 비트: `1` XOR `0`: `1`
> - `>>>`는 부호없는 우측 우측 시프트 연산자이다. `h >>> 16`은 상위 16비트를 하위 16비트로 이동시킨다.
> > [!example] 예시
> > 예를 들어, 키 객체의 해시코드가 `0xABCD1234`라고 가정해보자
> >
> > 1. 해시코드 계산
       > >    ```java 
> >    h = key.hashCode(); // h = 0xABCD1234
> >    ```
> > 2. 상위 16비트를 하위 16비트로 시프트
       > >    ```java 
> >    h >>> 16; // 0x0000ABCD
> >    ```
> > 3. 비트 XOR 연산
       > >    ```java 
> >    h ^ (h >>> 16); // 0xABCD1234 ^ 0x0000ABCD
> >    ```
       > >    - 원래 해시코드: `0xABCD1234` (이진수: `10101011110011010001001000110100`)
> >    - 상위 16비트를 하위로 시프트: `0x0000ABCD` (이진수: `00000000000000001010101111001101`)
> >    - XOR 연산 결과: `0xABCD6789` (이진수: `10101011110011010110011110001001`)

> [!info]- 상위 16비트를 하위 16비트로 시프트하는 이유
> 해시코드의 상위 비트와 하위비트를 혼합하여 해시 값을 더 고르게 분포시키기 위함이다. 이 과정은 해시 테이블의 성능을 최적화하고 해시 충돌을 줄이는데 도움이 된다.
> 1. 비트 분포 균형: 객체의 해시코드에서 상위 비트와 하위 비트를 혼합하면, 해시 값이 보다 균일하게 분포된다.
> 2. 충돌 감소: 해시코드의 하위 비트만 사용하는 경우, 비슷한 해시코드들이 동일한 버킷에 몰릴 수 있다. 상위 비트를 혼합하면 이러한 충돌가능성을 줄여준다.
>
> > [!example] 예시
> > 해시코드가 `0xABCD1234`인 객체를 예를 들어 설명해보자.
> > - 원래 해시코드: `0xABCD1234` (이진수: `10101011110011010001001000110100`)
> > - 혼합된 해시코드: `0xABCD6789` (이진수: `10101011110011010110011110001001`)
> > - XOR 연산
      > >   ```java 
> >   10101011110011010001001000110100
> >   ^ 00000000000000001010101111001101
> >   //--------------------------------------
> >   10101011110011010110011110001001 (0xABCD6789)
> >   ```

#### Object hashCode()

```java
public class Object {
	...
	
	public native int hashCode();
	
	...
}
```

> [!info] hashCode() 주석
> 객체에 대한 해시코드 값을 반환한다. 이 메서드는  `java.util.HashMap`과 같은 해시 테이블을 지원하기 위해 제공된다.
> - `hashCode`의 일반적인 조건:
    > 	- Java 애플리케이션 실행 중에 동일한 객체에 대해 여러번 호출될 때, 객체의 `equals` 비교에 사용되는 정보가 수정되지 않는 한, `hashCode` 메서드는 일관되게 동일한 정수를 반환해야한다. 이 정수는 애플리케이션의 한번의 실행에서 다른 실행으로 일관되게 유지될 필요는 없다.
           > 	- 두 객체가 `equals(Object)`메서드에 따라 같다면, 두 객체 각각에 대해 `hashCode`메서드를 호출하면 동일한 정수 결과를 반환해야 한다.
> 	- 두 객체가 `equals(Object)` 메서드에 따라 같지 않더라도, 각 객체에 대해 `hashCode` 메서드를 호출하면 서로 다른 정수 결과를 반환해야한다는 요구사항은 없다. 그러나 프로그래머는 같지 않는 객체에 대해 서로 다른 정수결과를 생성하는 것이 해시 테이블의 성능을 향상시킬 수 있다는 점을 인식해야 한다.
> 	- 합리적으로 실용적인 범위 내에서 `Object` 클래스가 정의한 `hashCode` 메서드는 서로 다른 객체에 대해 서로 다른 정수를 반환한다. (이는 일반적으로 객체의 내부 주소를 정수로 변환하여 구현되지만, 이 구현 기법은 Java 프로그래밍 언어에서 요구하지 않는다.)


##### 메서드 구현 내용
[[4주차 과제) JVM - hashCode()]]


### put()-4 : putVal()
```java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
	Node<K, V>[] tab; Node<K, V> p; int n, i;
	if ((tab = table) == null || (n = tab.length) == 0) {
		n = (tab = resize()).length;
	}
}
```

> [!check]
> table이 null이거나 내용이 비어있으면 **리사이징**하고, `n` 변수에 table의 크기를 할당함. 리사이징을 어떤식으로 하는지 알아봐야겠다.

### put()-5 : resize()

> [!info] threshold
> 임계값을 가리킨다. 보통 이 수치를 넘으면 리사이징 대상이 된다.
> 예: 테이블 용량이 16이고, LOAD_FACTOR가 75% 이면 `16 * 0.75 = 12` 12개 이상이 되면 리사이징 대상이 된다.
#### 해석
```java
final Node<K, V>[] resize() {
	Node<K, V>[] oldTab = table; // 현재 해시테이블
	int oldCap = (oldTab == null) ? 0 : oldTab.length; // 현재 해시테이블의 크기(용량)이며, 테이블이 null인경우 0으로 설정됨
	int oldThr = threshold; // 현재 임계값
	int newCap, newThr = 0;
	
	// -------------------------------------------------------------
	// -- 기존테이블이 임계값을 넘었는지 확인 후 새 테이블의 용량과 임계값을 초기화 --
	// -------------------------------------------------------------
	if (oldCap > 0) {
		// 기존테이블 크기가 최대용량을 넘었다면 
		if (oldCap >= MAXIMUM_CAPACITY) {
			// 임계값만 Integer.MAX_VALUE로 설정하고 사이즈를 늘리지 않고, 기존테이블을 반환 후 종료한다.
			threshold = Integer.MAX_VALUE;
			return oldTab;
		}
		// 2배의 크기로 늘린값이 임계값 미만이면서 기존 테이블 크기가 기본 초기 용량 이상이면 2배의 크기로 늘린다.
		else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY) {
			newThr = oldThr << 1; // double threshold
		}
	}
	// 현재 테이블크기가 0이고, 임계값이 초기용량으로 설정되어있는 경우
	else if (oldThr > 0) {
		// 기존 임계값을 테이블의 크기로 할당한다.
		newCap = oldThr;
	}
	// 임계값이 0인 경우
	else {
		// 기본 초기용량을 테이블크기로 할당
		newCap = DEFAULT_INITIAL_CAPACITY; // DEFAULT_INITIAL_CAPACITY = 기본값 `1 << 4 = 16`
		newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY); // 초기용량에 임계값을 계산하여 할당
	}
	
	// 새로운 임계값 계산
	if (newThr == 0) {
		float ft = (float) newCap * loadFactor;
		newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ? (int)ft : Integer.MAX_VALUE);
	}
	
	threshold = newThr;
	Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
	table = newTab;
	if(oldTab != null) {
		for (int j = 0; j < oldCap; ++j) {
			Node<K, V> e;
			if((e = oldTab[j]) != null) {
				oldTab[j] = null;
				
				// 해시 키에 값이 하나인 경우 
				if (e.next == null) {
					// 새용랴에 맞춘 인덱스로 노드를 할당 (할당크기의 2의 제곱수 자리에 맞게 해시키가 1로 채워진 인덱스 값으로 할당됨)
					newTab[e.hash & (newCap - 1)] = e;
				}
				
				// 해시키에 값의 연결리스트가 일정 길이(8) 이상이어서 TreeNode구조인 경우
				else if (e instance TreeNode) {
				
					((TreeNode<K, V>) e).split(this, newTab, j, oldCap);
				}
				
				// 해시키에 값이 연결리스트인 경우 
				else {
					Node<K, V> loHead = null, loTail = null;
					Node<K, V> hiHead = null, hiTail = null;
					Node<K, V> next;
					do {
						next = e.next();
						if ((e.hash & oldCap) == 0) {
							if(loTail == null)
								loHead = e;
							else 
								loTail.next = e;
							loTail = e;
						}
						else {  
						    if (hiTail == null)  
						        hiHead = e;  
						    else  
						        hiTail.next = e;  
						    hiTail = e;  
						}
					} while ((e = next) != null);
					if (loTail != null) {  
					    loTail.next = null;  
					    newTab[j] = loHead;  
					}  
					if (hiTail != null) {  
					    hiTail.next = null;  
					    newTab[j + oldCap] = hiHead;  
					}
				}
			}
		}
	}
	if (oldTab != null) {
		
	}
}
```

> [!info] 주석
> Initializes or doubles table size. if null, allocates in accord with initial capacity target held in field threshold. Otherwise, because we are using power-of-two expansion, the elements from each bin must either stay at same index or move with a power of two offset in the new table.
> -> 테이블 사이즈를 초기화하거나 두배로 늘린다. 만약 테이블이 null이라면 `threshold`변수의 값에 따라 초기 용량으로 할당한다. 그렇지 않으면 2의 제곱수 확장을 사용하기 때문에 각 빈(bin) 요소는 동일한 인덱스에 남아있거나 새 테이블에서 2의 제곱수만큼의 오프셋으로 이동해야한다.


### put()-6 : putVal()
```java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
	Node<K, V>[] tab; Node<K, V> p; int n, i;
	
	...
	
	// i = 주입할 hash값의 인덱스, p = 주입할 인덱스에 node, node가 없다면
	if ((p = tab[i = (n - 1) & hash]) == null) {
		// 새 노드를 생성하여 인덱스에 저장
		tab[i] = newNode(hash, key, value, null);
	}
	// 주입하려는 인덱스에 node가 이미 존재한다면
	else {
		Node<K, V> e; K k;
		// 기존 인덱스의 헤드노드와 주입하려는 노드의 해시값과 키 값이 같다면 
		if (p.hash == hash && 
				((k = p.key) == key || (key != null && key.equals(k))))
			// 기존노드의 주소값을 주입할 노드 값으로 주입
			e = p;
		// 기존 노드가 TreeNode라면
		else if (p instance TreeNode)
			// TreeNode에 삽입 (좀 더 알아보기)
			e = ((TreeNode<K, V>) p).putTreeVal(this, tab, hash, key, value);
		// 인덱스만 같은 다른 노드이면서 일반 연결리스트라면
		else {
			//연결리스트 next로 타고 가면서 주입할 수 있는 node 찾기
			for (int binCount = 0; ; ++binCount) {
				// next가 비어있는 끝자리 노드라면
				if ((e = p.next) == null) {
					// next에 새 노드 생성 후 주입
					p.next = newNode(hash, key, value, null);
					// 연결리스트 개수가 treeNode생성 임계치를 이상이라면 , (TREEIFY_THRESHOLD = 8)
					if (binCount >= TREEIFY_THRESHOLD - 1)
						treeifyBin(tab, hash);
					break;
				}
				// 연결리스트 중에 주입하려는 키와 해시값이 같은 값이라면
				if (e.hash == hash && ((k = e.key) == key || key != null & key.equals(k))) {
					break;
				}
				// 다음 요소의 노드를 p에 저장 (다음 반복 루프 차례에서는 P가 현재노드가 됨)
				p = e;
			}
		}
		
		if (e != null) {
			// oldValue = 주입할 노드의 값
			V oldValue = e.value;
			//비어있을 때만 업데이트하는 옵션이 false이거나 기존 값이 없는경우
			if (!onlyIfAbsent || oldValue == null) {
				// 새로운 값으로 업데이트
				e.value = value;
			}
			// LinkedHashMap일 때만 로직이 존재함
			afterNodeAccess(e)
			return oldValue;
		}
		
		++modCount;
		// 증가된 사이즈가 임계치를 초과했다면
		if (++size > threshold) 
			//해시 테이블 리사이징
			resize();
		// LinkedHashMap일 경우만 로직이 존재함
		afterNodeInsertion(evict);
	}
}

// Create a regular (non-tree) node
Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
	return new Node<>(hash, key, value, next);
}

final void treeifyBin(Node<K, V>[] tab, int hash) {
	int n, index; Node<K, V> e;
	
	// table이 비어있거나 테이블 크기가 최소 용량 64 미만이라면 테이블을 리사이징
	if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
		resize();
	
	// 트리노드 생성 대상이라면
	else if ((e = tab[index = (n - 1) & hash]) != null) {
		// hd = head TreeNode, tl = 이전 TreeNode
		TreeNode<K, V> hd = null, tl = null;
		do {
			TreeNode<K, V> p = replacementTreeNode(e, null);
			if (tl == null) {
				hd = p;
			}
			else {
				p.prev = tl;
				tl.next = p;
			}
			tl = p;
		} while ((e = e.next) != null);
		
		// 해시 테이블의 인덱스에 TreeNode head 노드로 저장
		if ((tab[index] = hd) != null) 
			hd.treeify(tab);
	}
}

TreeNode<K, V> replacementTreeNode(Node<K, V> p, Node<K, V> next) {
	return new TreeNode<>(p.hash, p.key, p.value, next);
}
```




## remove()
### remove()-1: `V remove(Object key)`
```java
public V remove(Object key) {
	Node<K, V> e;
	return (e = removeNode(hash(key), key, null, false)) == null ? null: e.value;
}
```

> [!check]
> e에는 제거된 노드가 담기고 제거된 노드가 존재하지 않으면 null 리턴, 존재하면 e의 value를 리턴하는구나
> removeNode가 put()->putVal() 처럼 실질적인 역할을 하는 것으로 보이네

### remove()-2: `Node<K, V> removeNode()`

```java
/**
* matchValue - if true only remove if value is equals , 값이 같을 때만 제거
* movable - if false do not move other nodes while removing , false라면 제거되는 도중에 노드들을 이동시키지 않는다.
*/
final Node<K,  V> removeNode(int hash, Object key, Object value, boolean matchValue, boolean movable) {
	Node<K, V>[] tab; Node<K, V> p; int n, index;
	
	// table에 노드들이 있고, 주어진 해시값의 인덱스로 값이 존재한다면
	//
	if ((tab = table) != null && (n = tab.length) > 0  && (p = tab[index = (n - 1) & hash]) != null) {
		// node = 실제 제거할 노드가 담길 예정
		// e = 기존 저장된 노드중에 검증할 노드가 담길 예정
		Node<K, V> node = null, e; K k, V v;
		// 노드 해시값과 키값이 제거할 해시값과 키값과 같다면
		if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))) {
			node = p;
		}
		else if ((e = p.next) != null) {
			// 해시테이블에 있는 노드가 트리노드라면 트리노드에서 해시와 일치하는 노드를 검색 후 node에 저장
			if (p instanceof TreeNode) 
				node = ((TreeNode<K, V>) p).getTreeNode(hash, key);
			else {
				do {
					// 해시값이 같고, 키값이 같다면 node 변수에 현재 노드 업데이트
					if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
						node = e;
						break;
				} while ((e = e.next) != null);
			}
		}
		//제거할 노드가 존재하고, 제거할 대상이라면
		if (node != null && (!matchValue || (v = node.value) == value || (value != null && value.equals(value)))) {
			// TreeNode면 노드를 TreeNode 에 해당 요소 제거
			if (node instance TreeNode) {
				((TreeNode<K, V>) node).removeTreeNode(this, tab, movable);
			}
			
			// 제거해야할 노드가 해시테이블인덱스의 헤드노드라면 해당 인덱스에 제거할 노드의 다음노드로 할당
			//예 1: 2(제거노드) -> 3 -> 4 => 1: 3 -> 4
			else if (node == p) {
				tab[index] = node.next;
			}
			
			// 제거해야하는 노드가 연결리스트의 노드중 하나라면
			//예 1: 2 -> 3(제거노드) -> 4 => 1: 2 -> 4
			else 
				// 
				p.next = node.next;
			++modeCount;
			--size;
			afterNodeRemoval(node);
			return node;
		}
	}
	
	return null;
}
```


## get()

### get()-1: `get()`
```java
public V get(Object key) {
	Node<K, V> e;
	return (e = getNode(hash(key), key)) == null ? null : e.value;
}
```
> [!check]
> getNode가 실질적인 기능을 실행하는구나, 값을 검색해서 존재하면 반환하고 존재하지 않으면 null을 반환한다.

### get()-2: `getNode()`
```java
final Node<K, V> getNode(int hash, Object key) {
	Node<K, V>[] tab; Node<K, V> first, e; int n; K k;
	
	//해시테이블 존재하고 조회할 해시값의 인덱스에 노드가 존재한다면
	if ((tab = table) != null && (n = tab.length) > 0 && 
			(first = tab[(n - 1) & hash]) != null) {
		
		// 노드 연결리스트 헤드노드와 조회할 해시값이 같고, 키값이 같을 경우
		if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k)))) 
			// 헤드노드 반환
			return first;
		
		// 연결리스트의 next 노드가 존재한다면
		if ((e = first.next) != null) {
			// 트리노드라면 
			if (first instanceof TreeNode) 
				트리노드에서 해시와 키가 일치하는 노드 검색 반환
				return ((TreeNode<K, V>) first).getTreeNode(hash, key);
			do {
				//해시와 키가 일치하는 노드가 나오면 반환
				if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
					return e;
			} while ((e = e.next) != null);
		}
	}
	
	return null;
}
```



