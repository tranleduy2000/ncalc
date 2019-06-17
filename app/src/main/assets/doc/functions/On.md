## On

```
On( )
```

> switch on the interactive trace. The output is printed in the defined `out` stream.
 

```
On({head1, head2,... })
```

> switch on the interactive trace only for the `head`s defined in the list. The output is printed in the defined `out` stream.

```
On({head1, head2,... }, Unique)
```

or

```
On(All, Unique)
```

> switch on the interactive trace only for the defined `head` s. The output is printed only once for a combination of _unevaluated_ input expression and _evaluated_ output expression.


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
[Off](Off.md)