
> 이 메서드는 JVM 내부에서 구현된 네이티브 메서드로, 주로 객체의 메모리 주소나 JVM이 객체를 식별하는 내부 정보를 기반으로 해시 코드를 생성한다. JVM마다 구현이 다를 수 있지만, 일반적으로 다음과 같은 방식으로 구현된다.
> 1. 객체의 메모리 주소 기반:
     >    - 많은 JVM 구현에서 `hashCode()`는 객체의 내부 메모리 주소를 기반으로 해시코드를 생성한다. 이는 객체의 고유성을 보장하면서도 빠르게 계산할 수 있다.
> 2. 객체의 내부 식별자 기반:
     >    - 일부 JVM은 객체의 메모리 주소 대신 객체의 내부 식별자를 사용하여 해시코드를 생성한다. 이는 객체 이동이나 압축을 지원하는 가비지 컬렉터 환경에서 유용할 수 있다.

> openjdk 11 기준: https://github.com/openjdk/jdk11u-dev/blob/master/src/hotspot/share/runtime/synchronizer.cpp


## ObjectSynchronizer::FastHashCode

> Object 클래스에서 hashCode() 네이티브 메서드 호출시 아래 코드가 실행되며, 빠르게 해시코드를 계산하기 위해 사용된다. 이 함수는 특히 동기화된 상태에서 효율적으로 해시 코드를 계산할 수 있도록 설계되었다.


**src/hotspot/share/runtime/synchronizer.cpp**
```cpp
intptr_t ObjectSynchronizer::FastHashCode(Thread * Self, oop obj) {
	...
	if (mark->is_neutral()) {
		hash = mark->hash(); // this is a normal header
		if (hash) { // if it has hash , just return it
			return hash;
		}
		hash = get_next_hash(Self, obj); //allocate a new hash code (요기!)
		temp = mark->copy_set_hash(hash); //merge the hash code into header
		...
	}
	...
}
```

1. 마크워드 가져오기 : 객체의 헤더부분인 마크워드를 가져온다.
2. 마크워드 상태 확인: 마크워드가 중립상태(특별한 상태가 없는 경우 해당), 잠금 상태인지, 모니터 상태인지 확인
3. 해시코드 생성: 마크워드에 해시코드가 없으면 `get_next_hash`를 호출하여 새로운 해시코드를 생성한다.
4. 마크워드에 저장: 생성된 해시코드를 마크워드에 저장한다.
5. 해시코드 반환: 최종적으로 해시코드를 반환한다.


## get_next_hash

**src/hotspot/share/runtime/synchronizer.cpp**
```cpp
// hashCode() generation :
//
// Possibilities:
// * MD5Digest of {obj,stwRandom}
// * CRC32 of {obj,stwRandom} or any linear-feedback shift register function.
// * A DES- or AES-style SBox[] mechanism
// * One of the Phi-based schemes, such as:
//   2654435761 = 2^32 * Phi (golden ratio)
//   HashCodeValue = ((uintptr_t(obj) >> 3) * 2654435761) ^ GVars.stwRandom ;
// * A variation of Marsaglia's shift-xor RNG scheme.
// * (obj ^ stwRandom) is appealing, but can result
//   in undesirable regularity in the hashCode values of adjacent objects
//   (objects allocated back-to-back, in particular).  This could potentially
//   result in hashtable collisions and reduced hashtable efficiency.
//   There are simple ways to "diffuse" the middle address bits over the
//   generated hashCode values:

static inline intptr_t get_next_hash(Thread * Self, oop obj) {
	intptr_t value = 0;
	if (hashCode == 0) {
		// This form uses global Park-Miller RNG.
		// On MP system we'll have lots of RW access to a global, so the
		// mechanism induces lots of coherency traffic.
		value = os::random();
	} else if (hashCode == 1) {
		// This variation has the property of being stable (idempotent)
		// between STW operations.  This can be useful in some of the 1-0
		// synchronization schemes.
		intptr_t addrBits = cast_from_oop<intptr_t>(obj) >> 3;
		value = addrBits ^ (addrBits >> 5) ^ GVars.stwRandom;
	} else if (hashCode == 2) {
		value = 1;            // for sensitivity testing
	} else if (hashCode == 3) {
		value = ++GVars.hcSequence;
	} else if (hashCode == 4) {
		value = cast_from_oop<intptr_t>(obj);
	} else {
		// Marsaglia's xor-shift scheme with thread-specific state
		// This is probably the best overall implementation -- we'll
		// likely make this the default in future releases.
		unsigned t = Self->_hashStateX;
		t ^= (t << 11);
		Self->_hashStateX = Self->_hashStateY;
		Self->_hashStateY = Self->_hashStateZ;
		Self->_hashStateZ = Self->_hashStateW;
		unsigned v = Self->_hashStateW;
		v = (v ^ (v >> 19)) ^ (t ^ (t >> 8));
		Self->_hashStateW = v;
		value = v;
	}
	
	value &= markOopDesc::hash_mask;
	
	if (value == 0) value = 0xBAD;
	
	assert(value != markOopDesc::no_hash, "invariant");
	TEVENT(hashCode: GENERATE);
	
	return value;
}
```

