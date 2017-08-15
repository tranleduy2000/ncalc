## Sow

```
Sow(expr)
```

> sends the value `expr` to the innermost `Reap`.

### Examples  
```
>> Reap(Sow(3); Sow(1))
{1,{{3,1}}}
``` 