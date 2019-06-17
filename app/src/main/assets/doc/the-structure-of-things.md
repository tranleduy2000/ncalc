## The structure of things

Every expression in Symja is built upon the same principle: it consists of a head and an arbitrary number of children, 
unless it is an atom, i.e. it can not be subdivided any further. 
To put it another way: everything is a function call. This can be best seen when displaying expressions in their “full form”:
```
>> FullForm(a + b + c)
Plus(a, b, c)
```

Nested calculations are nested function calls:
```
>> FullForm(a + b * (c + d))
Plus(a, Times(b, Plus(c, d)))
```

Even lists are function calls of the function `List`:
```
>> FullForm({1, 2, 3})
List(1, 2, 3)
```

The head of an expression can be determined with `Head`:
```
>> Head(a + b + c)
Plus
```

The children of an expression can be accessed like list elements:
```
>> (a + b + c)[[2]]
b
```

The head is the `0`th element:
```
>> (a + b + c)[[0]]
Plus
```

The head of an expression can be exchanged using the function `Apply`:
```
>> Apply(g, f(x, y))
g(x,y)

>> Apply(Plus, a * b * c)
a+b+c
```

Apply can be written using the operator `@@`:
```
>> Times @@ {1, 2, 3, 4}
24
```

This exchanges the head List of `{1, 2, 3, 4}` with `Times`, and then the expression `Times(1, 2, 3, 4)` is evaluated, yielding `24`.

Apply can also be applied on a certain level of an expression:
```
>> Apply(f, {{1, 2}, {3, 4}}, {1})
{f(1,2),f(3,4)}
```

Or even on a range of levels:
```
>> Apply(f, {{1, 2}, {3, 4}}, {0, 2})
f(f(1,2),f(3,4))
```

Apply is similar to Map (operator `/@`):
```
>> Map(f, {1, 2, 3, 4})
{f(1),f(2),f(3),f(4)}

>> f /@ {{1, 2}, {3, 4}}
{f({1,2}),f({3,4})}
```

The atoms of Symja are numbers, symbols, and strings. AtomQ tests whether an expression is an atom:
```
>> AtomQ(5)
True

>> AtomQ(a + b)
False
```

The full form of rational and complex numbers looks like they were compound expressions:
```
>> FullForm(3 / 5)
Rational(3,5)

>> FullForm(3 + 4 * I)
Complex(3,4)
```

However, they are still atoms, thus unaffected by applying functions, for instance:
```
>> f @@ Complex(3, 4)
3+I*4
```

Nevertheless, every atom has a head:
```
>> Head /@ {1, 1/2, 2.0, I, "a string", x}
Integer,Rational,Real,Complex,String,Symbol}
```

The operator === tests whether two expressions are the same on a structural level:
```
>> 3 === 3
True

>> 3 == 3.0
True
```

But
```
>> 3 === 3.0
False
```

because 3 (an Integer) and 3.0 (a Real) are structurally different.
