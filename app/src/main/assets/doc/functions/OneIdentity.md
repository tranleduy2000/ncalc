## OneIdentity

```
OneIdentity
```
> is an attribute specifying that `f(x)` should be treated as equivalent to `x` in pattern matching.    
 
### Examples
`OneIdentity` affects pattern matching. It does not affect evaluation.    
```
>> SetAttributes(f, OneIdentity)
>> f(a)    
f(a)    
```