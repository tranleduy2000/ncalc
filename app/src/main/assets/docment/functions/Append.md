## Append

```
Append(expr, item)
```

> returns `expr` with `item` appended to its leaves.

### Examples
```  
>> Append({1, 2, 3}, 4)    
{1,2,3,4}
```

`Append` works on expressions with heads other than `List`:
```    
>> Append(f(a, b), c)    
f(a,b,c)
``` 
 
Unlike `Join`, `Append` does not flatten lists in `item`:   
``` 
>> Append({a, b}, {c, d})    
{a,b,{c,d}}  
```

Nonatomic expression expected.  
```
>> Append(a, b)     
Append(a,b)   
```