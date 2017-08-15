## Through
```
Through(p(f)[x])
```

> gives `p(f(x))`. 

### Examples
```
>> Through(f(g)[x])
f(g(x))
 
>> Through(p(f, g)[x])
p(f(x), g(x))
 
>> Through(p(f, g)[x, y])
p(f(x, y), g(x, y))
 
>> Through(p(f, g)[])
p(f(), g())
 
>> Through(p(f, g))
Through(p(f, g))
 
>> Through(f()[x])
f()
```