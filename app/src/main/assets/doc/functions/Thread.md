## Thread
```
Thread(f(args)
```

> threads `f` over any lists that appear in `args`.
	
```
Thread(f(args), h)
```

> threads over any parts with head `h`. 

### Examples
```
>> Thread(f({a, b, c}))
{f(a),f(b),f(c)}
 
>> Thread(f({a, b, c}, t))
{f(a,t),f(b,t),f(c,t)}
 
>> Thread(f(a + b + c), Plus)
f(a)+f(b)+f(c)
```

Functions with attribute `Listable` are automatically threaded over lists:
```
>> {a, b, c} + {d, e, f} + g
{a+d+g,b+e+g,c+f+g} 
```