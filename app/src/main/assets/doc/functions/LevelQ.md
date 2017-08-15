## LevelQ
```  
LevelQ(expr)
```
> tests whether `expr` is a valid level specification.
	
### Examples
```
>> LevelQ(2)
True

>> LevelQ({2, 4})
True

>> LevelQ(Infinity)
True

>> LevelQ(a + b)
False
```