## BooleanConvert

```
BooleanConvert(logical-expr)
```

> convert the `logical-expr` to [disjunctive normal form](https://en.wikipedia.org/wiki/Disjunctive_normal_form)

```
BooleanConvert(logical-expr, "CNF")
```

> convert the `logical-expr` to [conjunctive normal form](https://en.wikipedia.org/wiki/Conjunctive_normal_form)

```
BooleanConvert(logical-expr, "DNF")
```

> convert the `logical-expr` to [disjunctive normal form](https://en.wikipedia.org/wiki/Disjunctive_normal_form)
 
### Examples

```
>> BooleanConvert(Xor(x,y))
x&&!y||y&&!x

>> BooleanConvert(Xor(x,y), "CNF")
(x||y)&&(!x||!y)

>> BooleanConvert(Xor(x,y), "DNF")
x&&!y||y&&!x
```