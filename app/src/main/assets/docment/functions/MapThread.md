## MapThread

```
MapThread(`f`, {{`a1`, `a2`, ...}, {`b1`, `b2`, ...}, ...})
```

> returns '{`f`(`a1`, `b1`, ...), `f`(`a2`, `b2`, ...), ...}'.   

```  
MapThread(`f`, {`expr1`, `expr2`, ...}, `n`)
```

> applies `f` at level `n`.    

### Examples
```  
>> MapThread(f, {{a, b, c}, {1, 2, 3}})       
{f(a,1),f(b,2),f(c,3)}
 
>> MapThread(f, {{{a, b}, {c, d}}, {{e, f}, {g, h}}}, 2)    
{{f(a, e), f(b, f)}, {f(c, g), f(d, h)}}    
```

Non-negative machine-sized integer expected at position 3 in MapThread(f, {{a, b}, {c, d}}, {1}).  
```
>> MapThread(f, {{a, b}, {c, d}}, {1})    
MapThread(f, {{a, b}, {c, d}}, {1})    
```

Object {a, b} at position {2, 1} in MapThread(f, {{a, b}, {c, d}}, 2) has only 1 of required 2 dimensions.   
```
>> MapThread(f, {{a, b}, {c, d}}, 2)   
MapThread(f, {{a, b}, {c, d}}, 2) 
```

Incompatible dimensions of objects at positions {2, 1} and {2, 2} of MapThread(f, {{a}, {b, c}}); dimensions are 1 and 2.    
``` 
>> MapThread(f, {{a}, {b, c}})    
MapThread(f, {{a}, {b, c}})    
 
>> MapThread(f, {})    
{}    
 
>> MapThread(f, {a, b}, 0)    
f(a, b)    
``` 

Object a at position {2, 1} in MapThread(f, {a, b}, 1) has only 0 of required 1 dimensions. 
```    
>> MapThread(f, {a, b}, 1)    
MapThread(f, {a, b}, 1)    
```    
    