### 1. `hashCode == 0`
Park-Miller RNG
```cpp
value = os::random();
```
- 전역 난수 생성기를 사용하여 해시코드를 생성한다. 이 방법은 다중 프로세서 시스템에서 많은 읽기/쓰기 접근을 유발할 수 있다.

### 2. `hashCode == 1`
Idempotent hash
```cpp
intptr_t addrBits = cast_from_oop<intptr_t>(obj) >> 3;
value = addrBits & (addrBits >> 5) ^ GVars.stwRandom;
```
- 객체의 주소 비트를 기반으로 해시 코드를 생성한다. Stop-The-World(STW) 작업 사이에서 안정적이다.
### 3. `hashCode == 2`
Constant vallue for testing
```cpp
value = 1
```
- 해시코드로 항상 1을 반환한다. 이는 민감도 테스트에 사용될 수 있다.

### 4. `hashCode == 3`
Sequence-based
```cpp
value = ++GVars.hcSequence;
```
- 전역 시퀀스 카운터를 사용하여 해시 코드를 생성한다.

### 5. `hashCode == 4`
Direct address
```cpp
value = cast_from_oop<intptr_t>(obj);
```
- 객체의 메모리 주소를 직접 해시코드로 사용한다.
### 6. Default (Marsaglia's xor-shift):
```cpp
unsigned t = Self->_hashStateX;
t ^= (t << 11);
Self->_hashStateX = Self->_hashStateY;
Self->_hashStateY = Self->_hashStateZ;
Self->_hashStateZ = Self->_hashStateW;
unsigned v = Self->_hashStateW;
v = (v ^ (v >> 19)) ^ (t ^ (t >> 8));
Self->_hashStateW = v;
value = v;
```
- Marsaglia의 XOR-Shift 알고리즘을 사용하여 스레드별 상태에서 해시 코드를 생성한다. 이는 향후 기본 구현으로 채택될 가능성이 있다.

이 구현은 MArsaglia의 XOR-Shift 알고리즘의 변형으로, 여러 상태 변수를 사용하여 난수를 생성한다. 각 상태 변수는 업데이트되며, 결과는 다음과 같은 방식으로 계산된다.
1. 현재 상태 변수 `Self->_hashStateX`를 `t`로 복사한다.
2. `t`에 대해 XOR와 비트 시프트 연산을 수행한다.
3. 상태 변수를 업데이트한다.
   - `Self->_hashStateX`는 `Self->_hashSateY`로 업데이트된다.
   - `Self->_hashStateY`는 `Self->_hashSateZ`로 업데이트된다.
   - `Self->_hahsStateZ`는 `Self->_hashSateW`로 업데이트된다.
4. 마지막 상태변수 `Self->_hashStateW`를 `v`로 복사한다.
5. `v`에 대해 XOR와 비트 시프트 연산을 수행하여 최종 난수를 계산한다.
6. 업데이트된 `v`를 `Self->_hashStateW`에 저장한다.
7. 최종 난수 값을 `value`에 저장한다.

> [!summary]
> 각각 다른 초기 상태 변수가 있고, 이 상태변수들을 순환 이동시킨 후 마지막 변수는 최종 연산된 값(비트 혼합)으로 업데이트된다. 최종적으로 `value`는 상태 변수들의 혼합 결과로 생성된 난수 값을 반환한다.

