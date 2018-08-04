## MultiplicativeOrder

```
MultiplicativeOrder(a, n)
```

> gives the multiplicative order `a` modulo `n`.

See:

* [Wikipedia: Multiplicative order](https://en.wikipedia.org/wiki/Multiplicative_order)

### Examples

The [A023394 Prime factors of Fermat numbers](https://oeis.org/A023394) integer sequence
 
```
>> Select(Prime(Range(500)), IntegerQ(Log(2, MultiplicativeOrder(2, # )))&) 
{3,5,17,257,641}
```
