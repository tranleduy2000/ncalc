## Equal

```
Equal(x, y) 

x == y
```

> yields `True` if `x` and `y` are known to be equal, or `False` if `x` and `y` are known to be unequal.

```
lhs == rhs
```
> represents the equation `lhs = rhs`.
 
	
### Examples
 
```
>> a==a
True

>> a==b
a == b

>> 1==1.
True
```

Lists are compared based on their elements:
```
>> {{1}, {2}} == {{1}, {2}}
True
>> {1, 2} == {1, 2, 3}
False
```

Symbolic constants are compared numerically:
```
>> E > 1
True

>> Pi == 3.14
False

>> Pi ^ E == E ^ Pi
False

>> N(E, 3) == N(E)
True

>> {1, 2, 3} < {1, 2, 3}
{1, 2, 3} < {1, 2, 3}

>> E == N(E)
True

>> {Equal(Equal(0, 0), True), Equal(0, 0) == True}
{True, True}

>> {Mod(6, 2) == 0, Mod(6, 4) == 0, (Mod(6, 2) == 0) == (Mod(6, 4) == 0), (Mod(6, 2) == 0) != (Mod(6, 4) == 0)}
{True,False,False,True}

>> a == a == a
True

>> {Equal(), Equal(x), Equal(1)}
{True, True, True}
```