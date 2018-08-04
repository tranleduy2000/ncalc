## BooleanTable

```
BooleanTable(logical-expr, variables)
```

> generate [truth values](https://en.wikipedia.org/wiki/Truth_table) from the `logical-expr`
 
  
### Examples

```
>> BooleanTable(Implies(Implies(p, q), r), {p, q, r})
{True,False,True,True,True,False,True,False}

>> BooleanTable(Xor(p, q, r), {p, q, r})
{True,False,False,True,False,True,True,False}
```