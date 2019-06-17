## Off

```
Off( )
```

> switch off the interactive trace. 
 
### Examples

`On()` enables the trace of the evaluation steps.

```
>> On()
  On() --> Null

>> D(Sin(x)+Cos(x), x)

  NotListQ(x) --> True

  D(x,x) --> 1

  D(x,x)*(-1)*Sin(x) --> (-1)*1*Sin(x)

  (-1)*1*Sin(x) --> -Sin(x)

  D(Cos(x),x) --> -Sin(x)

  NotListQ(x) --> True

  D(x,x) --> 1

  Cos(x)*D(x,x) --> 1*Cos(x)

  1*Cos(x) --> Cos(x)

  D(Sin(x),x) --> Cos(x)

  D(Cos(x)+Sin(x),x) --> -Sin(x)+Cos(x)

Cos(x)-Sin(x)
```

`Off()` disables the trace of the evaluation steps.

```
>> Off()

>> D(Sin(x)+Cos(x), x)
Cos(x)-Sin(x)

```

### Related terms 
[On](On.md)