> [!info] Marsaglia의 XOR-Shift 알고리즘
> 이 알고리즘은 단순한 비트 시프트와 XOR 연산을 사용하여 난수를 생성한다. 이 방법은 다른 복잡한 난수 생성기보다 계산 비용이 적고 구현이 간단하면서도, 꽤 좋은 품질의 난수를 제공한다.
> **기본 원칙**
> 1. 초기 상태(시드)로 시작한다.
> 2. 상태를 업데이트하기 위해 XOR와 비트 시프트 연산을 반복적으로 적용한다.
> 3. 이 연산을 통해 새로운 난수 값을 생성한다.
>
> **형태**
> ```c
> unsigned int xorShift(unsigned int state) {
> 	state ^= state << a;
> 	state ^= state >> b;
> 	state ^= state << c;
> 	return state;
> }
> ```
> 여기서 `a`, `b`, `c`는 특정 상수 값으로, 일반적으로 좋은 난수 특정을 얻기 위해 튜닝된다.
>
> **장점**
> 1. 속도: XOR와 비트 시프트연산은 매우 빠르며, 하드웨어에서 직접 지원된다.
> 2. 단순성: 알고리즘이 매우 간단하여 이해하고 구현하기 쉽다.
> 3. 적절한 품질: 적절히 선택된 시프트 값과 초기 상태를 사용하면, 충분히 균일한 분포를 가진 난수를 생성할 수 있다.
> 4. 작은 메모리 사용: 매우 작은 상태 변수만 필요로 하여, 이는 메모리 사용을 최소화한다.


### 후처리
#### 1. 비트 마스크 적용
```cpp
value &= markOopDesc::hash_mark;
```
- 해시 값에 마스크를 적용하여 값의 범위를 제한한다.
#### 2. 값이 0이면 초기화
```cpp
if (value == 0) value = 0xBAD
```
- 해시 값이 0이면, 특정 초기화 값을 설정한다.
#### 3. 유효성 검증
```cpp
assert(value != markOopDesc::no_hash, "invariant");
```
- 해시 값이 `no_hash` 값과 같지 않은지 검증한다.
#### 4. 이벤트 기록
```cpp
TEVENT(hashCode: GENERATE);
```
- 해시코드 생성 이벤트를 기록한다.

> 이 함수는 객체의 해시코드를 생성하는 다양한 방법을 제공한다. 각 방법은 특정 상황에 맞게 최적화되어 있으며, JVM의 설정이나 요구사항에 따라 다르게 선택될 수 있다. 최종적으로 생성된 해시 값은 해시 마스크를 적용받고, 유효성을 검증한 후 반환된다.


## ciObject::hash()
`ident()` 메서드를 호출하여 객체의 식별자(ID)를 가져온 후, 이를 특정 상수 값과 곱하여 해시 코드를 생성한다.

### 코드 분석
```cpp
int ciObject::hash() {
	return ident() * 31;
}
```
1. `ident()`
    - 이 메서드는 객체의 고유 식별자를 반환한다. 이 식별자는 객체를 유일하게 식별하기 위한 값이다.
    - `ident()` 메서드는 `_ident`멤버 변수에서 플래그 비트를 제외한 값을 반환한다.
2. 해시 코드 계산
    - `ident()` 메서드가 반환하는 객체의 식별자를 31이라는 상수 값과 곱한다.
    - 31은 해시 함수에서 자주 사용되는 소수(prime number)이다. 소수를 곱하는 이유는 해시 코드를 더욱 고르게 분포시키기 위함이다. 소스는 곱셈에 의해 해시 값이 더 다양한 값으로 분포되도록 하여, 해시 테이블에서 충돌을 줄이는 효과가 있다. 충돌을 줄임으로써 성능이 향상된다.

> [!info] 플래그비트 (FLAG_BITS)
> `_ident` 값을 `FLAG_BITS` 만큼 오른쪽으로 시프트한다. 이로인해 `_ident`의 하위 `FLAG_BITS`비트는 제거되고 상위 비트들만 남게 된다. `FLAG_BITS`는 객체의 플래그 비트 수를 나타내며, 객체의 특정 상태나 속성을 나타내기 위해 사용된다.

