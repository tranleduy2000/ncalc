## ArrayQ
```
'ArrayQ(expr)
```

> tests whether expr is a full array.
	
```
'ArrayQ(expr, pattern)
```

> also tests whether the array depth of expr matches pattern.
	
```
'ArrayQ(expr, pattern, test)
```

> furthermore tests whether `test` yields `True` for all elements of expr. 
 
### Examples
```
>> ArrayQ(a)
False
>> ArrayQ({a})
True
>> ArrayQ({{{a}},{{b,c}}})
False
>> ArrayQ({{a, b}, {c, d}}, 2, SymbolQ)
True
```