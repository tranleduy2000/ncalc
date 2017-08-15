## Prepend

```
Prepend(expr, item)
```

> returns `expr` with `item` prepended to its leaves.

### Examples

`Prepend` is similar to `Append`, but adds `item` to the beginning of `expr`:
```  
>> Prepend({2, 3, 4}, 1)    
{1,2,3,4}    
```

`Prepend` works on expressions with heads other than 'List':    
```
>> Prepend(f(b, c), a)    
f(a,b,c)    
```

Unlike `Join`, `Prepend` does not flatten lists in `item`:   
``` 
>> Prepend({c, d}, {a, b})  
{{a,b},c,d}   
```

Nonatomic expression expected.  
```
>> Prepend(a, b)       
Prepend(a,b)   
```