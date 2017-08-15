## TrigReduce

```
TrigReduce(expr)
```

> rewrites products and powers of trigonometric functions in `expr` in terms of trigonometric functions with combined arguments.
 
### Examples
```
>> TrigReduce(2*Sin(x)*Cos(y))
Sin(-y+x)+Sin(y+x)
``` 