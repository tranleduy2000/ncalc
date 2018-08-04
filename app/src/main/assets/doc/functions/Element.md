## Element 

```
Element(symbol, dom)
```

> assume (or test) that the `symbol` is in the domain `dom`.

See:
* [Wikipedia - Domain of a function](https://en.wikipedia.org/wiki/Domain_of_a_function)

### Examples

```
>> Refine(Sin(k*Pi), Element(k, Integers))
0
```

