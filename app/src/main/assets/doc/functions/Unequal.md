## Unequal

```
Unequal(x, y) 

x != y
```

> yields `False` if `x` and `y` are known to be equal, or `True` if `x` and `y` are known to be unequal.

```
lhs != rhs
```
> represents the inequality  `lhs <> rhs`.
 
	
### Examples
 
```
>> 1 != 1.
False
```

Lists are compared based on their elements:
```
>> {1} != {2}
True
 
>> {1, 2} != {1, 2}
False
 
>> {a} != {a}
False
 
>> "a" != "b"
True
 
>> "a" != "a"
False
 
>> Pi != N(Pi)
False
 
>> a_ != b_
a_ != b_
 
>> a != a != a
False
 
>> "abc" != "def" != "abc"
False

>> a != a != b
False

>> a != b != a
a != b != a

>> {Unequal(), Unequal(x), Unequal(1)}
{True, True, True}
```